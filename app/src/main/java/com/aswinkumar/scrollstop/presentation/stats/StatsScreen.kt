package com.aswinkumar.scrollstop.presentation.stats

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aswinkumar.scrollstop.core.theme.SageGreenDark
import com.aswinkumar.scrollstop.core.theme.SageGreenPrimary
import com.aswinkumar.scrollstop.core.theme.ScrollStopTheme
import com.aswinkumar.scrollstop.domain.model.AppTrigger
import com.aswinkumar.scrollstop.domain.model.InterventionStat
import com.aswinkumar.scrollstop.domain.model.TimePeriod

@Composable
fun StatsScreen(
    uiState: StatsUiState,
    onPeriodSelected: (TimePeriod) -> Unit,
    onToggleAppMonitoring: (String, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Title Header
        Text(
            text = "Statistics & Insights",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time Period Segmented Buttons (Day / Week / Month)
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            TimePeriod.entries.forEachIndexed { index, period ->
                SegmentedButton(
                    selected = uiState.selectedPeriod == period,
                    onClick = { onPeriodSelected(period) },
                    shape = SegmentedButtonDefaults.itemShape(
                        index = index,
                        count = TimePeriod.entries.size
                    ),
                    colors = SegmentedButtonDefaults.colors(
                        activeContainerColor = SageGreenPrimary,
                        activeContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(period.name.lowercase().replaceFirstChar { it.uppercase() })
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Total Time Saved Banner Card
        EstimatedTimeSavedCard(
            totalTimeSavedMinutes = uiState.totalEstimatedTimeSavedMinutes,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Intervention Bar Chart Card
        InterventionBarChartCard(
            stats = uiState.interventionStats,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Top Trigger Apps Section
        Text(
            text = "Top Trigger Feeds & Apps",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        uiState.topTriggerApps.forEach { app ->
            TriggerAppItemCard(
                app = app,
                onToggleMonitoring = { isMonitored ->
                    onToggleAppMonitoring(app.packageName, isMonitored)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun EstimatedTimeSavedCard(
    totalTimeSavedMinutes: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = SageGreenDark,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Timer,
                        contentDescription = "Time Saved",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = "Estimated Time Reclaimed",
                    style = MaterialTheme.typography.labelLarge,
                    color = SageGreenDark
                )
                Text(
                    text = "${totalTimeSavedMinutes / 60}h ${totalTimeSavedMinutes % 60}m saved",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
private fun InterventionBarChartCard(
    stats: List<InterventionStat>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.BarChart,
                    contentDescription = "Intervention Chart",
                    tint = SageGreenPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Interventions Frequency",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            val maxVal = (stats.maxOfOrNull { it.interventionCount } ?: 1).coerceAtLeast(1)
            val barColor = SageGreenPrimary
            val trackColor = MaterialTheme.colorScheme.surfaceVariant

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val barWidth = (size.width / (stats.size * 2f)).coerceAtMost(36.dp.toPx())
                    val spacing = (size.width - barWidth * stats.size) / (stats.size + 1)

                    stats.forEachIndexed { index, item ->
                        val x = spacing + index * (barWidth + spacing)
                        val barHeight = (item.interventionCount.toFloat() / maxVal) * (size.height - 30.dp.toPx())

                        // Draw background track
                        drawRoundRect(
                            color = trackColor,
                            topLeft = Offset(x, 0f),
                            size = Size(barWidth, size.height - 30.dp.toPx()),
                            cornerRadius = CornerRadius(12f, 12f)
                        )

                        // Draw active bar
                        drawRoundRect(
                            color = barColor,
                            topLeft = Offset(x, (size.height - 30.dp.toPx()) - barHeight),
                            size = Size(barWidth, barHeight),
                            cornerRadius = CornerRadius(12f, 12f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // X-Axis Labels
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                stats.forEach { item ->
                    Text(
                        text = item.periodLabel,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun TriggerAppItemCard(
    app: AppTrigger,
    onToggleMonitoring: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = app.appName.take(1).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SageGreenDark
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${app.interventionsTriggered} pauses • ${app.timeSpentMinutes} mins spent",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Switch(
                checked = app.isMonitored,
                onCheckedChange = onToggleMonitoring,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                    checkedTrackColor = SageGreenPrimary
                )
            )
        }
    }
}

@Preview(name = "Stats Light Mode", showBackground = true)
@Preview(name = "Stats Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun StatsScreenPreview() {
    ScrollStopTheme {
        StatsScreen(
            uiState = StatsUiState(
                selectedPeriod = TimePeriod.WEEK,
                interventionStats = listOf(
                    InterventionStat("Mon", 12, 35),
                    InterventionStat("Tue", 18, 50),
                    InterventionStat("Wed", 14, 42),
                    InterventionStat("Thu", 9, 28),
                    InterventionStat("Fri", 15, 45)
                ),
                topTriggerApps = listOf(
                    AppTrigger("1", "Instagram Reels", "com.instagram.android", 8, 45, true),
                    AppTrigger("2", "YouTube Shorts", "com.google.android.youtube", 4, 35, true)
                )
            ),
            onPeriodSelected = {},
            onToggleAppMonitoring = { _, _ -> }
        )
    }
}
