package com.example.data

import java.math.BigDecimal
import java.math.BigInteger
import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Planetary Astral Calendar Engine implementing:
 * - Twelve equal-duration months of 30.4375 days each
 * - True Earth Age Epoch (4.54 billion years from planetary birth)
 * - New System Month Order: April, June, May, February, Quintilis, Sextillia, September, October, November, December, Undecimus/March, January
 * - Four equal-duration calendar seasons, with northern and southern ordering
 * - Astral archetypes, Gregorian-year Chinese zodiac approximations, and a 20-day name cycle
 * - Deep quantum chronometry down to Planck time
 */
object AstralCalendarEngine {

    val ASTRAL_MONTHS_12 = listOf(
        AstralMonthInfo("April", "Opening buds of spring", 1),
        AstralMonthInfo("June", "Juno, goddess protector of marriage & children", 2),
        AstralMonthInfo("May", "Maia, goddess of spring growth", 3),
        AstralMonthInfo("February", "Februa, the purification festival", 4),
        AstralMonthInfo("Quintilis", "The fifth cosmic month", 5),
        AstralMonthInfo("Sextillia", "The sixth cosmic month", 6),
        AstralMonthInfo("September", "The seventh cosmic month", 7),
        AstralMonthInfo("October", "The eighth cosmic month", 8),
        AstralMonthInfo("November", "The ninth cosmic month", 9),
        AstralMonthInfo("December", "The tenth cosmic month", 10),
        AstralMonthInfo("March", "Mars, the god of courage & war", 11),
        AstralMonthInfo("January", "Janus, god of dual doorways looking to past & future", 12)
    )

    val SEASONS_NORTHERN = listOf(
        SeasonInfo("Lent", "Spring forth - germination and daylight expansion", 91.3125),
        SeasonInfo("Sem", "Together as ume - solar zenith and communal flourishing", 91.3125),
        SeasonInfo("Autumnus", "Fall season of harvest and golden hues", 91.3125),
        SeasonInfo("Winter", "Time of water, cosmic rest and renewal", 91.3125)
    )

    val SEASONS_SOUTHERN = listOf(
        SeasonInfo("Winter", "Time of water, cosmic rest and renewal", 91.3125),
        SeasonInfo("Lent", "Spring forth - germination and daylight expansion", 91.3125),
        SeasonInfo("Sem", "Together as ume - solar zenith and communal flourishing", 91.3125),
        SeasonInfo("Autumnus", "Fall season of harvest and golden hues", 91.3125)
    )

    val ZODIAC_GODS = listOf(
        ZodiacGodInfo("Aries", "Ares", "Greek Olympian god of vitality, drive, and courageous action (Roman: Mars)."),
        ZodiacGodInfo("Taurus", "Zeus & Aphrodite", "Europa myth & Aphrodite: beauty, terrestrial grounding, and fertility."),
        ZodiacGodInfo("Gemini", "Loki & Þjazi", "Norse myth: celestial duality, wit, and the starry eyes of Þjazi (augu Þjaza)."),
        ZodiacGodInfo("Cancer", "Artemis", "Goddess of the Moon, radiant empathy, night healing, and wild nature."),
        ZodiacGodInfo("Leo", "The Golden Lion", "Sun deity & King of the Zodiac Pantheon, radiant solar vitality."),
        ZodiacGodInfo("Virgo", "Athena (Minerva)", "Goddess of wisdom, handicraft, civilized practical reason, and harvest."),
        ZodiacGodInfo("Libra", "Themis & Astraea", "Goddess of Justice and Cosmic Law, balancing celestial scales."),
        ZodiacGodInfo("Scorpio", "Lisin (Negun)", "Sumerian goddess identified with Alpha Scorpii, the Heart of the Scorpion."),
        ZodiacGodInfo("Sagittarius", "Nergal", "Babylonian winged celestial archer centaur, wisdom and relentless expansion."),
        ZodiacGodInfo("Capricorn", "Pan", "God of woodlands, pastoral nature, mountains, and earthy resilience."),
        ZodiacGodInfo("Aquarius", "Lord Shiva", "Supreme cosmic consciousness and universal river of life bearer."),
        ZodiacGodInfo("Pisces", "Neptune", "Sovereign deity of boundless oceans, intuitive depth, and cosmic waters.")
    )

    val CHINESE_ANIMALS = listOf(
        "Rat", "Ox", "Tiger", "Rabbit", "Dragon", "Snake",
        "Horse", "Goat", "Monkey", "Rooster", "Dog", "Pig"
    )

    val MAYAN_DAY_NAMES = listOf(
        "Crocodile (Imix)", "Wind (Ik')", "House (Ak'b'al)", "Lizard (K'an)", "Serpent (Chikchan)",
        "Death (Kimi)", "Deer (Manik')", "Rabbit (Lamat)", "Water (Muluk)", "Dog (Ok)",
        "Monkey (Chuwen)", "Grass (Eb')", "Reed (B'en)", "Jaguar (Ix)", "Eagle (Men)",
        "Vulture (Kib')", "Earth (Kab'an)", "Knife (Etz'nab')", "Storm (Kawak)", "Flower (Ajaw)"
    )

