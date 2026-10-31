package com.example.data

import java.util.Calendar
import java.util.Date
import java.util.TimeZone

/**
 * Planetary Astral Calendar Engine implementing:
 * - 30.4375 days per month (12-month model) and 28.096 days (13-month lunar model)
 * - True Earth Age Epoch (4.54 billion years from planetary birth)
 * - New System Month Order: April, June, May, February, Quintilis, Sextillia, September, October, November, December, Undecimus/March, January
 * - 4 Solar Seasons: Lent (Spring forth), Sem (Together as ume), Autumnus (Harvest), Winter (Time of water)
 * - Mythological Zodiac Gods, Chinese Zodiac animals, and Mayan Tzolk'in Day Signs
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

    val MAYAN_TZOLKIN_SIGNS = listOf(
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
     * Converts a given timestamp (or Gregorian date) into the AstralSync New System Date.
     */
    fun calculateAstralDate(timestampMillis: Long, isSouthernHemisphere: Boolean = false): AstralDateResult {
        val cal = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply { timeInMillis = timestampMillis }
        val gYear = cal.get(Calendar.YEAR)
        val dayOfYear = cal.get(Calendar.DAY_OF_YEAR)
        val hourOfDay = cal.get(Calendar.HOUR_OF_DAY)
        val minute = cal.get(Calendar.MINUTE)
        val second = cal.get(Calendar.SECOND)

        val fractionalDay = (hourOfDay * 3600.0 + minute * 60.0 + second) / 86400.0
        val totalDaysInYear = (dayOfYear - 1) + fractionalDay

        // Year length = 365.25 days
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

        // Mayan Tzolk'in Sign (20-day cycle)
        // Correlated: Day count mod 20
        val totalEpochDays = (gYear * 365.25 + totalDaysInYear).toLong()
        val mayanIndex = Math.floorMod(totalEpochDays, 20).toInt()
        val mayanSign = MAYAN_TZOLKIN_SIGNS[mayanIndex]

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
     * Compute deep quantum chronometry down to Planck time for a given elapsed second duration.
     */
    fun computeQuantumChronometry(elapsedSeconds: Double, currentNanos: Long): QuantumChronometry {
        val totalMillis = (elapsedSeconds * 1000.0).toLong()
        val totalMicros = (elapsedSeconds * 1_000_000.0).toLong()
        val totalNanos = (elapsedSeconds * 1_000_000_000.0).toLong()

        val subMicro = (currentNanos % 1_000_000L) / 1000L // 0..999 microseconds

        // Triggers from user specification:
        // Eye Blink: 350,000 µs (~350 ms)
        // Finger Snap: 150,000 µs (~150 ms)
        // Camera Flash: 1,000 µs (1 ms)
        // Camera Shutter: 4,000 µs (4 ms)
        val trigger = when {
            totalMicros % 350_000L in 0..5000 -> "👁️ Eye Blink Quantum Window (~350,000 µs)"
            totalMicros % 150_000L in 0..5000 -> "🫰 Finger Snap Quantum Window (~150,000 µs)"
            totalMicros % 4_000L in 0..500 -> "📸 Camera Shutter Open (~4,000 µs)"
            totalMicros % 1_000L in 0..200 -> "⚡ Camera Flash Pulse (~1,000 µs)"
            else -> null
        }

        // Sub-atomic units using scientific formatting
        val picos = (totalNanos % 1_000_000_000L) * 1_000L + (currentNanos % 1000L)
        val femtos = picos * 1000L
        val attos = femtos * 1000L

        // Yoctoseconds: 10^-24 s, Planck time: 5.39 x 10^-44 s
        val yoctoExp = String.format("%.3e ys", elapsedSeconds * 1e24)
        val zeptoExp = String.format("%.3e zs", elapsedSeconds * 1e21)
        val planckExp = String.format("%.2e tP", elapsedSeconds / 5.39e-44)

        return QuantumChronometry(
            seconds = elapsedSeconds,
            milliseconds = totalMillis % 1000,
            microseconds = totalMicros % 1000,
            nanoseconds = totalNanos % 1000,
            picoseconds = String.format("%03d", picos % 1000),
            femtoseconds = String.format("%03d", femtos % 1000),
            attoseconds = String.format("%03d", attos % 1000),
            zeptoseconds = zeptoExp,
            yoctoseconds = yoctoExp,
            planckTimes = planckExp,
            activeBiologicalTrigger = trigger
        )
    }
}
