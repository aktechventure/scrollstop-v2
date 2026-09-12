package com.aswinkumar.scrollstop.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val title: String, val icon: ImageVector? = null) {
    object Onboarding : Screen("onboarding", "Onboarding")
    object Home : Screen("home", "Today", Icons.Default.Home)
    object Stats : Screen("stats", "Stats", Icons.Default.BarChart)
    object Mindful : Screen("mindful", "Mindful", Icons.Default.SelfImprovement)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)

    companion object {
        val bottomNavScreens = listOf(Home, Stats, Mindful, Settings)
    }
}
