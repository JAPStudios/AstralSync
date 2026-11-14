package com.example.data

import java.util.Calendar
import java.util.TimeZone
import kotlin.math.*

/**
 * Astronomical estimates based on the NOAA solar position approximation.
 * Estimates astronomical sunrise (top edge on horizon), solar noon,
 * golden hour, sunset, and night start (astronomical twilight).
 */
object AstralCalculator {

    enum class SolarDayCondition { NORMAL, POLAR_DAY, POLAR_NIGHT }

    enum class AstronomicalNightCondition { STARTS, ALL_DAY, DOES_NOT_OCCUR }

    data class SolarMilestones(
        val sunriseStartMillis: Long,
        val sunriseEndMillis: Long,
        val goldenHourMorningEnd: Long,
        val solarNoonMillis: Long,
        val goldenHourEveningStart: Long,
        val sunsetStartMillis: Long,
        val sunsetEndMillis: Long,
        val nightStartMillis: Long,
        val solarDayCondition: SolarDayCondition,
        val sunriseOccurs: Boolean,
        val sunsetOccurs: Boolean,
        val morningGoldenHourOccurs: Boolean,
        val eveningGoldenHourOccurs: Boolean,
        val astronomicalNightCondition: AstronomicalNightCondition
    )

    data class SolarPosition(
        val elevationDeg: Double,
        val azimuthDeg: Double,
        val isDaytime: Boolean
    )

    data class AstralTime(
        val hours: Int,
        val minutes: Int,
        val seconds: Int,
        val milliseconds: Int,
        val microseconds: Int,
        val nanoseconds: Int,
        val totalSecondsFromSunrise: Double,
        val progressPercentOfDay: Float,
        val currentMilestone: String,
        val nextMilestone: String,
        val timeUntilNextMilestone: String
    )

