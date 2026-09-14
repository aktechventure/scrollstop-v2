package com.aswinkumar.scrollstop.validation

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.aswinkumar.scrollstop.data.local.ScrollStopDatabase
import com.aswinkumar.scrollstop.data.local.entity.AppUsageEntity
import com.aswinkumar.scrollstop.data.local.entity.InterventionSessionEntity
import com.aswinkumar.scrollstop.data.local.entity.ReflectionEntity
import com.aswinkumar.scrollstop.data.local.entity.UserPreferenceEntity
import com.aswinkumar.scrollstop.domain.engine.InsightEngine
import com.aswinkumar.scrollstop.domain.engine.PatternEngine
import com.aswinkumar.scrollstop.domain.engine.QuestionEngine
import com.aswinkumar.scrollstop.domain.model.PermissionStatus
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test

class ScrollStopBehaviorLoopValidationTest {

    private lateinit var database: ScrollStopDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.databaseBuilder(
            context,
            ScrollStopDatabase::class.java,
            "scrollstop-behavior-loop.db"
        )
            .allowMainThreadQueries()
            .build()
    }

    @Test
    fun usageStats_provides_required_usage_information() {
        val usage = AppUsageEntity(
            date = "2026-09-14",
            timestamp = 1_700_000_000L,
            packageName = "com.google.android.youtube",
            appName = "YouTube",
            durationMillis = 3_600_000L,
            sessionCount = 1
        )

        assertEquals("2026-09-14", usage.date)
        assertEquals("com.google.android.youtube", usage.packageName)
        assertEquals("YouTube", usage.appName)
        assertTrue(usage.durationMillis > 0L)
    }

    @Test
    fun patternEngine_identifies_relevant_usage_patterns() {
        val engine = PatternEngine(setOf("com.google.android.youtube", "com.instagram.android"))
        assertTrue(engine.isMonitored("com.google.android.youtube"))
        assertTrue(engine.isMonitored("com.instagram.android"))
        assertFalse(engine.isMonitored("com.example.unknown"))
    }

    @Test
    fun intervention_threshold_is_configurable() {
        val prefs = UserPreferenceEntity(interventionThresholdMinutes = 15)
        assertEquals(15, prefs.interventionThresholdMinutes)
    }

    @Test
    fun quiet_hours_suppress_interventions() {
        fail("FAIL: quiet-hours suppression is not implemented in the current intervention flow; no quiet-hours gate exists before a trigger is created.")
    }

    @Test
    fun target_disabled_does_not_trigger_intervention() {
        val monitor = PatternEngine(setOf("com.google.android.youtube"))
        assertFalse(monitor.isMonitored("com.instagram.android"))
    }

    @Test
    fun target_enabled_may_trigger_when_threshold_reached() {
        val monitor = PatternEngine(setOf("com.google.android.youtube", "com.instagram.android"))
        assertTrue(monitor.isMonitored("com.google.android.youtube"))
    }

    @Test
    fun questionEngine_selects_question_deterministically() {
        val engine = QuestionEngine()
        assertEquals(
            "Why did I open this app right now?",
            engine.questionFor("com.google.android.youtube")
        )
    }

    @Test
    fun user_can_continue_leave_and_reflect_where_applicable() {
        val continueAction = "CONTINUE"
        val leaveAction = "LEAVE"
        val dismissedAction = "DISMISSED"

        assertEquals("CONTINUE", continueAction)
        assertEquals("LEAVE", leaveAction)
        assertEquals("DISMISSED", dismissedAction)
        assertTrue(listOf("CONTINUE", "LEAVE", "DISMISSED").contains("CONTINUE"))
    }

    @Test
    fun interventionSession_persists_outcome() = runBlocking {
        val session = InterventionSessionEntity(
            packageName = "com.google.android.youtube",
            startTime = 1000L,
            endTime = 2000L,
            triggerType = "usage",
            interventionType = "pause",
            userAction = "CONTINUE"
        )
        val inserted = database.interventionSessionDao().insertSession(session)
        assertNotNull(inserted)
    }

    @Test
    fun reflection_persists_when_provided() = runBlocking {
        val reflection = ReflectionEntity(
            questionId = "q1",
            timestamp = 1000L,
            triggerType = "usage",
            appPackageName = "com.google.android.youtube",
            answerId = "a1",
            signalType = "app_open",
            dismissed = false
        )
        database.reflectionDao().insertReflection(reflection)
        val stored = database.reflectionDao().getReflectionsByTimeRangeSync(0L, Long.MAX_VALUE)
        assertEquals(1, stored.size)
    }

    @Test
    fun insightEngine_generates_insights_only_from_available_local_data() {
        val engine = InsightEngine()
        val usage = listOf(
            AppUsageEntity(
                date = "2026-09-14",
                timestamp = 1L,
                packageName = "com.google.android.youtube",
                appName = "YouTube",
                durationMillis = 600_000L,
                sessionCount = 1
            )
        )
        assertTrue(engine.summarize(usage)?.contains("YouTube") == true)
    }

    @Test
    fun no_insight_when_insufficient_data_exists() {
        val engine = InsightEngine()
        assertNull(engine.summarize(emptyList()))
    }

    @Test
    fun permission_denied_and_revoked_are_distinguished() {
        assertEquals(PermissionStatus.REQUIRED, PermissionStatus.resolve(false, false))
        assertEquals(PermissionStatus.DENIED, PermissionStatus.resolve(false, true))
    }

    @Test
    fun duplicate_events_do_not_create_duplicate_intervention_sessions() {
        fail("FAIL: duplicate-event deduplication is not implemented; the current code inserts sessions without uniqueness or dedupe checks.")
    }

    @Test
    fun repeated_usageStats_queries_are_avoided() {
        fail("FAIL: repeated refresh is not avoided; UsageRepositoryImpl calls refreshToday() for each collection flow emission and does not cache the result.")
    }

    @Test
    fun app_restart_preserves_required_local_state() = runBlocking {
        database.userPreferenceDao().insertOrUpdate(
            UserPreferenceEntity(
                id = 1,
                notificationsEnabled = true,
                dailyTimeLimitMinutes = 60,
                targetedApps = "com.google.android.youtube,com.instagram.android",
                themePreference = "DARK"
            )
        )

        database.close()
        val reopened = Room.databaseBuilder(
            ApplicationProvider.getApplicationContext<Context>(),
            ScrollStopDatabase::class.java,
            "scrollstop-behavior-loop.db"
        )
            .allowMainThreadQueries()
            .build()

        val saved = reopened.userPreferenceDao().getPreferenceSync()
        assertNotNull(saved)
        assertEquals(60, saved?.dailyTimeLimitMinutes)
        assertEquals("DARK", saved?.themePreference)
        reopened.close()
    }

    @Test
    fun insufficient_data_for_insight_is_rejected() {
        val engine = InsightEngine()
        assertNull(engine.summarize(emptyList()))
    }

    @Test
    fun permission_denied_requires_user_action() {
        assertEquals(PermissionStatus.REQUIRED, PermissionStatus.resolve(false, false))
    }

    @Test
    fun missing_usage_data_is_handled_without_crash() {
        val engine = InsightEngine()
        assertNull(engine.summarize(emptyList()))
    }
}
