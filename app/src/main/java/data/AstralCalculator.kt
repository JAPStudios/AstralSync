package com.example.data

import java.util.Calendar
import java.util.TimeZone
import kotlin.math.*

/**
 * High-precision astronomical calculations based on NOAA Solar Position Equations.
 * Calculates exact astronomical sunrise (top edge on horizon), solar noon,
 * golden hour, sunset, and night start (astronomical twilight).
 */
object AstralCalculator {

    data class SolarMilestones(
        val sunriseStartMillis: Long,     // Top edge touches horizon (00:00:00 Astral Time)
        val sunriseEndMillis: Long,       // Bottom edge touches horizon (full disk rise)
        val goldenHourMorningEnd: Long,   // 6 degrees above horizon
        val solarNoonMillis: Long,        // Sun at zenith / highest point
        val goldenHourEveningStart: Long, // 6 degrees above horizon
        val sunsetStartMillis: Long,      // Bottom edge touches horizon
        val sunsetEndMillis: Long,        // Sun completely disappears below horizon
        val nightStartMillis: Long        // Sun 18 degrees below horizon (astronomical twilight)
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
        val cal = Calendar.getInstance(timeZone).apply { timeInMillis = timestampMillis }
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val year = cal.get(Calendar.YEAR)

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
        val gamma = 2.0 * Math.PI / 365.0 * (dayOfYear - 1 + (12.0 - 12.0) / 24.0)

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

        // Helper to get time for zenith angle in degrees
        fun getTimeForZenith(zenithDeg: Double, isRise: Boolean): Long {
            val zenithRad = Math.toRadians(zenithDeg)
            val cosHa = (cos(zenithRad) - sin(latRad) * sin(decl)) / (cos(latRad) * cos(decl))
            if (cosHa > 1.0) {
                // Polar night: never rises
                return solarNoonMillis
            } else if (cosHa < -1.0) {
                // Midnight sun: never sets
                return solarNoonMillis
            }
            val haRad = acos(cosHa)
            val haDeg = Math.toDegrees(haRad)
            val deltaMinutes = 4.0 * haDeg
            val minutes = if (isRise) solarNoonLocalMinutes - deltaMinutes else solarNoonLocalMinutes + deltaMinutes
            return midnightMillis + (minutes * 60.0 * 1000.0).toLong()
        }

        // Zenith angles:
        // Standard sunrise/sunset (top edge on horizon): 90.833° (90°50')
        val sunriseStart = getTimeForZenith(90.833, true)
        // Sunrise ends (bottom edge touches horizon, full disk risen): ~90.25°
        val sunriseEnd = getTimeForZenith(90.25, true)
        // Golden hour morning (sun reaches 6° altitude): 84.0°
        val goldenHourMorning = getTimeForZenith(84.0, true)
        // Golden hour evening (sun begins descent at 6° altitude): 84.0°
        val goldenHourEvening = getTimeForZenith(84.0, false)
        // Sunset starts (bottom edge touches horizon): ~90.25°
        val sunsetStart = getTimeForZenith(90.25, false)
        // Sunset ends (sun disappears completely): 90.833°
        val sunsetEnd = getTimeForZenith(90.833, false)
        // Night starts (astronomical twilight, 18° below horizon): 108.0°
        val nightStart = getTimeForZenith(108.0, false)

        return SolarMilestones(
            sunriseStartMillis = sunriseStart,
            sunriseEndMillis = sunriseEnd,
            goldenHourMorningEnd = goldenHourMorning,
            solarNoonMillis = solarNoonMillis,
            goldenHourEveningStart = goldenHourEvening,
            sunsetStartMillis = sunsetStart,
            sunsetEndMillis = sunsetEnd,
            nightStartMillis = nightStart
        )
    }

