package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AstralCalculator
import com.example.ui.theme.*
import kotlin.math.*

@Composable
fun AstralSolarDial(
    astralTime: AstralCalculator.AstralTime,
    solarPosition: AstralCalculator.SolarPosition,
    milestones: AstralCalculator.SolarMilestones,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "sun_pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .testTag("astral_solar_dial"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = (size.minDimension / 2f) - 24.dp.toPx()
            val strokeWidth = 14.dp.toPx()

            // 1. Draw outer celestial orbit track
            drawCircle(
                color = CosmicSurfaceVariant,
                radius = radius,
                center = center,
                style = Stroke(width = strokeWidth)
            )

            // 2. Draw Daylight Arc (from 00:00 Sunrise to Sunset)
            // In Astral Time, 24 hours = 360 degrees.
            // 00:00 (Sunrise) is at 180 degrees (Left Horizon)
            // Solar noon is at 270 degrees (Top Zenith)
            // Sunset is at ~0 degrees (Right Horizon)
            val dayFraction = ((milestones.sunsetEndMillis - milestones.sunriseStartMillis).toDouble() / (24.0 * 3600000.0)).toFloat().coerceIn(0.2f, 0.8f)
            val daySweepAngle = dayFraction * 360f

            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to SolarHorizon,
                    0.25f to SolarGold,
                    0.5f to SolarAmber,
                    1.0f to SolarHorizon,
                    center = center
                ),
                startAngle = 180f,
                sweepAngle = daySweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // 3. Draw Night Arc (from Sunset to next Sunrise)
            val nightSweep = 360f - daySweepAngle
            drawArc(
                brush = Brush.sweepGradient(
                    0.0f to CosmicBorder,
                    0.5f to DeepNight,
                    1.0f to CosmicBorder,
                    center = center
                ),
                startAngle = (180f + daySweepAngle) % 360f,
                sweepAngle = nightSweep,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeWidth - 4.dp.toPx())
            )

            // 4. Draw Horizon Line (East-West sunrise/sunset line)
            val horizonLeft = Offset(center.x - radius - 16.dp.toPx(), center.y)
            val horizonRight = Offset(center.x + radius + 16.dp.toPx(), center.y)
            drawLine(
                color = SolarHorizon.copy(alpha = 0.5f),
                start = horizonLeft,
                end = horizonRight,
                strokeWidth = 2.dp.toPx(),
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
            )

            // 5. Hour tick marks (0h, 3h, 6h, 9h, 12h, 15h, 18h, 21h in Astral Time)
            for (h in 0 until 24) {
                val angleDeg = 180f + (h / 24f) * 360f
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val isMajor = h % 3 == 0
                val tickLength = if (isMajor) 14.dp.toPx() else 7.dp.toPx()
                val innerR = radius - strokeWidth / 2f - tickLength
                val outerR = radius - strokeWidth / 2f

                val start = Offset(
                    (center.x + innerR * cos(angleRad)).toFloat(),
                    (center.y + innerR * sin(angleRad)).toFloat()
                )
                val end = Offset(
                    (center.x + outerR * cos(angleRad)).toFloat(),
                    (center.y + outerR * sin(angleRad)).toFloat()
                )

                val tickColor = if (isMajor) SolarGoldLight else TextMuted.copy(alpha = 0.4f)
                drawLine(
                    color = tickColor,
                    start = start,
                    end = end,
                    strokeWidth = if (isMajor) 2.5.dp.toPx() else 1.dp.toPx()
                )
            }

            // 6. Draw Current Astral Sun Position Indicator
            // Current progress: 00:00 -> 180 deg (Left Horizon), moving clockwise over top zenith
            val currentAngleDeg = 180f + astralTime.progressPercentOfDay * 360f
            val sunRad = Math.toRadians(currentAngleDeg.toDouble())
            val sunPos = Offset(
                (center.x + radius * cos(sunRad)).toFloat(),
                (center.y + radius * sin(sunRad)).toFloat()
            )

            // Outer sun corona glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        SolarGold.copy(alpha = 0.6f * pulseGlow),
                        SolarAmber.copy(alpha = 0.2f),
                        Color.Transparent
                    ),
                    center = sunPos,
                    radius = 32.dp.toPx() * pulseGlow
                ),
                radius = 32.dp.toPx() * pulseGlow,
                center = sunPos
            )

            // Inner glowing sun orb
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White, SolarGold, SolarAmber),
                    center = sunPos,
                    radius = 12.dp.toPx()
                ),
                radius = 12.dp.toPx(),
                center = sunPos
            )
            drawCircle(
                color = Color.White,
                radius = 5.dp.toPx(),
                center = sunPos
            )

            // Zenith (Solar Noon) crown marker at Top
            val zenithPos = Offset(center.x, center.y - radius - 10.dp.toPx())
            drawCircle(color = SolarGold, radius = 3.dp.toPx(), center = zenithPos)
        }

        // Center Dial Information Overlay
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = String.format("%02d:%02d:%02d", astralTime.hours, astralTime.minutes, astralTime.seconds),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                ),
                color = SolarGoldLight
            )
            Text(
                text = "ASTRAL TIME (00:00 = SUNRISE)",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                ),
                color = TextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sun Alt: ${String.format("%.1f°", solarPosition.elevationDeg)} • Az: ${String.format("%.1f°", solarPosition.azimuthDeg)}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                color = CelestialCyan
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = astralTime.currentMilestone,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = SolarAmber
            )
        }
    }
}
