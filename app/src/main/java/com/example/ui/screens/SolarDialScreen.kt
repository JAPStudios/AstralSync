package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.AstralUiState
import com.example.ui.components.AstralSolarDial
import com.example.ui.theme.*

@Composable
fun SolarDialScreen(
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
        // Celestial Observatory Header
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth().testTag("solar_dial_card")
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Solar Celestial Observatory",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Text(
                        text = "Real-time 24h orbital projection anchored to ${state.currentCoordinate.name}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // The Custom Canvas Solar Dial
                    AstralSolarDial(
                        astralTime = state.astralTime,
                        solarPosition = state.solarPosition,
                        milestones = state.milestones
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Live Solar Coordinates (Elevation & Azimuth)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        SolarMetricBox(
                            label = "Solar Elevation",
                            value = String.format("%.2f°", state.solarPosition.elevationDeg),
                            subtext = if (state.solarPosition.isDaytime) "Above Horizon" else "Below Horizon",
                            color = if (state.solarPosition.isDaytime) SolarGold else CelestialCyan
                        )
                        SolarMetricBox(
                            label = "Solar Azimuth",
                            value = String.format("%.2f°", state.solarPosition.azimuthDeg),
                            subtext = getCompassDirection(state.solarPosition.azimuthDeg),
                            color = SolarAmber
                        )
                        SolarMetricBox(
                            label = "Day Fraction",
                            value = String.format("%.1f%%", state.astralTime.progressPercentOfDay * 100f),
                            subtext = "Solar Day Elapsed",
                            color = AuroraGreen
                        )
                    }
                }
            }
        }

        // Astronomical Arc Explanations
        item {
            Text(
                text = "Astral Horizon Arc Key",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ArcDescriptionRow(
                    name = "Left Horizon (180° / 00:00:00)",
                    meaning = "Immediate sunrise start: top edge of the sun crosses the local horizontal plane. This resets the Astral clock to 00:00:00 everywhere on Earth.",
                    accent = SolarHorizon
                )
                ArcDescriptionRow(
                    name = "Top Zenith (270° / ~06:00 Astral)",
                    meaning = "Solar Noon: The sun achieves its highest celestial altitude for the day across the local observer's meridian.",
                    accent = SolarGold
                )
                ArcDescriptionRow(
                    name = "Right Horizon (0° / ~12:00-14:00 Astral)",
                    meaning = "Sunset: Bottom edge touches horizon until top edge fully disappears. In AstralSync, this occurs between 12:00 and 15:00 depending on season.",
                    accent = SolarAmber
                )
                ArcDescriptionRow(
                    name = "Lower Arc (Night / Astronomical Twilight)",
                    meaning = "Astronomical night: begins when the sun is 18° below the horizon. The sky is dark enough for deep-space astronomy and melatonin synthesis.",
                    accent = CelestialCyan
                )
            }
        }
    }
}

@Composable
fun SolarMetricBox(
    label: String,
    value: String,
    subtext: String,
    color: androidx.compose.ui.graphics.Color
) {
    Surface(
        color = CosmicSurfaceVariant,
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CosmicBorder)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                color = color
            )
            Text(subtext, style = MaterialTheme.typography.labelSmall, color = TextSecondary)
        }
    }
}

@Composable
fun ArcDescriptionRow(
    name: String,
    meaning: String,
    accent: androidx.compose.ui.graphics.Color
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CosmicSurface),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, CosmicBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(accent)
                    .padding(top = 4.dp)
            )
            Column {
                Text(name, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                Text(meaning, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
            }
        }
    }
}

private fun getCompassDirection(azimuthDeg: Double): String {
    val directions = listOf("N", "NNE", "NE", "ENE", "E", "ESE", "SE", "SSE", "S", "SSW", "SW", "WSW", "W", "WNW", "NW", "NNW")
    val index = (((azimuthDeg % 360) + 11.25) / 22.5).toInt() % 16
    return directions[index]
}
