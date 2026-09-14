package com.aswinkumar.scrollstop.domain.engine

import com.aswinkumar.scrollstop.data.local.entity.InterventionSessionEntity
import com.aswinkumar.scrollstop.data.local.dao.InterventionSessionDao
import com.aswinkumar.scrollstop.data.local.entity.UserPreferenceEntity
import java.util.Calendar

class InterventionDecisionEngine(
    private val interventionSessionDao: InterventionSessionDao,
    private val userPreferences: () -> UserPreferenceEntity?
) {
    suspend fun shouldTrigger(
        packageName: String,
        foregroundMinutes: Long,
        activeSessionKey: String
    ): Boolean {
        val prefs = userPreferences() ?: UserPreferenceEntity()
        val targetApps = prefs.targetedApps
            .split(",")
            .map { it.trim() }
            .filter { it.isNotBlank() }
            .toSet()

        if (packageName !in targetApps) return false
        if (foregroundMinutes < prefs.interventionThresholdMinutes.toLong()) return false
        if (isQuietHours(prefs)) return false
        if (interventionSessionDao.findBySessionKey(activeSessionKey) != null) return false
        return true
    }

    suspend fun recordIntervention(
        packageName: String,
        startTime: Long,
        endTime: Long,
        thresholdMinutes: Int,
        sessionKey: String,
        userAction: String = "PENDING",
        reflectionText: String? = null
    ) {
        interventionSessionDao.insertSession(
            InterventionSessionEntity(
                packageName = packageName,
                startTime = startTime,
                endTime = endTime,
                triggerType = "usage",
                interventionType = "pause",
                userAction = userAction,
                sessionKey = sessionKey,
                thresholdMinutes = thresholdMinutes,
                reflectionText = reflectionText
            )
        )
    }

    suspend fun updateUserAction(sessionKey: String, userAction: String, reflectionText: String? = null) {
        interventionSessionDao.updateOutcome(sessionKey, userAction, reflectionText)
    }

    private fun isQuietHours(preferences: UserPreferenceEntity): Boolean {
        val now = Calendar.getInstance()
        val currentMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)
        val startMinutes = parseTime(preferences.quietHoursStart)
        val endMinutes = parseTime(preferences.quietHoursEnd)

        return if (startMinutes < endMinutes) {
            currentMinutes in startMinutes until endMinutes
        } else {
            currentMinutes >= startMinutes || currentMinutes < endMinutes
        }
    }

    private fun parseTime(value: String): Int {
        val parts = value.split(":")
        if (parts.size != 2) return 0
        val hour = parts[0].toIntOrNull() ?: 0
        val minute = parts[1].toIntOrNull() ?: 0
        return hour * 60 + minute
    }
}
