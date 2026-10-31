package com.example.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.TimeZone

data class AstralUiState(
    val currentCoordinate: LocationCoordinate = LocationPresets.WINDHAM_NY,
    val isGpsActive: Boolean = false,
    val isSouthernHemisphere: Boolean = false,
    val isHighPrecisionQuantum: Boolean = true,
    val currentMillis: Long = System.currentTimeMillis(),
    val nanoOffset: Long = System.nanoTime(),
    val milestones: AstralCalculator.SolarMilestones = AstralCalculator.computeMilestones(
        latitude = LocationPresets.WINDHAM_NY.latitude,
        longitude = LocationPresets.WINDHAM_NY.longitude,
        timestampMillis = System.currentTimeMillis(),
        timeZone = TimeZone.getTimeZone(LocationPresets.WINDHAM_NY.timeZoneId)
    ),
    val astralTime: AstralCalculator.AstralTime = AstralCalculator.computeAstralTime(
        currentTimeMillis = System.currentTimeMillis(),
        nanoOffset = System.nanoTime(),
        milestones = AstralCalculator.computeMilestones(
            latitude = LocationPresets.WINDHAM_NY.latitude,
            longitude = LocationPresets.WINDHAM_NY.longitude,
            timestampMillis = System.currentTimeMillis(),
            timeZone = TimeZone.getTimeZone(LocationPresets.WINDHAM_NY.timeZoneId)
        )
    ),
    val solarPosition: AstralCalculator.SolarPosition = AstralCalculator.computeSolarPosition(
        latitude = LocationPresets.WINDHAM_NY.latitude,
        longitude = LocationPresets.WINDHAM_NY.longitude,
        timestampMillis = System.currentTimeMillis()
    ),
    val astralDate: AstralCalendarEngine.AstralDateResult = AstralCalendarEngine.calculateAstralDate(
        timestampMillis = System.currentTimeMillis(),
        isSouthernHemisphere = false
    ),
    val quantumChronometry: AstralCalendarEngine.QuantumChronometry = AstralCalendarEngine.computeQuantumChronometry(
        elapsedSeconds = 0.0,
        currentNanos = System.nanoTime()
    ),
    // Calculator State
    val calcYear: Int = 2004,
    val calcMonth: Int = 9,
    val calcDay: Int = 2,
    val calcHour: Int = 2,
    val calcMinute: Int = 18,
    val calcLocation: LocationCoordinate = LocationPresets.WINDHAM_NY,
    val calcResult: AstralDateConversionResult? = null,
    val currentScreen: NavigationScreen = NavigationScreen.CLOCK
)

enum class NavigationScreen(val label: String) {
    CLOCK("Chronometer"),
    DIAL("Solar Dial"),
    CALENDAR("Planetary Calendar"),
    HEALTH("Circadian Suite"),
    CALCULATOR("Converter"),
    STRATEGY("Funding & Legal")
}

data class AstralDateConversionResult(
    val gregorianDateFormatted: String,
    val astralTimeFormatted: String,
    val earthAgeYears: String,
    val newMonthName: String,
    val newMonthMeaning: String,
    val newMonthOrder: Int,
    val dayInMonth: String,
    val seasonName: String,
    val seasonDay: String,
    val weekday: String,
    val zodiacGod: AstralCalendarEngine.ZodiacGodInfo,
    val chineseZodiac: String,
    val mayanSign: String,
    val explanation: String
)

class AstralViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AstralUiState())
    val uiState: StateFlow<AstralUiState> = _uiState.asStateFlow()

    init {
        // Initialize default conversion with user's September 2, 2004 milestone
        loadCalculatorPreset(2004, 9, 2, 2, 18, LocationPresets.WINDHAM_NY)
        startChronometerLoop()
    }

    private fun startChronometerLoop() {
        viewModelScope.launch {
            while (isActive) {
                val now = System.currentTimeMillis()
                val nanos = System.nanoTime()
                val loc = _uiState.value.currentCoordinate
                val tz = TimeZone.getTimeZone(loc.timeZoneId)

                val milestones = AstralCalculator.computeMilestones(
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    timestampMillis = now,
                    timeZone = tz
                )

                val astralTime = AstralCalculator.computeAstralTime(
                    currentTimeMillis = now,
                    nanoOffset = nanos,
                    milestones = milestones
                )

                val solarPos = AstralCalculator.computeSolarPosition(
                    latitude = loc.latitude,
                    longitude = loc.longitude,
                    timestampMillis = now
                )

                val astralDate = AstralCalendarEngine.calculateAstralDate(
                    timestampMillis = now,
                    isSouthernHemisphere = _uiState.value.isSouthernHemisphere
                )

                val quantum = AstralCalendarEngine.computeQuantumChronometry(
                    elapsedSeconds = astralTime.totalSecondsFromSunrise,
                    currentNanos = nanos
                )

                _uiState.update { state ->
                    state.copy(
                        currentMillis = now,
                        nanoOffset = nanos,
                        milestones = milestones,
                        astralTime = astralTime,
                        solarPosition = solarPos,
                        astralDate = astralDate,
                        quantumChronometry = quantum
                    )
                }

                // Update at ~30 FPS for smooth sub-microsecond and visual pulses
                delay(33L)
            }
        }
    }

    fun setScreen(screen: NavigationScreen) {
        _uiState.update { it.copy(currentScreen = screen) }
    }

    fun setLocation(coordinate: LocationCoordinate) {
        val isSouth = coordinate.latitude < 0
        _uiState.update {
            it.copy(
                currentCoordinate = coordinate,
                isGpsActive = false,
                isSouthernHemisphere = isSouth
            )
        }
    }

    fun setGpsLocation(latitude: Double, longitude: Double, name: String = "GPS Horizon") {
        val coord = LocationCoordinate(
            name = name,
            region = "Device Geolocation",
            latitude = latitude,
            longitude = longitude,
            timeZoneId = TimeZone.getDefault().id
        )
        val isSouth = latitude < 0
        _uiState.update {
            it.copy(
                currentCoordinate = coord,
                isGpsActive = true,
                isSouthernHemisphere = isSouth
            )
        }
    }

    fun toggleHemisphere() {
        _uiState.update { it.copy(isSouthernHemisphere = !it.isSouthernHemisphere) }
    }

    fun toggleQuantumPrecision() {
        _uiState.update { it.copy(isHighPrecisionQuantum = !it.isHighPrecisionQuantum) }
    }

    // --- Calculator Operations ---

    fun updateCalcInputs(year: Int, month: Int, day: Int, hour: Int, minute: Int, loc: LocationCoordinate? = null) {
        _uiState.update {
            it.copy(
                calcYear = year,
                calcMonth = month,
                calcDay = day,
                calcHour = hour,
                calcMinute = minute,
                calcLocation = loc ?: it.calcLocation
            )
        }
        computeCalculation()
    }

    fun loadCalculatorPreset(year: Int, month: Int, day: Int, hour: Int, minute: Int, loc: LocationCoordinate) {
        updateCalcInputs(year, month, day, hour, minute, loc)
    }

    fun computeCalculation() {
        val state = _uiState.value
        val cal = Calendar.getInstance(TimeZone.getTimeZone(state.calcLocation.timeZoneId)).apply {
            set(Calendar.YEAR, state.calcYear)
            set(Calendar.MONTH, state.calcMonth - 1)
            set(Calendar.DAY_OF_MONTH, state.calcDay)
            set(Calendar.HOUR_OF_DAY, state.calcHour)
            set(Calendar.MINUTE, state.calcMinute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val targetMillis = cal.timeInMillis

        val milestones = AstralCalculator.computeMilestones(
            latitude = state.calcLocation.latitude,
            longitude = state.calcLocation.longitude,
            timestampMillis = targetMillis,
            timeZone = TimeZone.getTimeZone(state.calcLocation.timeZoneId)
        )

        val astralTime = AstralCalculator.computeAstralTime(
            currentTimeMillis = targetMillis,
            nanoOffset = 0L,
            milestones = milestones
        )

        val astralDate = AstralCalendarEngine.calculateAstralDate(
            timestampMillis = targetMillis,
            isSouthernHemisphere = state.calcLocation.latitude < 0
        )

        val sunriseCal = Calendar.getInstance(TimeZone.getTimeZone(state.calcLocation.timeZoneId)).apply {
            timeInMillis = milestones.sunriseStartMillis
        }
        val sunriseStr = String.format("%02d:%02d", sunriseCal.get(Calendar.HOUR_OF_DAY), sunriseCal.get(Calendar.MINUTE))

        val gFmt = String.format("%04d-%02d-%02d %02d:%02d", state.calcYear, state.calcMonth, state.calcDay, state.calcHour, state.calcMinute)
        val aTimeFmt = String.format("%02d:%02d:%02d Astral", astralTime.hours, astralTime.minutes, astralTime.seconds)

        val explanation = buildString {
            append("1. Local sunrise at ${state.calcLocation.name} was at $sunriseStr standard time (00:00 Astral Time).\n")
            append("2. Elapsed time from sunrise to target time: ${astralTime.hours}h ${astralTime.minutes}m (${astralTime.totalSecondsFromSunrise.toInt()} seconds).\n")
            append("3. Earth Age Year: ~4.54 Billion Years (${astralDate.earthAgeFormatted}).\n")
            append("4. Planetary Month: Month ${astralDate.monthOrder} (${astralDate.monthName} - ${astralDate.monthMeaning}), Day ${astralDate.dayFormatted}.\n")
            append("5. Astronomical Season: ${astralDate.seasonName} (${String.format("%.1f", astralDate.seasonDay)} / 91.31 days).\n")
            append("6. Archetype: ${astralDate.zodiacGod.deity} (${astralDate.zodiacGod.sign}) | Chinese Zodiac: ${astralDate.chineseZodiac} | Mayan Tzolk'in: ${astralDate.mayanSign}.")
        }

        val result = AstralDateConversionResult(
            gregorianDateFormatted = gFmt,
            astralTimeFormatted = aTimeFmt,
            earthAgeYears = astralDate.earthAgeFormatted,
            newMonthName = astralDate.monthName,
            newMonthMeaning = astralDate.monthMeaning,
            newMonthOrder = astralDate.monthOrder,
            dayInMonth = astralDate.dayFormatted,
            seasonName = astralDate.seasonName,
            seasonDay = String.format("%.2f", astralDate.seasonDay),
            weekday = astralDate.weekdayName,
            zodiacGod = astralDate.zodiacGod,
            chineseZodiac = astralDate.chineseZodiac,
            mayanSign = astralDate.mayanSign,
            explanation = explanation
        )

        _uiState.update { it.copy(calcResult = result) }
    }
}
