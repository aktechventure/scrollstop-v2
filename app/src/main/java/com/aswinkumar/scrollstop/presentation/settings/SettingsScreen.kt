package com.aswinkumar.scrollstop.presentation.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.RssFeed
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aswinkumar.scrollstop.core.theme.AccentCoral
import com.aswinkumar.scrollstop.core.theme.SageGreenDark
import com.aswinkumar.scrollstop.core.theme.SageGreenPrimary
import com.aswinkumar.scrollstop.core.theme.ScrollStopTheme
import com.aswinkumar.scrollstop.domain.model.AppSettings
import com.aswinkumar.scrollstop.domain.model.PermissionState

@Composable
fun SettingsScreen(
    uiState: SettingsUiState,
    onRequestUsageAccess: () -> Unit,
    onRequestOverlayPermission: () -> Unit,
    onRequestAccessibilityService: () -> Unit,
    onUpdateDailyLimit: (Int) -> Unit,
    onToggleFeed: (String, Boolean) -> Unit,
    onShowDataDeletionDialog: (Boolean) -> Unit,
    onConfirmClearData: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header Title
        Text(
            text = "Settings & Privacy",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        // System Permissions Deep Links Section
        SettingsSectionTitle(title = "App Permissions")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                PermissionLinkItem(
                    title = "Usage Access",
                    isGranted = uiState.permissionState.hasUsageAccess,
                    onClick = onRequestUsageAccess
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                PermissionLinkItem(
                    title = "Overlay Permission",
                    isGranted = uiState.permissionState.hasOverlayPermission,
                    onClick = onRequestOverlayPermission
                )
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                PermissionLinkItem(
                    title = "Accessibility Service",
                    isGranted = uiState.permissionState.hasAccessibilityService,
                    onClick = onRequestAccessibilityService
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Time Limits Section
        SettingsSectionTitle(title = "Daily Limit")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = "Daily Limit",
                            tint = SageGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Daily Feed Limit",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = "${uiState.settings.dailyTimeLimitMinutes} mins",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = SageGreenDark
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Slider(
                    value = uiState.settings.dailyTimeLimitMinutes.toFloat(),
                    onValueChange = { onUpdateDailyLimit(it.toInt()) },
                    valueRange = 15f..120f,
                    steps = 6,
                    colors = SliderDefaults.colors(
                        thumbColor = SageGreenPrimary,
                        activeTrackColor = SageGreenPrimary
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Monitored Apps & Feeds Section
        SettingsSectionTitle(title = "Monitored Feeds")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                uiState.settings.feedsList.forEachIndexed { index, feed ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.RssFeed,
                                contentDescription = feed.name,
                                tint = SageGreenPrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = feed.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Switch(
                            checked = feed.isEnabled,
                            onCheckedChange = { isChecked ->
                                onToggleFeed(feed.packageName, isChecked)
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = MaterialTheme.colorScheme.onPrimary,
                                checkedTrackColor = SageGreenPrimary
                            )
                        )
                    }
                    if (index < uiState.settings.feedsList.lastIndex) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Privacy & Data Control Section
        SettingsSectionTitle(title = "Privacy & Data")

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Privacy Policy",
                            tint = SageGreenPrimary
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "On-Device Data Guarantee",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "100% Local",
                        style = MaterialTheme.typography.labelMedium,
                        color = SageGreenDark,
                        fontWeight = FontWeight.Bold
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                OutlinedButton(
                    onClick = { onShowDataDeletionDialog(true) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteForever,
                        contentDescription = "Delete Data",
                        tint = AccentCoral
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Clear Local Data & Reset",
                        color = AccentCoral
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Confirmation Dialog for Data Deletion
    if (uiState.showDataDeletionDialog) {
        AlertDialog(
            onDismissRequest = { onShowDataDeletionDialog(false) },
            title = {
                Text(
                    text = "Clear All Local Data?",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = "This will reset all your settings, logged interventions, and custom focus goals on this device. This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = onConfirmClearData
                ) {
                    Text("Delete Everything", color = AccentCoral, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { onShowDataDeletionDialog(false) }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun PermissionLinkItem(
    title: String,
    isGranted: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (isGranted) "Granted" else "Requires setup in System Settings",
                style = MaterialTheme.typography.labelMedium,
                color = if (isGranted) SageGreenDark else AccentCoral
            )
        }
        OutlinedButton(
            onClick = onClick,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(if (isGranted) "Manage" else "Enable")
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                contentDescription = title,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

@Preview(name = "Settings Light Mode", showBackground = true)
@Preview(name = "Settings Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun SettingsScreenPreview() {
    ScrollStopTheme {
        SettingsScreen(
            uiState = SettingsUiState(
                settings = AppSettings(),
                permissionState = PermissionState(
                    hasUsageAccess = true,
                    hasOverlayPermission = false,
                    hasAccessibilityService = true
                )
            ),
            onRequestUsageAccess = {},
            onRequestOverlayPermission = {},
            onRequestAccessibilityService = {},
            onUpdateDailyLimit = {},
            onToggleFeed = { _, _ -> },
            onShowDataDeletionDialog = {},
            onConfirmClearData = {}
        )
    }
}
