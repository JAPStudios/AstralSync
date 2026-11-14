package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.*
import com.example.ui.AstralUiState
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

@Composable
fun ClockScreen(
    state: AstralUiState,
    onSelectLocation: (LocationCoordinate) -> Unit,
    onRequestGps: () -> Unit,
    onToggleQuantum: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showLocationDialog by remember { mutableStateOf(false) }

    val localTimeFmt = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    localTimeFmt.timeZone = TimeZone.getTimeZone(state.currentCoordinate.timeZoneId)
    val localTimeStr = localTimeFmt.format(Date(state.currentMillis))

    val sunriseTimeFmt = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    sunriseTimeFmt.timeZone = TimeZone.getTimeZone(state.currentCoordinate.timeZoneId)
    val sunriseStr = if (state.milestones.sunriseOccurs) {
        sunriseTimeFmt.format(Date(state.milestones.sunriseStartMillis))
    } else {
        "Does not occur"
    }
    val sunsetStr = if (state.milestones.sunsetOccurs) {
        sunriseTimeFmt.format(Date(state.milestones.sunsetEndMillis))
    } else {
        "Does not occur"
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Location & Astronomical Coordinates Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(18.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.linearGradient(listOf(CosmicBorder, SolarGold.copy(alpha = 0.3f)))),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showLocationDialog = true }
                    .testTag("location_selector_card")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (state.isGpsActive) AuroraGreen.copy(alpha = 0.2f) else SolarGold.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (state.isGpsActive) Icons.Default.GpsFixed else Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = if (state.isGpsActive) AuroraGreen else SolarGold,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                text = state.currentCoordinate.name,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                            Text(
                                text = "${state.currentCoordinate.region} • ${String.format("%.4f°N, %.4f°W", state.currentCoordinate.latitude, abs(state.currentCoordinate.longitude))}",
                                style = MaterialTheme.typography.bodySmall,
                                color = TextSecondary
                            )
                        }
                    }
                    TextButton(
                        onClick = { showLocationDialog = true },
                        modifier = Modifier.testTag("change_location_button")
                    ) {
                        Text("Change", color = SolarGoldLight)
                    }
                }
            }
        }

        // Hero 00:00 Astronomical Clock
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(24.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.verticalGradient(listOf(SolarGold.copy(alpha = 0.5f), CosmicBorder))),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("hero_astral_clock")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Badge: 00:00 Sunrise System
                    Surface(
                        color = SolarGold.copy(alpha = 0.12f),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, SolarGold.copy(alpha = 0.35f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(SolarGold))
                            Text(
                                text = if (state.milestones.sunriseOccurs) {
                                    "TRUE ASTRONOMICAL TIME"
                                } else {
                                    "SOLAR-NOON-ANCHORED ASTRAL TIME"
                                },
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                color = SolarGoldLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Big Astral Time Display
                    Text(
                        text = String.format(
                            "%02d:%02d:%02d",
                            state.astralTime.hours,
                            state.astralTime.minutes,
                            state.astralTime.seconds
                        ),
                        style = MaterialTheme.typography.displayLarge.copy(
                            fontSize = 58.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            letterSpacing = 2.sp
                        ),
                        color = TextPrimary
                    )

                    // Wall-clock time is sourced from millisecond-resolution system time.
                    Text(
                        text = String.format(".%03d ms", state.astralTime.milliseconds),
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = CelestialCyan
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gregorian Comparison Pill
                    Surface(
                        color = CosmicSurfaceVariant,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Schedule, contentDescription = "Standard Time", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            Text(
                                text = "Standard Local: $localTimeStr (${state.currentCoordinate.timeZoneId})",
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                color = TextSecondary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Solar Progress Bar (00:00 Sunrise to 24:00 next Sunrise)
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = if (state.milestones.sunriseOccurs) {
                                    "Sunrise 00:00 ($sunriseStr)"
                                } else {
                                    "Solar noon 00:00 (anchor)"
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = SolarHorizon
                            )
                            Text(
                                text = "Sunset ($sunsetStr)",
                                style = MaterialTheme.typography.labelSmall,
                                color = SolarGold
                            )
                            Text(
                                text = if (state.milestones.sunriseOccurs) "24:00 Next Rise" else "No sunrise today",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { state.astralTime.progressPercentOfDay },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = SolarGold,
                            trackColor = CosmicSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Active Milestone & Countdown
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = SolarGold, modifier = Modifier.size(18.dp))
                            Text(
                                text = state.astralTime.currentMilestone,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                color = SolarGoldLight
                            )
                        }
                        Text(
                            text = "Next: ${state.astralTime.nextMilestone} in ${state.astralTime.timeUntilNextMilestone}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        // Sub-Microsecond Deep Quantum Chronometry Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quantum_chronometry_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.AllInclusive, contentDescription = null, tint = CelestialCyan, modifier = Modifier.size(20.dp))
                            Column {
                                Text(
                                    text = "Elapsed-Time Scale Conversions",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Elapsed-time scale conversions (theoretical below nanoseconds)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                        IconButton(onClick = onToggleQuantum) {
                            Icon(
                                imageVector = if (state.isHighPrecisionQuantum) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle Quantum Details",
                                tint = CelestialCyan
                            )
                        }
                    }

                    // Active elapsed-time reference window
                    state.quantumChronometry.activeTimingReference?.let { reference ->
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            color = AuroraGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, AuroraGreen.copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "ELAPSED-TIME REFERENCE: $reference",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = AuroraGreen,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    if (state.isHighPrecisionQuantum) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            QuantumUnitRow("Millisecond (10⁻³ s)", "${state.quantumChronometry.milliseconds} ms", "One-thousandth of a second")
                            QuantumUnitRow("Microsecond (10⁻⁶ s)", "${state.quantumChronometry.microseconds} µs", "350 ms and 150 ms reference intervals")
                            QuantumUnitRow("Nanosecond (10⁻⁹ s)", "${state.quantumChronometry.nanoseconds} ns", "Distance light travels ~30 cm in 1 ns")
                            QuantumUnitRow("Picosecond (10⁻¹² s)", "${state.quantumChronometry.picoseconds} ps", "Decimal scale conversion; not directly measured")
                            QuantumUnitRow("Femtosecond (10⁻¹⁵ s)", "${state.quantumChronometry.femtoseconds} fs", "Decimal scale conversion; not directly measured")
                            QuantumUnitRow("Attosecond (10⁻¹⁸ s)", "${state.quantumChronometry.attoseconds} as", "Decimal scale conversion; not directly measured")
                            QuantumUnitRow("Zeptosecond (10⁻²¹ s)", state.quantumChronometry.zeptoseconds, "Mathematical scale conversion; not directly measured")
                            QuantumUnitRow("Yoctosecond (10⁻²⁴ s)", state.quantumChronometry.yoctoseconds, "Mathematical scale conversion; not directly measured")
                            QuantumUnitRow("Planck Time (5.39×10⁻⁴⁴ s)", state.quantumChronometry.planckTimes, "Ratio to the Planck-time constant; not a physical measurement")
                        }
                    }
                }
            }
        }

        // Solar Milestones Grid
        item {
            Text(
                text = "Estimated Astronomical Solar Milestones",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                val tz = TimeZone.getTimeZone(state.currentCoordinate.timeZoneId)
                val timeFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply { timeZone = tz }

                val sunriseAvailable = state.milestones.sunriseOccurs
                val sunsetAvailable = state.milestones.sunsetOccurs
                val morningGoldenHourAvailable = state.milestones.morningGoldenHourOccurs
                val eveningGoldenHourAvailable = state.milestones.eveningGoldenHourOccurs
                val sunriseAnchor = state.milestones.sunriseStartMillis
                fun localTime(occurs: Boolean, millis: Long): String =
                    if (occurs) timeFmt.format(Date(millis)) else "Does not occur"
                fun astralTime(occurs: Boolean, millis: Long): String =
                    if (occurs && sunriseAvailable) computeRelativeAstral(millis, sunriseAnchor) else "—"

                MilestoneCard(
                    title = "1. Sunrise Starts (00:00:00 Astral)",
                    desc = "Top edge of sun appears on horizon (Zenith 90.833°)",
                    standardTime = localTime(sunriseAvailable, state.milestones.sunriseStartMillis),
                    astralTime = if (sunriseAvailable) "00:00:00" else "—",
                    accentColor = SolarGold
                )
                MilestoneCard(
                    title = "2. Sunrise Ends (Full Disk)",
                    desc = "Bottom edge of sun touches horizon, full disk visible",
                    standardTime = localTime(sunriseAvailable, state.milestones.sunriseEndMillis),
                    astralTime = astralTime(sunriseAvailable, state.milestones.sunriseEndMillis),
                    accentColor = SolarGoldLight
                )
                MilestoneCard(
                    title = "3. Morning Golden Hour Ends",
                    desc = "Sun reaches 6° elevation; optimal soft light photography",
                    standardTime = localTime(morningGoldenHourAvailable, state.milestones.goldenHourMorningEnd),
                    astralTime = astralTime(morningGoldenHourAvailable, state.milestones.goldenHourMorningEnd),
                    accentColor = SolarAmber
                )
                MilestoneCard(
                    title = "4. Solar Noon (Highest Position)",
                    desc = "Sun at maximum altitude across the local meridian",
                    standardTime = timeFmt.format(Date(state.milestones.solarNoonMillis)),
                    astralTime = astralTime(sunriseAvailable, state.milestones.solarNoonMillis),
                    accentColor = SolarGold
                )
                MilestoneCard(
                    title = "5. Evening Golden Hour Starts",
                    desc = "Sun descends through 6° elevation, warm golden lighting",
                    standardTime = localTime(eveningGoldenHourAvailable, state.milestones.goldenHourEveningStart),
                    astralTime = astralTime(eveningGoldenHourAvailable, state.milestones.goldenHourEveningStart),
                    accentColor = SolarAmber
                )
                MilestoneCard(
                    title = "6. Sunset Starts (Horizon Touch)",
                    desc = "Bottom edge of sun touches western horizon",
                    standardTime = localTime(sunsetAvailable, state.milestones.sunsetStartMillis),
                    astralTime = astralTime(sunsetAvailable, state.milestones.sunsetStartMillis),
                    accentColor = SolarHorizon
                )
                MilestoneCard(
                    title = "7. Sunset Ends (Disappeared)",
                    desc = "Top edge disappears below horizon, dusk begins",
                    standardTime = localTime(sunsetAvailable, state.milestones.sunsetEndMillis),
                    astralTime = astralTime(sunsetAvailable, state.milestones.sunsetEndMillis),
                    accentColor = SolarHorizon
                )
                val nightStatus = when (state.milestones.astronomicalNightCondition) {
                    AstralCalculator.AstronomicalNightCondition.STARTS -> localTime(true, state.milestones.nightStartMillis)
                    AstralCalculator.AstronomicalNightCondition.ALL_DAY -> "All day"
                    AstralCalculator.AstronomicalNightCondition.DOES_NOT_OCCUR -> "Does not occur"
                }
                val nightAstral = when (state.milestones.astronomicalNightCondition) {
                    AstralCalculator.AstronomicalNightCondition.STARTS -> astralTime(sunriseAvailable, state.milestones.nightStartMillis)
                    AstralCalculator.AstronomicalNightCondition.ALL_DAY -> "All day"
                    AstralCalculator.AstronomicalNightCondition.DOES_NOT_OCCUR -> "—"
                }
                MilestoneCard(
                    title = "8. Night Starts (Astronomical Twilight)",
                    desc = "Sun reaches 18° below the horizon",
                    standardTime = nightStatus,
                    astralTime = nightAstral,
                    accentColor = CelestialCyan
                )
            }
        }
    }

    // Location Selection Dialog
    if (showLocationDialog) {
        AlertDialog(
            onDismissRequest = { showLocationDialog = false },
            title = {
                Text(
                    text = "Select Astronomical Horizon",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = TextPrimary
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "Time in AstralSync is locally anchored to when the sun begins to shine over your horizon line. Choose coordinates or auto-detect with device GPS:",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = {
                            showLocationDialog = false
                            onRequestGps()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AuroraGreen),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("use_my_gps_button")
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Auto-Detect via Device GPS")
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = CosmicBorder)

                    LocationPresets.ALL_PRESETS.forEach { preset ->
                        val isSelected = state.currentCoordinate.name == preset.name
                        Surface(
                            color = if (isSelected) SolarGold.copy(alpha = 0.15f) else CosmicSurfaceVariant,
                            shape = RoundedCornerShape(10.dp),
                            border = if (isSelected) BorderStroke(1.dp, SolarGold) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSelectLocation(preset)
                                    showLocationDialog = false
                                }
                                .testTag("preset_${preset.name.replace(" ", "_")}")
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(preset.name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                                    Text("${preset.region} (${preset.latitude}°, ${preset.longitude}°)", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = "Selected", tint = SolarGold, modifier = Modifier.size(20.dp))
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showLocationDialog = false }) {
                    Text("Close", color = SolarGoldLight)
                }
            },
            containerColor = CosmicSurface
        )
    }
}

@Composable
fun QuantumUnitRow(unitName: String, value: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(CosmicSurfaceVariant.copy(alpha = 0.6f))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = unitName, style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
            Text(text = description, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold),
            color = CelestialCyan
        )
    }
}

@Composable
fun MilestoneCard(
    title: String,
    desc: String,
    standardTime: String,
    astralTime: String,
    accentColor: Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CosmicBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(accentColor)
                )
                Column {
                    Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    Text(desc, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = astralTime,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                    color = accentColor
                )
                Text(
                    text = "Local: $standardTime",
                    style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                    color = TextMuted
                )
            }
        }
    }
}

private fun computeRelativeAstral(targetMillis: Long, sunriseMillis: Long): String {
    val dayMillis = 24L * 60L * 60L * 1000L
    val diff = Math.floorMod(targetMillis - sunriseMillis, dayMillis)
    val totalSec = diff / 1000
    val h = totalSec / 3600
    val m = (totalSec % 3600) / 60
    val s = totalSec % 60
    return String.format("%02d:%02d:%02d", h, m, s)
}