    data class AstralMonthInfo(val name: String, val meaning: String, val order: Int)
    data class SeasonInfo(val name: String, val description: String, val durationDays: Double)
    data class ZodiacGodInfo(val sign: String, val deity: String, val description: String)

    data class AstralDateResult(
        val earthAgeYears: Double,
        val earthAgeFormatted: String,
        val monthName: String,
        val monthMeaning: String,
        val monthOrder: Int,
        val dayInMonth: Double,
        val dayFormatted: String,
        val seasonName: String,
        val seasonDay: Double,
        val seasonProgressPercent: Float,
        val weekdayName: String,
        val weekdayEtymology: String,
        val zodiacGod: ZodiacGodInfo,
        val chineseZodiac: String,
        val mayanSign: String,
        val yearProgressPercent: Float
    )

    data class QuantumChronometry(
        val seconds: Double,
        val milliseconds: Long,
        val microseconds: Long,
        val nanoseconds: Long,
        val picoseconds: String,
        val femtoseconds: String,
        val attoseconds: String,
        val zeptoseconds: String,
        val yoctoseconds: String,
        val planckTimes: String,
        val activeBiologicalTrigger: String?
    )

    /**
     * Validates a proleptic Gregorian date without silently normalizing invalid days.
     */
    fun isValidGregorianDate(year: Int, month: Int, day: Int): Boolean {
        if (year !in 1..9999 || month !in 1..12) return false
        val daysInMonth = when (month) {
            2 -> if (year % 400 == 0 || (year % 4 == 0 && year % 100 != 0)) 29 else 28
            4, 6, 9, 11 -> 30
            else -> 31
        }
        return day in 1..daysInMonth
    }

    /**
     * Converts a given timestamp (or Gregorian date) into the AstralSync New System Date.
     */
    fun calculateAstralDate(timestampMillis: Long, isSouthernHemisphere: Boolean = false): AstralDateResult {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = timestampMillis }
        val gYear = cal.get(Calendar.YEAR)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val daysInGregorianYear = cal.getActualMaximum(Calendar.DAY_OF_YEAR)
        val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val second = cal.get(Calendar.SECOND)

        val fractionalDay = (hourOfDay * 3600.0 + minute * 60.0 + second) / 86400.0
        val elapsedGregorianDays = (dayOfYear - 1) + fractionalDay

        // Map each Gregorian year onto the Astral calendar's fixed 365.25-day year.
        // This prevents leap years from overflowing the twelve 30.4375-day months.
        val totalDaysInYear = elapsedGregorianDays * (365.25 / daysInGregorianYear)

        val yearProgress = (totalDaysInYear / 365.25).toFloat().coerceIn(0f, 1f)

        // Earth Age: ~4.54 billion years + Gregorian offset
        val earthBaseYears = 4_540_000_000.0
        val earthAge = earthBaseYears + (gYear - 2000) + yearProgress

        // Astral Month: 30.4375 days per month across 12 months
        val daysPerMonth = 30.4375
        val monthIndex = ((totalDaysInYear / daysPerMonth).toInt() % 12).coerceIn(0, 11)
        val dayInMonth = (totalDaysInYear % daysPerMonth) + 1.0

        val monthInfo = ASTRAL_MONTHS_12[monthIndex]

        // Seasons: 91.3125 days per season
        val seasonList = if (isSouthernHemisphere) SEASONS_SOUTHERN else SEASONS_NORTHERN
        val seasonIndex = ((totalDaysInYear / 91.3125).toInt() % 4).coerceIn(0, 3)
        val dayInSeason = totalDaysInYear % 91.3125
        val seasonProgress = (dayInSeason / 91.3125).toFloat().coerceIn(0f, 1f)
        val currentSeason = seasonList[seasonIndex]

        // Weekday
        val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
        val (weekdayName, weekdayEtym) = getWeekdayInfo(dayOfWeek)

        // Zodiac God
        val zodiacIndex = ((totalDaysInYear / (365.25 / 12.0)).toInt() % 12).coerceIn(0, 11)
        val zodiac = ZODIAC_GODS[zodiacIndex]

        // Chinese Zodiac (Cycle of 12 years: 2008 was Rat, 2004 was Monkey, 2024 was Dragon, etc.)
        val chineseIndex = Math.floorMod(gYear - 4, 12)
        val chineseSign = CHINESE_ANIMALS[chineseIndex]

        // A 20-day name rotation anchored to 0001-01-01 in the proleptic
        // Gregorian calendar. This is not a full GMT-correlated Tzolk'in date.
        val yearsBeforeCurrent = gYear.toLong() - 1L
        val daysBeforeCurrentYear = 365L * yearsBeforeCurrent +
            yearsBeforeCurrent / 4L - yearsBeforeCurrent / 100L + yearsBeforeCurrent / 400L
        val absoluteGregorianDay = daysBeforeCurrentYear + dayOfYear.toLong() - 1L
        val mayanIndex = Math.floorMod(absoluteGregorianDay, 20L).toInt()
        val mayanSign = MAYAN_DAY_NAMES[mayanIndex]

