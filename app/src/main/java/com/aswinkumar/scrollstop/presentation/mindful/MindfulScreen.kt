package com.aswinkumar.scrollstop.presentation.mindful

import android.content.res.Configuration
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.aswinkumar.scrollstop.core.theme.SageGreenDark
import com.aswinkumar.scrollstop.core.theme.SageGreenPrimary
import com.aswinkumar.scrollstop.core.theme.ScrollStopTheme

@Composable
fun MindfulScreen(
    uiState: MindfulUiState,
    onStartBreathing: () -> Unit,
    onUpdateIntention: (String) -> Unit,
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
            text = "Mindfulness & Well-being",
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Guided Breathing Exercise Card
        BreathingExerciseCard(
            isActive = uiState.isBreathingActive,
            secondsRemaining = uiState.breathingSecondsRemaining,
            completedSessions = uiState.totalBreathingSessionsCompleted,
            onStart = onStartBreathing,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Daily Intention Card
        DailyIntentionCard(
            currentIntention = uiState.dailyIntention,
            onSaveIntention = onUpdateIntention,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Reflection Questions Section
        Text(
            text = "Mindful Check-ins",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        ReflectionPromptCard(
            icon = Icons.Default.Psychology,
            prompt = "Why did I open this app right now?",
            subtitle = "Am I looking for connection, rest, or habit?"
        )

        Spacer(modifier = Modifier.height(10.dp))

        ReflectionPromptCard(
            icon = Icons.Default.Air,
            prompt = "Take 3 deep breaths before opening feeds.",
            subtitle = "Give your brain a 10-second pause window."
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun BreathingExerciseCard(
    isActive: Boolean,
    secondsRemaining: Int,
    completedSessions: Int,
    onStart: () -> Unit,
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.SelfImprovement,
                    contentDescription = "Mindful Breathing",
                    tint = SageGreenPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Mindful Breathing Pause",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Animated Breathing Circle
            val infiniteTransition = rememberInfiniteTransition(label = "breathing")
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.85f,
                targetValue = 1.15f,
                animationSpec = infiniteRepeatable(
                    animation = tween(3000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .scale(if (isActive) scale else 1.0f)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = CircleShape
                        )
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isActive) "${secondsRemaining}s" else "10s",
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Bold,
                        color = SageGreenDark
                    )
                    Text(
                        text = if (isActive) "Breathe in... out..." else "Pause",
                        style = MaterialTheme.typography.labelMedium,
                        color = SageGreenDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onStart,
                enabled = !isActive,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SageGreenPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (isActive) "Breathing in progress..." else "Start 10s Pause")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "$completedSessions sessions completed today",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DailyIntentionCard(
    currentIntention: String,
    onSaveIntention: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var textState by remember(currentIntention) { mutableStateOf(currentIntention) }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Today's Intention",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SageGreenDark
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = textState,
                onValueChange = {
                    textState = it
                    onSaveIntention(it)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                placeholder = { Text("Set your mindful intention...") }
            )
        }
    }
}

@Composable
private fun ReflectionPromptCard(
    icon: ImageVector,
    prompt: String,
    subtitle: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = prompt,
                        tint = SageGreenPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column {
                Text(
                    text = prompt,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(name = "Mindful Light Mode", showBackground = true)
@Preview(name = "Mindful Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MindfulScreenPreview() {
    ScrollStopTheme {
        MindfulScreen(
            uiState = MindfulUiState(),
            onStartBreathing = {},
            onUpdateIntention = {}
        )
    }
}
