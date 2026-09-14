package com.aswinkumar.scrollstop.platform

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class ScrollStopAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Event handling will be added with the feed intervention feature.
    }

    override fun onInterrupt() = Unit
}
