package com.aswinkumar.scrollstop.platform

import android.content.Context
import android.graphics.Color
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.TextView

class OverlayController(context: Context) {
    private val appContext = context.applicationContext
    private val windowManager =
        appContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var overlay: View? = null

    fun showQuestion(question: String) {
        if (overlay != null) return
        val view = TextView(appContext).apply {
            text = question
            setTextColor(Color.WHITE)
            setBackgroundColor(Color.rgb(35, 55, 45))
            setPadding(48, 32, 48, 32)
        }
        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.CENTER
        }
        windowManager.addView(view, params)
        overlay = view
    }

    fun hide() {
        overlay?.let(windowManager::removeView)
        overlay = null
    }
}
