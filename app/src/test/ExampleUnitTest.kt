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
}
