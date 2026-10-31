package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.FundingStrategyData
import com.example.ui.theme.*

@Composable
fun FundingStrategyScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
    ) {
        // Hero Manifesto
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(22.dp),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SolarGold, CelestialCyan))),
                modifier = Modifier.fillMaxWidth().testTag("funding_manifesto_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Surface(
                        color = SolarGold.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "NOT-FOR-PROFIT PUBLIC BENEFIT ROADMAP",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                            color = SolarGoldLight,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Funding AstralSync: The Open Competitor",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Big Tech monopolies (Google Clock, Samsung Clock/Health, Apple Watch, Microsoft Calendar, Fitbit, Headspace, Noom, Pokémon Sleep) profit from proprietary walled gardens and arbitrary 19th-century railway time zones. AstralSync can secure public-interest capital as a scientific, open-source 501(c)(3) standard for circadian health and astronomical accuracy.",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                }
            }
        }

        // Official Links
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = "Official AstralSync Repositories & Documents",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )

                    ExternalLinkRow(
                        title = "GitHub - JAPStudios/AstralSync",
                        subtitle = "Open Source Repository & Public Specifications",
                        url = "https://github.com/JAPStudios/AstralSync",
                        tag = "link_github"
                    )
                    ExternalLinkRow(
                        title = "Google Docs - Legal & Funding Requirements",
                        subtitle = "Non-profit bylaws, budget allocations, and patent filings",
                        url = "https://docs.google.com",
                        tag = "link_docs"
                    )
                }
            }
        }

        // Legal Precedent Analysis: Melzer v. Johnson & Johnson
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(18.dp),
                border = BorderStroke(1.dp, CelestialCyan.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth().testTag("legal_analysis_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Legal Precedent: Melzer v. J&J",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = CelestialCyan
                        )
                        Icon(Icons.Default.Gavel, contentDescription = null, tint = CelestialCyan, modifier = Modifier.size(20.dp))
                    }
                    Text(
                        text = FundingStrategyData.LEGAL_FRAMEWORK.citation,
                        style = MaterialTheme.typography.labelSmall.copy(fontFamily = FontFamily.Monospace),
                        color = TextMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Core Legal Principle:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = SolarGoldLight
                    )
                    Text(
                        text = FundingStrategyData.LEGAL_FRAMEWORK.keyPrinciple,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Strategic Application to AstralSync:",
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = AuroraGreen
                    )
                    Text(
                        text = FundingStrategyData.LEGAL_FRAMEWORK.applicationToAstralSync,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextPrimary
                    )
                }
            }
        }

        // The 3 Grant Funding Pillars
        item {
            Text(
                text = "Three Actionable Capital Avenues ($500K - $3M+)",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        items(FundingStrategyData.GRANT_PILLARS) { pillar ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(16.dp),
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
                            text = pillar.category,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = SolarGoldLight
                        )
                        Surface(
                            color = AuroraGreen.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = pillar.grantSizeRange,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace),
                                color = AuroraGreen,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = pillar.valueProposition,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Target Grant Institutions:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    pillar.targetFunders.forEach { funder ->
                        Text("• $funder", style = MaterialTheme.typography.bodySmall, color = CelestialCyan)
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Execution Checklist:", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                    pillar.actionSteps.forEach { step ->
                        Text("✓ $step", style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                    }
                }
            }
        }

        // Competitor Disruption Matrix
        item {
            Text(
                text = "Disrupting the Tech Oligopoly",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
        }

        items(FundingStrategyData.COMPETITOR_MATRIX) { comp ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CosmicSurface),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, CosmicBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(comp.category, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold), color = TextPrimary)
                        Text(
                            text = comp.proprietaryMonopolies.joinToString(", "),
                            style = MaterialTheme.typography.labelSmall,
                            color = NebulaRose
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Proprietary Limitation: ${comp.corporateFlaw}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "AstralSync Advantage: ${comp.astralSyncAdvantage}",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                        color = AuroraGreen
                    )
                }
            }
        }
    }
}

@Composable
fun ExternalLinkRow(title: String, subtitle: String, url: String, tag: String) {
    val context = LocalContext.current
    Surface(
        color = CosmicSurfaceVariant,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                try {
                    context.startActivity(intent)
                } catch (_: Exception) {}
            }
            .testTag(tag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold), color = CelestialCyan)
                Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }
            Icon(Icons.Default.OpenInNew, contentDescription = null, tint = CelestialCyan, modifier = Modifier.size(18.dp))
        }
    }
}