    /**
     * Compute Astral Time where 00:00:00 is exact sunrise.
     */
    fun computeAstralTime(
        currentTimeMillis: Long,
        nanoOffset: Long,
        milestones: SolarMilestones
    ): AstralTime {
        // Difference from sunrise
        var diffMillis = currentTimeMillis - milestones.sunriseStartMillis
        val fullDayMillis = 24L * 3600L * 1000L

        // Normalize to positive 24-hour cycle from sunrise
        while (diffMillis < 0) {
            diffMillis += fullDayMillis
        }
        diffMillis %= fullDayMillis

        val totalSeconds = diffMillis / 1000.0
        val totalSecInt = diffMillis / 1000
        val hours = (totalSecInt / 3600).toInt()
        val minutes = ((totalSecInt % 3600) / 60).toInt()
        val seconds = (totalSecInt % 60).toInt()
        val millis = (diffMillis % 1000).toInt()

        val micros = (nanoOffset / 1000 % 1000).toInt()
        val nanos = (nanoOffset % 1000).toInt()

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
        val list = listOf(
            m.sunriseStartMillis to "Sunrise Starts (00:00)",
            m.sunriseEndMillis to "Sunrise Ends (Full Disk)",
            m.goldenHourMorningEnd to "Morning Golden Hour Ends",
            m.solarNoonMillis to "Solar Noon (Zenith)",
            m.goldenHourEveningStart to "Evening Golden Hour Starts",
            m.sunsetStartMillis to "Sunset Starts (Horizon Touch)",
            m.sunsetEndMillis to "Sunset Ends (Disappeared)",
            m.nightStartMillis to "Astronomical Night Starts"
        ).sortedBy { it.first }

        var current = "Night Phase (Astronomical)"
        var next = list.first().second
        var nextTime = list.first().first

        for (i in list.indices) {
            if (currentMillis >= list[i].first) {
                current = list[i].second
                if (i + 1 < list.size) {
                    next = list[i + 1].second
                    nextTime = list[i + 1].first
                } else {
                    next = "Next Sunrise (00:00)"
                    nextTime = list.first().first + 24 * 3600 * 1000
                }
            }
        }

        val remaining = (nextTime - currentMillis).coerceAtLeast(0)
        val remHours = remaining / 3600000
        val remMinutes = (remaining % 3600000) / 60000
        val remSecs = (remaining % 60000) / 1000
        val timeUntilStr = String.format("%02d:%02d:%02d", remHours, remMinutes, remSecs)

        return Triple(current, next, timeUntilStr)
    }

    /**
     * Compute current Solar Elevation and Azimuth for celestial compass and dial.
     */
    fun computeSolarPosition(latitude: Double, longitude: Double, timestampMillis: Long): SolarPosition {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = timestampMillis }
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val hour = cal.get(Calendar.HOUR_OF_DAY) + cal.get(Calendar.MINUTE) / 60.0 + cal.get(Calendar.SECOND) / 3600.0

        val gamma = 2.0 * Math.PI / 365.0 * (dayOfYear - 1 + (hour - 12.0) / 24.0)

        val eqtime = 229.18 * (0.000075 + 0.001868 * cos(gamma) - 0.032077 * sin(gamma) -
                0.014615 * cos(2 * gamma) - 0.040849 * sin(2 * gamma))

        val decl = 0.006918 - 0.399912 * cos(gamma) + 0.070257 * sin(gamma) -
                0.006758 * cos(2 * gamma) + 0.000907 * sin(2 * gamma) -
                0.002697 * cos(3 * gamma) + 0.00148 * sin(3 * gamma)

        val timeOffset = eqtime + 4.0 * longitude
        val trueSolarTime = (hour * 60.0 + timeOffset) % 1440.0
        val haDeg = if (trueSolarTime / 4.0 < 0) trueSolarTime / 4.0 + 180.0 else trueSolarTime / 4.0 - 180.0
        val haRad = Math.toRadians(haDeg)
        val latRad = Math.toRadians(latitude)

        val cosZenith = sin(latRad) * sin(decl) + cos(latRad) * cos(decl) * cos(haRad)
        val zenithRad = acos(cosZenith.coerceIn(-1.0, 1.0))
        val elevationDeg = 90.0 - Math.toDegrees(zenithRad)

        val cosAzimuth = (sin(decl) - sin(latRad) * cos(zenithRad)) / (cos(latRad) * sin(zenithRad))
        var azimuthRad = acos(cosAzimuth.coerceIn(-1.0, 1.0))
        var azimuthDeg = Math.toDegrees(azimuthRad)
        if (haDeg > 0) {
            azimuthDeg = (azimuthDeg + 180.0) % 360.0
        } else {
            azimuthDeg = (540.0 - azimuthDeg) % 360.0
        }

        return SolarPosition(
            elevationDeg = elevationDeg,
            azimuthDeg = azimuthDeg,
            isDaytime = elevationDeg > -0.833
        )
    }
}
