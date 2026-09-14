package com.aswinkumar.scrollstop.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.aswinkumar.scrollstop.data.local.ScrollStopDatabase
import com.aswinkumar.scrollstop.data.local.entity.AppUsageEntity
import com.aswinkumar.scrollstop.data.local.entity.InsightEntity
import com.aswinkumar.scrollstop.data.local.entity.InterventionSessionEntity
import com.aswinkumar.scrollstop.data.local.entity.ReflectionEntity
import com.aswinkumar.scrollstop.data.local.entity.UserPreferenceEntity
import com.aswinkumar.scrollstop.data.repository.SettingsRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.Before

class SettingsDeletionAndResetTest {

    private lateinit var database: ScrollStopDatabase
    private lateinit var repository: SettingsRepositoryImpl

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, ScrollStopDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repository = SettingsRepositoryImpl(database)
    }

    @Test
    fun dataExists_delete_databaseEmpty() = runBlocking {
        database.appUsageDao().insertUsage(AppUsageEntity(
            date = "2026-09-14",
            timestamp = 1L,
            packageName = "com.google.android.youtube",
            appName = "YouTube",
            durationMillis = 120000L,
            sessionCount = 1
        ))
        database.reflectionDao().insertReflection(ReflectionEntity(
            questionId = "q1",
            timestamp = 2L,
            triggerType = "app",
            appPackageName = "com.google.android.youtube",
            answerId = "a1",
            signalType = "usage",
            dismissed = false
        ))
        database.insightDao().insertInsight(InsightEntity(
            type = "usage",
            message = "test insight",
            createdAt = 3L,
            evidenceStart = 1L,
            evidenceEnd = 3L,
            status = "ACTIVE"
        ))
        database.interventionSessionDao().insertSession(InterventionSessionEntity(
            packageName = "com.google.android.youtube",
            startTime = 4L,
            endTime = 5L,
            triggerType = "usage",
            interventionType = "pause",
            userAction = "CONTINUE"
        ))
        database.userPreferenceDao().insertOrUpdate(UserPreferenceEntity(
            id = 1,
            notificationsEnabled = true,
            quietHoursStart = "22:00",
            quietHoursEnd = "07:00",
            dailyQuestionLimit = 3,
            dailyTimeLimitMinutes = 60,
            targetedApps = "com.google.android.youtube,com.instagram.android",
            themePreference = "DARK"
        ))

        repository.clearAllUserData()

        assertTrue(database.appUsageDao().getAllUsageSync().isEmpty())
        assertTrue(database.reflectionDao().getReflectionsByTimeRangeSync(0L, Long.MAX_VALUE).isEmpty())
        assertTrue(database.insightDao().getAllInsights().toList().isEmpty())
        assertTrue(database.interventionSessionDao().getRecentSessions(50).toList().isEmpty())
        assertEquals(0, database.userPreferenceDao().getPreferenceSync()?.dailyTimeLimitMinutes ?: 0)
    }

    @Test
    fun deleteWithEmptyDatabase_isSafe() = runBlocking {
        repository.clearAllUserData()

        assertTrue(database.appUsageDao().getAllUsageSync().isEmpty())
        assertTrue(database.reflectionDao().getReflectionsByTimeRangeSync(0L, Long.MAX_VALUE).isEmpty())
        assertTrue(database.userPreferenceDao().getPreferenceSync() == null)
    }

    @Test
    fun deleteTwice_isSafe() = runBlocking {
        repository.clearAllUserData()
        repository.clearAllUserData()

        assertTrue(database.appUsageDao().getAllUsageSync().isEmpty())
        assertTrue(database.reflectionDao().getReflectionsByTimeRangeSync(0L, Long.MAX_VALUE).isEmpty())
        assertTrue(database.insightDao().getAllInsights().toList().isEmpty())
        assertTrue(database.interventionSessionDao().getRecentSessions(50).toList().isEmpty())
    }

    @Test
    fun foreignKey_orphanValidation_withDeleteAll_keepsDbClean() = runBlocking {
        database.userPreferenceDao().insertOrUpdate(UserPreferenceEntity(
            id = 1,
            targetedApps = "com.google.android.youtube,com.instagram.android"
        ))
        database.appUsageDao().insertUsage(AppUsageEntity(
            date = "2026-09-14",
            timestamp = 10L,
            packageName = "com.google.android.youtube",
            appName = "YouTube",
            durationMillis = 5000L,
            sessionCount = 1
        ))
        database.reflectionDao().insertReflection(ReflectionEntity(
            questionId = "q2",
            timestamp = 11L,
            triggerType = "app",
            appPackageName = "com.google.android.youtube",
            answerId = "a2",
            signalType = "usage",
            dismissed = false
        ))
        database.interventionSessionDao().insertSession(InterventionSessionEntity(
            packageName = "com.google.android.youtube",
            startTime = 12L,
            endTime = 13L,
            triggerType = "usage",
            interventionType = "pause",
            userAction = "LEAVE"
        ))

        repository.clearAllUserData()

        assertTrue(database.appUsageDao().getAllUsageSync().isEmpty())
        assertTrue(database.reflectionDao().getReflectionsByTimeRangeSync(0L, Long.MAX_VALUE).isEmpty())
        assertTrue(database.interventionSessionDao().getRecentSessions(50).toList().isEmpty())
    }

    @Test
    fun preferencesReset_toCleanDefaultState() = runBlocking {
        database.userPreferenceDao().insertOrUpdate(UserPreferenceEntity(
            id = 1,
            notificationsEnabled = false,
            quietHoursStart = "20:00",
            quietHoursEnd = "08:00",
            dailyQuestionLimit = 8,
            dailyTimeLimitMinutes = 90,
            targetedApps = "com.google.android.youtube,com.instagram.android",
            themePreference = "DARK"
        ))

        repository.clearAllUserData()

        val reset = database.userPreferenceDao().getPreferenceSync()
        assertTrue(reset == null)
    }

    @Test
    fun settingsUi_returnsCleanStateAfterDeletion() = runBlocking {
        database.userPreferenceDao().insertOrUpdate(UserPreferenceEntity(
            id = 1,
            notificationsEnabled = false,
            targetedApps = "com.google.android.youtube",
            themePreference = "DARK",
            dailyTimeLimitMinutes = 75
        ))

        repository.clearAllUserData()

        val preferences = database.userPreferenceDao().getPreferenceSync()
        assertTrue(preferences == null)
        val settings = repository.getSettings().toList().last()
        assertEquals(AppSettingsDefaults().dailyTimeLimitMinutes, settings.dailyTimeLimitMinutes)
    }
}

private class AppSettingsDefaults {
    val dailyTimeLimitMinutes: Int = 45
}