    /**
     * Compute astronomical solar milestones for a given latitude, longitude, and day timestamp.
     */
    fun computeMilestones(latitude: Double, longitude: Double, timestampMillis: Long, timeZone: TimeZone = TimeZone.getDefault()): SolarMilestones {
        require(latitude.isFinite() && latitude in -90.0..90.0) { "Latitude must be between -90 and 90 degrees." }
        require(longitude.isFinite() && longitude in -180.0..180.0) { "Longitude must be between -180 and 180 degrees." }

        val cal = Calendar.getInstance(timeZone).apply { timeInMillis = timestampMillis }
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val daysInYear = if (cal.getActualMaximum(Calendar.DAY_OF_YEAR) == 366) 366.0 else 365.0

        // Day start in UTC
        val midnightCal = Calendar.getInstance(timeZone).apply {
            timeInMillis = timestampMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val midnightMillis = midnightCal.timeInMillis
        val tzOffsetHours = timeZone.getOffset(timestampMillis) / 3600000.0

        // Fractional year in radians
        // NOAA fractional-year angle evaluated at local solar noon.
        val gamma = 2.0 * Math.PI / daysInYear * (dayOfYear - 1)

        // Equation of time in minutes
        val eqtime = 229.18 * (0.000075 + 0.001868 * cos(gamma) - 0.032077 * sin(gamma) -
                0.014615 * cos(2 * gamma) - 0.040849 * sin(2 * gamma))

        // Solar declination in radians
        val decl = 0.006918 - 0.399912 * cos(gamma) + 0.070257 * sin(gamma) -
                0.006758 * cos(2 * gamma) + 0.000907 * sin(2 * gamma) -
                0.002697 * cos(3 * gamma) + 0.00148 * sin(3 * gamma)

        val latRad = Math.toRadians(latitude)

        // Solar Noon in minutes from midnight UTC
        val solarNoonUtcMinutes = 720.0 - 4.0 * longitude - eqtime
        val solarNoonLocalMinutes = solarNoonUtcMinutes + tzOffsetHours * 60.0
        val solarNoonMillis = midnightMillis + (solarNoonLocalMinutes * 60.0 * 1000.0).toLong()

        fun cosineHourAngle(zenithDeg: Double): Double {
            val zenithRad = Math.toRadians(zenithDeg)
            return (cos(zenithRad) - sin(latRad) * sin(decl)) / (cos(latRad) * cos(decl))
        }

        fun getTimeForZenith(zenithDeg: Double, isRise: Boolean): Long? {
            val cosHa = cosineHourAngle(zenithDeg)
            if (!cosHa.isFinite() || cosHa !in -1.0..1.0) return null

            val hourAngleDeg = Math.toDegrees(acos(cosHa))
            val deltaMinutes = 4.0 * hourAngleDeg
            val localMinutes = if (isRise) {
                solarNoonLocalMinutes - deltaMinutes
            } else {
                solarNoonLocalMinutes + deltaMinutes
            }
            return midnightMillis + (localMinutes * 60_000.0).toLong()
        }

        val sunriseStartEvent = getTimeForZenith(90.833, true)
        val sunriseEndEvent = getTimeForZenith(90.25, true)
        val morningGoldenHourEvent = getTimeForZenith(84.0, true)
        val eveningGoldenHourEvent = getTimeForZenith(84.0, false)
        val sunsetStartEvent = getTimeForZenith(90.25, false)
        val sunsetEndEvent = getTimeForZenith(90.833, false)
        val nightStartEvent = getTimeForZenith(108.0, false)

        val sunriseCosHa = cosineHourAngle(90.833)
        val solarDayCondition = when {
            sunriseCosHa > 1.0 -> SolarDayCondition.POLAR_NIGHT
            sunriseCosHa < -1.0 -> SolarDayCondition.POLAR_DAY
            else -> SolarDayCondition.NORMAL
        }
        val nightCosHa = cosineHourAngle(108.0)
        val astronomicalNightCondition = when {
            nightCosHa > 1.0 -> AstronomicalNightCondition.ALL_DAY
            nightCosHa < -1.0 -> AstronomicalNightCondition.DOES_NOT_OCCUR
            else -> AstronomicalNightCondition.STARTS
        }

        // Keep the legacy timestamp fields populated for existing consumers. Availability
        // flags prevent the fallback solar-noon value from being presented as an event.
        return SolarMilestones(
            sunriseStartMillis = sunriseStartEvent ?: solarNoonMillis,
            sunriseEndMillis = sunriseEndEvent ?: solarNoonMillis,
            goldenHourMorningEnd = morningGoldenHourEvent ?: solarNoonMillis,
            solarNoonMillis = solarNoonMillis,
            goldenHourEveningStart = eveningGoldenHourEvent ?: solarNoonMillis,
            sunsetStartMillis = sunsetStartEvent ?: solarNoonMillis,
            sunsetEndMillis = sunsetEndEvent ?: solarNoonMillis,
            nightStartMillis = nightStartEvent ?: solarNoonMillis,
            solarDayCondition = solarDayCondition,
            sunriseOccurs = sunriseStartEvent != null && sunriseEndEvent != null,
            sunsetOccurs = sunsetStartEvent != null && sunsetEndEvent != null,
            morningGoldenHourOccurs = morningGoldenHourEvent != null,
            eveningGoldenHourOccurs = eveningGoldenHourEvent != null,
            astronomicalNightCondition = astronomicalNightCondition
        )
    }

    /**
     * Compute Astral Time where 00:00:00 is the estimated sunrise.
     */
    @Suppress("UNUSED_PARAMETER")
    fun computeAstralTime(
        currentTimeMillis: Long,
        nanoOffset: Long,
        milestones: SolarMilestones
    ): AstralTime {
        val fullDayMillis = 24L * 60L * 60L * 1000L

        // floorMod handles timestamps before sunrise without an unbounded loop.
        val diffMillis = Math.floorMod(currentTimeMillis - milestones.sunriseStartMillis, fullDayMillis)

        val totalSeconds = diffMillis / 1000.0
        val totalSecInt = diffMillis / 1000
        val hours = (totalSecInt / 3600).toInt()
        val minutes = ((totalSecInt % 3600) / 60).toInt()
        val seconds = (totalSecInt % 60).toInt()
        val millis = (diffMillis % 1000).toInt()

        // System.nanoTime() has an arbitrary monotonic origin and cannot provide
        // the sub-millisecond fraction of currentTimeMillis.
        val micros = 0
        val nanos = 0

        val progress = (diffMillis.toFloat() / fullDayMillis.toFloat()).coerceIn(0f, 1f)

        // Identify current solar milestone stage
        val (currentMilestone, nextMilestone, timeUntil) = determineMilestonePhase(currentTimeMillis, milestones)

        return AstralTime(
            hours = hours,
            minutes = minutes,
            seconds = seconds,
            milliseconds = millis,
            microseconds = micros,
            nanoseconds = nanos,
            totalSecondsFromSunrise = totalSeconds,
            progressPercentOfDay = progress,
            currentMilestone = currentMilestone,
            nextMilestone = nextMilestone,
            timeUntilNextMilestone = timeUntil
        )
    }

    private fun determineMilestonePhase(currentMillis: Long, m: SolarMilestones): Triple<String, String, String> {
        when (m.solarDayCondition) {
            SolarDayCondition.POLAR_DAY ->
                return Triple("Continuous Daylight (Midnight Sun)", "Sunset does not occur", "—")
            SolarDayCondition.POLAR_NIGHT ->
                return Triple("Continuous Polar Night", "Sunrise does not occur", "—")
            SolarDayCondition.NORMAL -> Unit
        }

        val events = buildList {
            if (m.sunriseOccurs) {
                add(m.sunriseStartMillis to "Sunrise Starts (00:00)")
                add(m.sunriseEndMillis to "Sunrise Ends (Full Disk)")
            }
            if (m.morningGoldenHourOccurs) add(m.goldenHourMorningEnd to "Morning Golden Hour Ends")
            add(m.solarNoonMillis to "Solar Noon (Highest Position)")
            if (m.eveningGoldenHourOccurs) add(m.goldenHourEveningStart to "Evening Golden Hour Starts")
            if (m.sunsetOccurs) {
                add(m.sunsetStartMillis to "Sunset Starts (Horizon Touch)")
                add(m.sunsetEndMillis to "Sunset Ends (Disappeared)")
            }
            if (m.astronomicalNightCondition == AstronomicalNightCondition.STARTS) {
                add(m.nightStartMillis to "Astronomical Night Starts")
            }
        }.sortedBy { it.first }

        var current = "Night Phase"
        var next = events.first().second
        var nextTime = events.first().first

        for (i in events.indices) {
            if (currentMillis >= events[i].first) {
                current = events[i].second
                if (i + 1 < events.size) {
                    next = events[i + 1].second
                    nextTime = events[i + 1].first
                } else {
                    next = "Next Sunrise (00:00)"
                    nextTime = events.first().first + 24L * 60L * 60L * 1000L
                }
            }
        }

        val remaining = (nextTime - currentMillis).coerceAtLeast(0L)
        val remHours = remaining / 3_600_000L
        val remMinutes = (remaining % 3_600_000L) / 60_000L
        val remSecs = (remaining % 60_000L) / 1_000L
        return Triple(current, next, String.format("%02d:%02d:%02d", remHours, remMinutes, remSecs))
    }

    /**
     * Compute current Solar Elevation and Azimuth for celestial compass and dial.
     */
    fun computeSolarPosition(latitude: Double, longitude: Double, timestampMillis: Long): SolarPosition {
        require(latitude.isFinite() && latitude in -90.0..90.0) { "Latitude must be between -90 and 90 degrees." }
        require(longitude.isFinite() && longitude in -180.0..180.0) { "Longitude must be between -180 and 180 degrees." }

        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = timestampMillis }
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val daysInYear = if (cal.getActualMaximum(Calendar.DAY_OF_YEAR) == 366) 366.0 else 365.0
        val hour = cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) / 60.0 +
                cal.get(Calendar.SECOND) / 3600.0 + cal.get(Calendar.MILLISECOND) / 3_600_000.0

