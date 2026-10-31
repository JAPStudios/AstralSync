package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.LocationCoordinate
import com.example.data.LocationPresets
import com.example.ui.AstralUiState
import com.example.ui.theme.*

@Composable
fun CalculatorScreen(
    state: AstralUiState,
    onCalculate: (year: Int, month: Int, day: Int, hour: Int, minute: Int, loc: LocationCoordinate?) -> Unit,
    onLoadPreset: (year: Int, month: Int, day: Int, hour: Int, minute: Int, loc: LocationCoordinate) -> Unit,
    modifier: Modifier = Modifier
) {
    var yearText by remember(state.calcYear) { mutableStateOf(state.calcYear.toString()) }
    var monthText by remember(state.calcMonth) { mutableStateOf(state.calcMonth.toString()) }
    var dayText by remember(state.calcDay) { mutableStateOf(state.calcDay.toString()) }
    var hourText by remember(state.calcHour) { mutableStateOf(state.calcHour.toString()) }
    var minuteText by remember(state.calcMinute) { mutableStateOf(state.calcMinute.toString()) }

    fun triggerCalc() {
        val y = yearText.toIntOrNull() ?: 2004
        val m = (monthText.toIntOrNull() ?: 1).coerceIn(1, 12)
        val d = (dayText.toIntOrNull() ?: 1).coerceIn(1, 31)
        val h = (hourText.toIntOrNull() ?: 0).coerceIn(0, 23)
        val min = (minuteText.toIntOrNull() ?: 0).coerceIn(0, 59)
        onCalculate(y, m, d, h, min, null)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Quick Presets from AstralSync Specification
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth().testTag("calculator_presets_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Historical & Validation Presets",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Instant test cases from the AstralSync foundation document:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        PresetButton(
                            title = "Sept 2, 2004 02:18 (New York)",
                            subtitle = "Calculates Earth Age, Month 8 / October, Day 17, Mayan Sign Earth (Kab'an)",
                            onClick = {
                                onLoadPreset(2004, 9, 2, 2, 18, LocationPresets.WINDHAM_NY)
                            },
                            tag = "preset_sept_2_2004"
                        )
                        PresetButton(
                            title = "Oct 25, 2007 00:10 (Albany Med, NY)",
                            subtitle = "Albany Med birth conversion in the New System",
                            onClick = {
                                onLoadPreset(2007, 10, 25, 0, 10, LocationPresets.ALBANY_NY)
                            },
                            tag = "preset_oct_25_2007"
                        )
                        PresetButton(
                            title = "April 25, 2023 19:49 (Windham, NY)",
                            subtitle = "Sunrise 06:00 -> 00:00, Sunset 19:49 -> 13:49 Astral Time",
                            onClick = {
                                onLoadPreset(2023, 4, 25, 19, 49, LocationPresets.WINDHAM_NY)
                            },
                            tag = "preset_april_25_2023"
                        )
                    }
                }
            }
        }

        // Custom Date & Time Inputs
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth().testTag("calculator_input_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Gregorian to AstralSync Converter",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SolarGold
                    )
                    Text(
                        text = "Location: ${state.calcLocation.name} (${state.calcLocation.latitude}°, ${state.calcLocation.longitude}°)",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = yearText,
                            onValueChange = { yearText = it; triggerCalc() },
                            label = { Text("Year") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1.3f).testTag("calc_input_year"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = monthText,
                            onValueChange = { monthText = it; triggerCalc() },
                            label = { Text("Month") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("calc_input_month"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = dayText,
                            onValueChange = { dayText = it; triggerCalc() },
                            label = { Text("Day") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("calc_input_day"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = hourText,
                            onValueChange = { hourText = it; triggerCalc() },
                            label = { Text("Hour (0-23)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("calc_input_hour"),
                            singleLine = true
                        )
                        OutlinedTextField(
                            value = minuteText,
                            onValueChange = { minuteText = it; triggerCalc() },
                            label = { Text("Minute (0-59)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f).testTag("calc_input_minute"),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { triggerCalc() },
                        colors = ButtonDefaults.buttonColors(containerColor = SolarGold, contentColor = CosmicBackground),
                        modifier = Modifier.fillMaxWidth().testTag("execute_calc_button")
                    ) {
                        Icon(Icons.Default.Calculate, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Calculate AstralSync Parameters", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // Calculation Results Display
        state.calcResult?.let { result ->
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, SolarGold.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth().testTag("calc_result_card")
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Text(
                            text = "Conversion Results for ${result.gregorianDateFormatted}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SolarGoldLight
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        ResultItemRow("Astral Time (00:00 Sunrise)", result.astralTimeFormatted, SolarGold)
                        ResultItemRow("Continuous Earth Age", result.earthAgeYears, CelestialCyan)
                        ResultItemRow("Astral Month", "Month ${result.newMonthOrder}: ${result.newMonthName} (${result.newMonthMeaning})", TextPrimary)
                        ResultItemRow("Astral Day", "Day ${result.dayInMonth} (30.4375 scale)", TextPrimary)
                        ResultItemRow("Solar Season", "${result.seasonName} (Day ${result.seasonDay})", SolarAmber)
                        ResultItemRow("Weekday", result.weekday, TextSecondary)
                        ResultItemRow("Zodiac Archetype", "${result.zodiacGod.deity} (${result.zodiacGod.sign})", SolarGoldLight)
                        ResultItemRow("Chinese Zodiac", "Year of the ${result.chineseZodiac}", CelestialCyan)
                        ResultItemRow("Mayan Tzolk'in Sign", result.mayanSign, AuroraGreen)

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = CosmicBorder)
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Mathematical Derivation:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Surface(
                            color = CosmicSurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = result.explanation,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                color = TextPrimary,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PresetButton(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    tag: String
) {
    OutlinedButton(
        onClick = onClick,
        colors = ButtonDefaults.outlinedButtonColors(contentColor = TextPrimary),
        border = BorderStroke(1.dp, CosmicBorder),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().testTag(tag)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = SolarGoldLight)
            Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        }
    }
}

@Composable
fun ResultItemRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}
