package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AstralCalendarEngine
import com.example.ui.AstralUiState
import com.example.ui.theme.*

@Composable
fun CalendarScreen(
    state: AstralUiState,
    onToggleHemisphere: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedZodiacIndex by remember { mutableStateOf(0) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Planetary Earth-Age Year Hero
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(22.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SolarGold, StarlightIndigo))),
                modifier = Modifier.fillMaxWidth().testTag("earth_age_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = StarlightIndigo.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                text = "CONTINUOUS EARTH EPOCH",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                color = CelestialCyan,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                        TextButton(
                            onClick = onToggleHemisphere,
                            modifier = Modifier.testTag("toggle_hemisphere_button")
                        ) {
                            Icon(Icons.Default.Public, contentDescription = null, modifier = Modifier.size(16.dp), tint = SolarGoldLight)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (state.isSouthernHemisphere) "Southern / Asia Order" else "Northern Order",
                                style = MaterialTheme.typography.labelSmall,
                                color = SolarGoldLight
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = state.astralDate.earthAgeFormatted,
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = SolarGoldLight
                    )
                    Text(
                        text = "Continuously counting forward from the conception (birth) of planet Earth ~4.54 billion years ago.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Month and Day in the New System
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            color = CosmicSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("ASTRAL MONTH", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Text(
                                    text = "Month ${state.astralDate.monthOrder}: ${state.astralDate.monthName}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = TextPrimary
                                )
                                Text(state.astralDate.monthMeaning, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }

                        Surface(
                            color = CosmicSurfaceVariant,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text("DAY IN MONTH (30.4375)", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                Text(
                                    text = "Day ${state.astralDate.dayFormatted}",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                                    color = CelestialCyan
                                )
                                Text("Transitions at .4375 point", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                        }
                    }
                }
            }
        }

        // Season Progress Tracker (91.3125 days)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Current Solar Season: ${state.astralDate.seasonName}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = SolarAmber
                        )
                        Text(
                            text = "${String.format("%.1f", state.astralDate.seasonDay)} / 91.31 Days",
                            style = MaterialTheme.typography.bodyMedium.copy(fontFamily = FontFamily.Monospace),
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { state.astralDate.seasonProgressPercent },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = SolarAmber,
                        trackColor = CosmicSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "The 4 equal astronomical seasons (91.3125 days each across 365.25 day solar year):",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    val seasons = if (state.isSouthernHemisphere) AstralCalendarEngine.SEASONS_SOUTHERN else AstralCalendarEngine.SEASONS_NORTHERN
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        seasons.forEach { s ->
                            val isCurrent = s.name == state.astralDate.seasonName
                            Surface(
                                color = if (isCurrent) SolarAmber.copy(alpha = 0.2f) else CosmicSurfaceVariant,
                                shape = RoundedCornerShape(8.dp),
                                border = if (isCurrent) BorderStroke(1.dp, SolarAmber) else null,
                                modifier = Modifier.weight(1f)
                            ) {
                                Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = s.name,
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = if (isCurrent) SolarGoldLight else TextPrimary
                                    )
                                    Text("91.31d", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Planetary 12-Month Order
        item {
            Text(
                text = "The Astral Month Sequence (True Order)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(AstralCalendarEngine.ASTRAL_MONTHS_12) { month ->
                    val isCurrent = month.name == state.astralDate.monthName
                    Card(
                        colors = CardDefaults.cardColors(containerColor = if (isCurrent) CosmicSurfaceVariant else CosmicSurface),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, if (isCurrent) SolarGold else CosmicBorder),
                        modifier = Modifier.width(170.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("M${month.order}", style = MaterialTheme.typography.labelSmall, color = SolarGoldLight)
                                if (isCurrent) {
                                    Text("NOW", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = AuroraGreen)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(month.name, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(month.meaning, style = MaterialTheme.typography.bodySmall, color = TextSecondary, maxLines = 2)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text("30.4375 Days", style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace), color = TextMuted)
                        }
                    }
                }
            }
        }

        // Mythology & Zodiac Archetypes
        item {
            Text(
                text = "Astral Mythology & Zodiac Pantheon",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Current Archetype: ${state.astralDate.zodiacGod.sign} — ${state.astralDate.zodiacGod.deity}",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SolarGold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = state.astralDate.zodiacGod.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = CosmicBorder)
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("CHINESE CALENDAR YEAR", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = "Year of the ${state.astralDate.chineseZodiac}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = CelestialCyan
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("MAYAN TZOLK'IN DAY SIGN", style = MaterialTheme.typography.labelSmall, color = TextMuted)
                            Text(
                                text = state.astralDate.mayanSign,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = AuroraGreen
                            )
                        }
                    }
                }
            }
        }

        // Weekday Etymology
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Today: ${state.astralDate.weekdayName}",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = state.astralDate.weekdayEtymology,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}