        val gamma = 2.0 * Math.PI / daysInYear * (dayOfYear - 1 + (hour - 12.0) / 24.0)

        val eqtime = 229.18 * (0.000075 + 0.001868 * cos(gamma) - 0.032077 * sin(gamma) -
                0.014615 * cos(2 * gamma) - 0.040849 * sin(2 * gamma))

        val decl = 0.006918 - 0.399912 * cos(gamma) + 0.070257 * sin(gamma) -
                0.006758 * cos(2 * gamma) + 0.000907 * sin(2 * gamma) -
                0.002697 * cos(3 * gamma) + 0.00148 * sin(3 * gamma)

        val timeOffset = eqtime + 4.0 * longitude
        val trueSolarTime = ((hour * 60.0 + timeOffset) % 1440.0 + 1440.0) % 1440.0
        val haDeg = trueSolarTime / 4.0 - 180.0
        val haRad = Math.toRadians(haDeg)
        val latRad = Math.toRadians(latitude)

        val cosZenith = sin(latRad) * sin(decl) + cos(latRad) * cos(decl) * cos(haRad)
        val zenithRad = acos(cosZenith.coerceIn(-1.0, 1.0))
        val elevationDeg = 90.0 - Math.toDegrees(zenithRad)

        // atan2 preserves the correct quadrant and remains stable near the horizon.
        // At a geographic pole the azimuth is undefined; return a neutral bearing.
        val azimuthDeg = if (abs(cos(latRad) * sin(zenithRad)) < 1e-12) {
            0.0
        } else {
            val azimuth = Math.toDegrees(
                atan2(
                    sin(haRad),
                    cos(haRad) * sin(latRad) - tan(decl) * cos(latRad)
                )
            ) + 180.0
            ((azimuth % 360.0) + 360.0) % 360.0
        }

        return SolarPosition(
            elevationDeg = elevationDeg,
            azimuthDeg = azimuthDeg,
            isDaytime = elevationDeg > -0.833
        )
    }
}
