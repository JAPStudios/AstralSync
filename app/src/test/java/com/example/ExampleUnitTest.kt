package com.example

import com.example.data.*
import org.junit.Assert.*
import org.junit.Test
import java.util.TimeZone

class ExampleUnitTest {
    @Test
    fun testAstralSolarMilestonesWindham() {
        // Windham, NY: 42.345996, -74.246748
        val nyTz = TimeZone.getTimeZone("America/New_York")
        val timestamp = 1682416800000L // April 25, 2023

        val milestones = AstralCalculator.computeMilestones(
            latitude = 42.345996,
            longitude = -74.246748,
            timestampMillis = timestamp,
            timeZone = nyTz
        )

        assertTrue("Sunrise must occur before solar noon", milestones.sunriseStartMillis < milestones.solarNoonMillis)
        assertTrue("Solar noon must occur before sunset", milestones.solarNoonMillis < milestones.sunsetEndMillis)
        assertTrue("Sunset must occur before night starts", milestones.sunsetEndMillis < milestones.nightStartMillis)
    }

    @Test
    fun testAstralTimeZeroAtSunrise() {
        val nyTz = TimeZone.getTimeZone("America/New_York")
        val timestamp = 1682416800000L

        val milestones = AstralCalculator.computeMilestones(
            latitude = 42.345996,
            longitude = -74.246748,
            timestampMillis = timestamp,
            timeZone = nyTz
        )

        // At exact sunrise start, Astral Time must be 00:00:00
        val astralAtRise = AstralCalculator.computeAstralTime(
            currentTimeMillis = milestones.sunriseStartMillis,
            nanoOffset = 0L,
            milestones = milestones
        )

        assertEquals("Hour at sunrise must be 0", 0, astralAtRise.hours)
        assertEquals("Minute at sunrise must be 0", 0, astralAtRise.minutes)
        assertEquals("Second at sunrise must be 0", 0, astralAtRise.seconds)
    }

    @Test
    fun testAstralCalendarEarthAgeAndMonths() {
        val timestamp = 1682416800000L // 2023
        val astralDate = AstralCalendarEngine.calculateAstralDate(timestamp, isSouthernHemisphere = false)

        assertTrue("Earth age must be ~4.54 billion years", astralDate.earthAgeYears > 4.5e9)
        assertEquals("There are 12 months in the sequence", 12, AstralCalendarEngine.ASTRAL_MONTHS_12.size)
        assertEquals("First month is April", "April", AstralCalendarEngine.ASTRAL_MONTHS_12[0].name)
        assertEquals("Last month is January", "January", AstralCalendarEngine.ASTRAL_MONTHS_12[11].name)
    }
    @Test
    fun testSolarPositionIsFiniteAndUsesCompassBearingRange() {
        val positions = listOf(
            AstralCalculator.computeSolarPosition(42.345996, -74.246748, 1682416800000L),
            AstralCalculator.computeSolarPosition(0.0, 0.0, 1704067200000L),
            AstralCalculator.computeSolarPosition(90.0, 0.0, 1704067200000L),
            AstralCalculator.computeSolarPosition(-90.0, 0.0, 1704067200000L)
        )
        positions.forEach { position ->
            assertTrue(position.elevationDeg.isFinite())
            assertTrue(position.azimuthDeg.isFinite())
            assertTrue(position.azimuthDeg >= 0.0 && position.azimuthDeg < 360.0)
        }
    }

    @Test
    fun testSolarCalculationsRejectInvalidCoordinates() {
        assertThrows(IllegalArgumentException::class.java) {
            AstralCalculator.computeSolarPosition(91.0, 0.0, 0L)
        }
        assertThrows(IllegalArgumentException::class.java) {
            AstralCalculator.computeMilestones(0.0, 181.0, 0L)
        }
    }

    @Test
    fun testAstralTimeBeforeSunriseWrapsWithinOneDay() {
        val milestones = AstralCalculator.computeMilestones(
            latitude = 42.345996,
            longitude = -74.246748,
            timestampMillis = 1682416800000L,
            timeZone = TimeZone.getTimeZone("America/New_York")
        )
        val beforeSunrise = AstralCalculator.computeAstralTime(
            currentTimeMillis = milestones.sunriseStartMillis - 1L,
            nanoOffset = 987654321L,
            milestones = milestones
        )
        assertEquals(23, beforeSunrise.hours)
        assertEquals(59, beforeSunrise.minutes)
        assertEquals(59, beforeSunrise.seconds)
        assertEquals(999, beforeSunrise.milliseconds)
        assertEquals(0, beforeSunrise.microseconds)
        assertEquals(0, beforeSunrise.nanoseconds)
    }

    @Test
    fun testCalendarHandlesLeapDayAndYearEndWithinTwelveMonthSequence() {
        val utc = TimeZone.getTimeZone("UTC")
        val leapDay = java.util.Calendar.getInstance(utc).apply {
            clear()
            set(2024, java.util.Calendar.FEBRUARY, 29, 12, 0, 0)
        }.timeInMillis
        val yearEnd = java.util.Calendar.getInstance(utc).apply {
            clear()
            set(2024, java.util.Calendar.DECEMBER, 31, 23, 59, 59)
        }.timeInMillis
        listOf(leapDay, yearEnd).forEach { timestamp ->
            val result = AstralCalendarEngine.calculateAstralDate(timestamp)
            assertTrue(result.monthOrder in 1..12)
            assertTrue(result.dayInMonth >= 1.0)
            assertTrue(result.yearProgressPercent in 0f..1f)
        }
    }

    @Test
    fun testQuantumChronometryUsesNonOverflowingFractionalConversions() {
        val result = AstralCalendarEngine.computeQuantumChronometry(12.345678901, 987654321L)
        assertEquals(345L, result.milliseconds)
        assertEquals(678L, result.microseconds)
        assertEquals(901L, result.nanoseconds)
        assertEquals("000", result.picoseconds)
        assertEquals("000", result.femtoseconds)
        assertEquals("000", result.attoseconds)
    }

    @Test
    fun testSolarNoonFallsNearLocalMidday() {
        val timeZone = TimeZone.getTimeZone("America/New_York")
        val milestones = AstralCalculator.computeMilestones(
            latitude = 42.345996,
            longitude = -74.246748,
            timestampMillis = 1682416800000L,
            timeZone = timeZone
        )
        val localNoon = java.util.Calendar.getInstance(timeZone).apply {
            timeInMillis = milestones.solarNoonMillis
        }
        assertEquals(12, localNoon.get(java.util.Calendar.HOUR_OF_DAY))
    }

}
