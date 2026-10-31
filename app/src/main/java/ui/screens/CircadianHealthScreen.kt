package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AstralUiState
import com.example.ui.theme.*

@Composable
fun CircadianHealthScreen(
    state: AstralUiState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Hero Circadian Synchronization Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(22.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SolarGold, AuroraGreen))),
                modifier = Modifier.fillMaxWidth().testTag("circadian_hero_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = AuroraGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "ASTRONOMICALLY SYNCHRONIZED BIOLOGY",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                color = AuroraGreen,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = AuroraGreen, modifier = Modifier.size(20.dp))
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "100% Sunlight Immersion Protocol",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "By defining 00:00 at immediate horizon sunrise, your daily rhythm receives 100% of available daylight for peak cortisol awakening response, mitochondrial ATP synthesis, and restorative sleep architecture.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Live Biological Phase Status
                    Surface(
                        color = CosmicSurfaceVariant,
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, CosmicBorder)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(if (state.solarPosition.isDaytime) SolarGold.copy(alpha = 0.2f) else StarlightIndigo.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (state.solarPosition.isDaytime) Icons.Default.WbSunny else Icons.Default.Bedtime,
                                    contentDescription = null,
                                    tint = if (state.solarPosition.isDaytime) SolarGold else StarlightIndigo,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = if (state.solarPosition.isDaytime) "Daylight Biological Window Active" else "Nocturnal Melatonin Window Active",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(
                                    text = "Current Astral Hour: ${String.format("%02d:%02d", state.astralTime.hours, state.astralTime.minutes)} • Elevation: ${String.format("%.1f°", state.solarPosition.elevationDeg)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }

        // Biological Timeline Windows (replacing Sleep Cycle, Fitbit, Pokémon Sleep, Noom)
        item {
            Text(
                text = "Circadian Biological Phases",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CircadianPhaseCard(
                    timeRange = "Astral 00:00 – 01:30",
                    title = "Phase 1: Cortisol Awakening & Lux Intake",
                    benefit = "View natural horizon sunlight within 90 minutes of 00:00 sunrise. Activates melanopsin retinal ganglion cells to suppress melatonin and set the circadian master clock.",
                    accent = SolarGold,
                    icon = Icons.Default.Visibility
                )
                CircadianPhaseCard(
                    timeRange = "Astral 02:00 – 06:00",
                    title = "Phase 2: Peak Cognitive & Metabolic Engine",
                    benefit = "Leading into Solar Noon zenith. Core body temperature climbs, insulin sensitivity peaks, and deep analytical focus is maximally supported by cosmic solar irradiance.",
                    accent = SolarAmber,
                    icon = Icons.Default.Psychology
                )
                CircadianPhaseCard(
                    timeRange = "Astral ~11:00 – Sunset",
                    title = "Phase 3: Golden Hour Wind-Down & Red Lux",
                    benefit = "Low solar angle triggers natural retinal filtering of blue wavelengths. Encourages pineal gland signaling and gradual adenosine build-up for effortless evening wind-down.",
                    accent = SolarHorizon,
                    icon = Icons.Default.WbTwilight
                )
                CircadianPhaseCard(
                    timeRange = "Night Starts (Astronomical Twilight)",
                    title = "Phase 4: Astronomical Deep Sleep & Glymphatic Clearance",
                    benefit = "Sun drops 18° below horizon. True cosmic darkness allows peak melatonin release, deep slow-wave REM sleep, cellular repair, and brain glymphatic toxin drainage.",
                    accent = StarlightIndigo,
                    icon = Icons.Default.Nightlight
                )
            }
        }

        // Astronomical Solar Compass (replacing Samsung Compass)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth().testTag("astronomic_compass_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Explore, contentDescription = null, tint = CelestialCyan, modifier = Modifier.size(20.dp))
                            Text(
                                text = "Astronomical Solar Compass",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = TextPrimary
                            )
                        }
                        Text(
                            text = "True Solar North",
                            style = MaterialTheme.typography.labelSmall,
                            color = TextMuted
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Unlike magnetic phone compasses prone to electromagnetic interference, AstralSync computes absolute astronomical solar azimuth directly from NOAA ephemeris equations.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        CompassPill("Azimuth", String.format("%.1f°", state.solarPosition.azimuthDeg))
                        CompassPill("Elevation", String.format("%.1f°", state.solarPosition.elevationDeg))
                        CompassPill("Horizon Status", if (state.solarPosition.isDaytime) "Daylight" else "Sub-Horizon")
                    }
                }
            }
        }

        // Open Source Competitor Disruption Summary
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurfaceVariant),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, SolarGold.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Free Public Utility vs Corporate Paywalls",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = SolarGoldLight
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Competitors like Headspace ($70/yr), Noom ($70/mo), Fitbit Premium ($80/yr), and Sleep Cycle lock basic circadian telemetry behind predatory subscriptions. AstralSync is built as a non-profit scientific public utility honoring true planetary physics.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun CircadianPhaseCard(
    timeRange: String,
    title: String,
    benefit: String,
    accent: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(1.dp, CosmicBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(icon, contentDescription = null, tint = accent, modifier = Modifier.size(18.dp))
                    Text(title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                }
                Surface(
                    color = accent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = timeRange,
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold),
                        color = accent,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(benefit, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
        }
    }
}

@Composable
fun CompassPill(label: String, value: String) {
    Surface(
        color = CosmicSurface,
        shape = RoundedCornerShape(10.dp),
        border = BorderStroke(1.dp, CosmicBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text(value, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace), color = SolarGoldLight)
        }
    }
}
