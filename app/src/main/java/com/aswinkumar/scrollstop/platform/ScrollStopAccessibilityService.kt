package com.aswinkumar.scrollstop.platform

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import com.aswinkumar.scrollstop.domain.engine.PatternEngine
import com.aswinkumar.scrollstop.domain.engine.QuestionEngine

class ScrollStopAccessibilityService : AccessibilityService() {
    private lateinit var overlayController: OverlayController
    private val patternEngine = PatternEngine(
        setOf(
            "com.instagram.android",
            "com.google.android.youtube",
            "com.zhiliaoapp.musically",
            "com.twitter.android"
        )
    )
    private val questionEngine = QuestionEngine()

    override fun onServiceConnected() {
        overlayController = OverlayController(this)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName = event?.packageName?.toString() ?: return
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED &&
            patternEngine.isMonitored(packageName) &&
            ::overlayController.isInitialized
        ) {
            overlayController.showQuestion(questionEngine.questionFor(packageName))
        }
    }

    override fun onInterrupt() {
        if (::overlayController.isInitialized) overlayController.hide()
    }

    override fun onDestroy() {
        if (::overlayController.isInitialized) overlayController.hide()
        super.onDestroy()
    }
}
