package com.aswinkumar.scrollstop.domain.engine

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.aswinkumar.scrollstop.data.local.ScrollStopDatabase
import com.aswinkumar.scrollstop.data.local.entity.InterventionSessionEntity
import com.aswinkumar.scrollstop.data.local.entity.UserPreferenceEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InterventionDecisionEngineTest {

    private lateinit var database: ScrollStopDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        database = Room.inMemoryDatabaseBuilder(context, ScrollStopDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }

    @Test
    fun belowThreshold_doesNotTrigger() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(
                targetedApps = "com.google.android.youtube,com.instagram.android",
                interventionThresholdMinutes = 10,
                quietHoursStart = "22:00",
                quietHoursEnd = "07:00"
            )
        }

        val shouldTrigger = engine.shouldTrigger(
            packageName = "com.google.android.youtube",
            foregroundMinutes = 9,
            activeSessionKey = "youtube:session:1"
        )

        assertFalse(shouldTrigger)
    }

    @Test
    fun thresholdReached_andTargetEnabled_triggers() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(
                targetedApps = "com.google.android.youtube,com.instagram.android",
                interventionThresholdMinutes = 10,
                quietHoursStart = "22:00",
                quietHoursEnd = "07:00"
            )
        }

        val shouldTrigger = engine.shouldTrigger(
            packageName = "com.google.android.youtube",
            foregroundMinutes = 10,
            activeSessionKey = "youtube:session:2"
        )

        assertTrue(shouldTrigger)
    }

    @Test
    fun targetDisabled_neverTriggers() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(
                targetedApps = "com.google.android.youtube",
                interventionThresholdMinutes = 10,
                quietHoursStart = "22:00",
                quietHoursEnd = "07:00"
            )
        }

        assertFalse(
            engine.shouldTrigger(
                packageName = "com.instagram.android",
                foregroundMinutes = 10,
                activeSessionKey = "insta:session:1"
            )
        )
    }

    @Test
    fun targetEnabled_andThresholdReached_triggers() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(
                targetedApps = "com.google.android.youtube,com.instagram.android",
                interventionThresholdMinutes = 10,
                quietHoursStart = "22:00",
                quietHoursEnd = "07:00"
            )
        }

        assertTrue(
            engine.shouldTrigger(
                packageName = "com.instagram.android",
                foregroundMinutes = 10,
                activeSessionKey = "insta:session:2"
            )
        )
    }

    @Test
    fun quietHours_blocksTrigger() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(
                targetedApps = "com.google.android.youtube",
                interventionThresholdMinutes = 10,
                quietHoursStart = "00:00",
                quietHoursEnd = "23:59"
            )
        }

        assertFalse(
            engine.shouldTrigger(
                packageName = "com.google.android.youtube",
                foregroundMinutes = 10,
                activeSessionKey = "quiet:1"
            )
        )
    }

    @Test
    fun duplicateAccessibilityEvents_doNotCreateDuplicateSessions() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(
                targetedApps = "com.google.android.youtube",
                interventionThresholdMinutes = 10,
                quietHoursStart = "22:00",
                quietHoursEnd = "07:00"
            )
        }

        val sessionKey = "youtube:session:dup"
        val first = engine.shouldTrigger("com.google.android.youtube", 10L, sessionKey)
        assertTrue(first)
        engine.recordIntervention(
            packageName = "com.google.android.youtube",
            startTime = 100L,
            endTime = 200L,
            thresholdMinutes = 10,
            sessionKey = sessionKey
        )

        val second = engine.shouldTrigger("com.google.android.youtube", 10L, sessionKey)
        assertFalse(second)
    }

    @Test
    fun oneInterventionPerActiveSession() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(
                targetedApps = "com.google.android.youtube",
                interventionThresholdMinutes = 10,
                quietHoursStart = "22:00",
                quietHoursEnd = "07:00"
            )
        }

        assertTrue(
            engine.shouldTrigger("com.google.android.youtube", 10L, "session:one")
        )
        engine.recordIntervention(
            packageName = "com.google.android.youtube",
            startTime = 100L,
            endTime = 200L,
            thresholdMinutes = 10,
            sessionKey = "session:one"
        )
        assertFalse(
            engine.shouldTrigger("com.google.android.youtube", 10L, "session:one")
        )
    }

    @Test
    fun continueOutcome_recordsContinuation() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(targetedApps = "com.google.android.youtube")
        }

        val sessionKey = "session:continue"
        engine.recordIntervention(
            packageName = "com.google.android.youtube",
            startTime = 100L,
            endTime = 200L,
            thresholdMinutes = 10,
            sessionKey = sessionKey,
            userAction = "CONTINUE"
        )
        engine.updateUserAction(sessionKey, "CONTINUE")

        val saved = database.interventionSessionDao().findBySessionKey(sessionKey)
        assertEquals("CONTINUE", saved?.userAction)
    }

    @Test
    fun leaveOutcome_recordsInterruption() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(targetedApps = "com.google.android.youtube")
        }

        val sessionKey = "session:leave"
        engine.recordIntervention(
            packageName = "com.google.android.youtube",
            startTime = 100L,
            endTime = 200L,
            thresholdMinutes = 10,
            sessionKey = sessionKey,
            userAction = "LEAVE"
        )
        engine.updateUserAction(sessionKey, "LEAVE")

        val saved = database.interventionSessionDao().findBySessionKey(sessionKey)
        assertEquals("LEAVE", saved?.userAction)
    }

    @Test
    fun optionalReflection_persistsWithSession() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(targetedApps = "com.google.android.youtube")
        }

        val sessionKey = "session:reflect"
        engine.recordIntervention(
            packageName = "com.google.android.youtube",
            startTime = 100L,
            endTime = 200L,
            thresholdMinutes = 10,
            sessionKey = sessionKey,
            reflectionText = "I was checking updates"
        )
        engine.updateUserAction(sessionKey, "REFLECTION", "I was checking updates")

        val saved = database.interventionSessionDao().findBySessionKey(sessionKey)
        assertEquals("REFLECTION", saved?.userAction)
        assertEquals("I was checking updates", saved?.reflectionText)
    }

    @Test
    fun persistence_and_appRestart_preserve_session_state() = runBlocking {
        val engine = InterventionDecisionEngine(database.interventionSessionDao()) {
            UserPreferenceEntity(
                targetedApps = "com.google.android.youtube",
                quietHoursStart = "22:00",
                quietHoursEnd = "07:00",
                interventionThresholdMinutes = 10
            )
        }

        val sessionKey = "session:restart"
        engine.recordIntervention(
            packageName = "com.google.android.youtube",
            startTime = 100L,
            endTime = 200L,
            thresholdMinutes = 10,
            sessionKey = sessionKey,
            userAction = "CONTINUE"
        )

        val saved = database.interventionSessionDao().findBySessionKey(sessionKey)
        assertEquals("CONTINUE", saved?.userAction)
        assertEquals(10, saved?.thresholdMinutes)
    }
}
