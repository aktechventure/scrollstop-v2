package com.aswinkumar.scrollstop.presentation.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DataUsage
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aswinkumar.scrollstop.core.theme.SageGreenDark
import com.aswinkumar.scrollstop.core.theme.SageGreenPrimary
import com.aswinkumar.scrollstop.core.theme.ScrollStopTheme
import com.aswinkumar.scrollstop.domain.model.PermissionState

@Composable
fun LandingOnboardingScreen(
    uiState: OnboardingUiState,
    onRequestUsageAccess: () -> Unit,
    onRequestOverlayPermission: () -> Unit,
    onRequestAccessibilityService: () -> Unit,
    onCompleteOnboarding: () -> Unit,
    onNextStep: () -> Unit,
    onPrevStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Serene Lake & Mountain Backdrop Canvas
        SereneBackdropCanvas(
            modifier = Modifier.fillMaxSize()
        )

        // Overlay Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(36.dp))

            // App Header
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "ScrollStop",
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Pause the Loop, Reclaim Your Time.",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White.copy(alpha = 0.9f),
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Onboarding Step Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Step Progress Indicator
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(3) { index ->
                            val isCurrent = uiState.currentStep == index
                            val isCompleted = when (index) {
                                0 -> uiState.permissionState.hasUsageAccess
                                1 -> uiState.permissionState.hasOverlayPermission
                                2 -> uiState.permissionState.hasAccessibilityService
                                else -> false
                            }
                            Box(
                                modifier = Modifier
                                    .padding(horizontal = 4.dp)
                                    .height(6.dp)
                                    .width(if (isCurrent) 28.dp else 12.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isCompleted -> SageGreenPrimary
                                            isCurrent -> SageGreenDark
                                            else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        }
                                    )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Step Specific Content
                    when (uiState.currentStep) {
                        0 -> PermissionStepContent(
                            icon = Icons.Default.DataUsage,
                            title = "Step 1: Usage Access",
                            description = "ScrollStop detects when short-form video feeds (Reels, Shorts, TikTok) open so it can measure your mindful limits.",
                            isGranted = uiState.permissionState.hasUsageAccess,
                            actionText = if (uiState.permissionState.hasUsageAccess) "Usage Access Granted" else "Grant Usage Access",
                            onAction = onRequestUsageAccess
                        )
                        1 -> PermissionStepContent(
                            icon = Icons.Default.Layers,
                            title = "Step 2: Overlay Permission",
                            description = "Allows ScrollStop to show a gentle pause overlay card over endless feeds, giving you a breathing moment.",
                            isGranted = uiState.permissionState.hasOverlayPermission,
                            actionText = if (uiState.permissionState.hasOverlayPermission) "Overlay Permission Granted" else "Grant Overlay Permission",
                            onAction = onRequestOverlayPermission
                        )
                        2 -> PermissionStepContent(
                            icon = Icons.Default.AccessibilityNew,
                            title = "Step 3: Accessibility Service",
                            description = "Provides precise feed detection and gentle back-button navigation when you choose to pause a scrolling loop.",
                            isGranted = uiState.permissionState.hasAccessibilityService,
                            actionText = if (uiState.permissionState.hasAccessibilityService) "Accessibility Enabled" else "Enable Accessibility Service",
                            onAction = onRequestAccessibilityService
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Navigation Actions
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        if (uiState.currentStep > 0) {
                            OutlinedButton(
                                onClick = onPrevStep,
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("Back")
                            }
                        } else {
                            Spacer(modifier = Modifier.width(1.dp))
                        }

                        if (uiState.currentStep < 2) {
                            Button(
                                onClick = onNextStep,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SageGreenDark)
                            ) {
                                Text("Next")
                                Spacer(modifier = Modifier.width(6.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = "Next Step"
                                )
                            }
                        } else {
                            Button(
                                onClick = onCompleteOnboarding,
                                shape = RoundedCornerShape(16.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SageGreenPrimary)
                            ) {
                                Text("Get Started")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PermissionStepContent(
    icon: ImageVector,
    title: String,
    description: String,
    isGranted: Boolean,
    actionText: String,
    onAction: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            shape = CircleShape,
            color = if (isGranted) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(64.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = if (isGranted) Icons.Default.CheckCircle else icon,
                    contentDescription = title,
                    tint = if (isGranted) SageGreenDark else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onAction,
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isGranted) SageGreenPrimary else MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(actionText)
        }
    }
}

@Composable
private fun SereneBackdropCanvas(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        // Sky Gradient
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF2B4C3F),
                    Color(0xFF4A7C59),
                    Color(0xFF88A090)
                ),
                startY = 0f,
                endY = height * 0.65f
            ),
            size = Size(width, height)
        )

        // Distant Mountain Silhouette
        val mountainPath1 = Path().apply {
            moveTo(0f, height * 0.5f)
            lineTo(width * 0.25f, height * 0.38f)
            lineTo(width * 0.55f, height * 0.48f)
            lineTo(width * 0.8f, height * 0.35f)
            lineTo(width, height * 0.45f)
            lineTo(width, height * 0.6f)
            lineTo(0f, height * 0.6f)
            close()
        }
        drawPath(path = mountainPath1, color = Color(0xFF213A2F).copy(alpha = 0.7f))

        // Closer Mountain Silhouette
        val mountainPath2 = Path().apply {
            moveTo(0f, height * 0.55f)
            lineTo(width * 0.35f, height * 0.42f)
            lineTo(width * 0.7f, height * 0.52f)
            lineTo(width, height * 0.46f)
            lineTo(width, height * 0.62f)
            lineTo(0f, height * 0.62f)
            close()
        }
        drawPath(path = mountainPath2, color = Color(0xFF162920))

        // Lake Water Surface
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF1B382B),
                    Color(0xFF12241C)
                )
            ),
            topLeft = Offset(0f, height * 0.62f),
            size = Size(width, height * 0.38f)
        )

        // Water Reflection Lines
        for (i in 1..6) {
            val yPos = height * (0.64f + i * 0.05f)
            drawLine(
                color = Color(0xFF5B8A6A).copy(alpha = 0.25f - i * 0.03f),
                start = Offset(width * (0.2f + i * 0.02f), yPos),
                end = Offset(width * (0.8f - i * 0.02f), yPos),
                strokeWidth = 3f
            )
        }
    }
}

@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun LandingOnboardingScreenPreview() {
    ScrollStopTheme {
        LandingOnboardingScreen(
            uiState = OnboardingUiState(
                currentStep = 0,
                permissionState = PermissionState(
                    hasUsageAccess = false,
                    hasOverlayPermission = false,
                    hasAccessibilityService = false
                )
            ),
            onRequestUsageAccess = {},
            onRequestOverlayPermission = {},
            onRequestAccessibilityService = {},
            onCompleteOnboarding = {},
            onNextStep = {},
            onPrevStep = {}
        )
    }
}
