package com.base.render.monitoring

import android.util.Log
import com.performance.renderspy.Constants.TAGS
import com.performance.renderspy.tti.BaseTtiTracker


object AppTtiListener : BaseTtiTracker.Listener {

    override fun onScreenCreated(screen: String) {}

    override fun onFirstFrameIsDrawn(screen: String, duration: Long) {
        Log.d(TAGS, "$screen - TTFR = ${duration}ms")
    }

    override fun onFirstUsableFrameIsDrawn(screen: String, duration: Long) {
        Log.d(TAGS, "$screen - TTI = ${duration}ms")
    }
}