        val earthAgeFmt = String.format("%.4f Billion Years", earthAge / 1_000_000_000.0)
        val dayFmt = String.format("%.2f", dayInMonth)

        return AstralDateResult(
            earthAgeYears = earthAge,
            earthAgeFormatted = earthAgeFmt,
            monthName = monthInfo.name,
            monthMeaning = monthInfo.meaning,
            monthOrder = monthInfo.order,
            dayInMonth = dayInMonth,
            dayFormatted = dayFmt,
            seasonName = currentSeason.name,
            seasonDay = dayInSeason,
            seasonProgressPercent = seasonProgress,
            weekdayName = weekdayName,
            weekdayEtymology = weekdayEtym,
            zodiacGod = zodiac,
            chineseZodiac = chineseSign,
            mayanSign = mayanSign,
            yearProgressPercent = yearProgress
        )
    }

    private fun getWeekdayInfo(calendarDayOfWeek: Int): Pair<String, String> {
        return when (calendarDayOfWeek) {
            Calendar.SUNDAY -> "Sunday" to "Old English Sunnandæg, dies solis - Sun's Day honoring goddess Sunna / Sól."
            Calendar.MONDAY -> "Monday" to "Old English Mōnandæg, dies lunae - Day of the Moon and celestial deity Máni."
            Calendar.TUESDAY -> "Tysdagr (Tuesday)" to "Tīwesdæg / dies Marti - Day of Tīw / Týr, Norse god of justice & bravery."
            Calendar.WEDNESDAY -> "Wednesday" to "Wōdnesdæg / dies Mercurii - Day of Odin / Woden, master of cosmic wisdom."
            Calendar.THURSDAY -> "Thursday" to "Thu(n)resdæg / dies Jovis - Day of Thor / Thunor, god of thunder & protection."
            Calendar.FRIDAY -> "Friday" to "Frīgedæg / dies Veneris - Day of Frigga / Freya, goddess of love & harmony."
            Calendar.SATURDAY -> "Saturday" to "Sætern(es)dæg / dies Saturni - Roman god Saturn, deity of harvest & cycles."
            else -> "Sunday" to "Sun's day"
        }
    }

    /**
     * Convert elapsed seconds into progressively smaller mathematical time scales.
     * Values below nanoseconds are scale conversions, not direct physical measurements.
     */
    @Suppress("UNUSED_PARAMETER")
    fun computeQuantumChronometry(elapsedSeconds: Double, currentNanos: Long): QuantumChronometry {
        require(elapsedSeconds.isFinite() && elapsedSeconds >= 0.0) {
            "Elapsed time must be a finite, non-negative number of seconds."
        }

        val elapsed = BigDecimal.valueOf(elapsedSeconds)
        val fractionalSeconds = elapsed.remainder(BigDecimal.ONE)
        val totalMicros = (elapsedSeconds * 1_000_000.0).toLong()

        // Trigger windows are centered on each period boundary, rather than only
        // recognizing the interval after a boundary.
        fun nearPeriodBoundary(periodMicros: Long, toleranceMicros: Long): Boolean {
            val phase = Math.floorMod(totalMicros, periodMicros)
            return minOf(phase, periodMicros - phase) <= toleranceMicros
        }

        val trigger = when {
            nearPeriodBoundary(350_000L, 5_000L) -> "👁️ Eye Blink Quantum Window (~350,000 µs)"
            nearPeriodBoundary(150_000L, 5_000L) -> "🫰 Finger Snap Quantum Window (~150,000 µs)"
            nearPeriodBoundary(4_000L, 500L) -> "📸 Camera Shutter Open (~4,000 µs)"
            nearPeriodBoundary(1_000L, 200L) -> "⚡ Camera Flash Pulse (~1,000 µs)"
            else -> null
        }

        // These are decimal scale conversions of elapsed time. The monotonic
        // clock's arbitrary origin is not mixed into wall-clock measurements.
        fun fractionalUnit(digits: Int): String = fractionalSeconds
            .movePointRight(digits)
            .toBigInteger()
            .mod(BigInteger.valueOf(1000L))
            .toString()
            .padStart(3, '0')

        val yoctoExp = String.format("%.3e ys", elapsedSeconds * 1e24)
        val zeptoExp = String.format("%.3e zs", elapsedSeconds * 1e21)
        val planckExp = String.format("%.2e tP", elapsedSeconds / 5.39e-44)

        return QuantumChronometry(
            seconds = elapsedSeconds,
            milliseconds = fractionalUnit(3).toLong(),
            microseconds = fractionalUnit(6).toLong(),
            nanoseconds = fractionalUnit(9).toLong(),
            picoseconds = fractionalUnit(12),
            femtoseconds = fractionalUnit(15),
            attoseconds = fractionalUnit(18),
            zeptoseconds = zeptoExp,
            yoctoseconds = yoctoExp,
            planckTimes = planckExp,
            activeBiologicalTrigger = trigger
        )
    }
}
