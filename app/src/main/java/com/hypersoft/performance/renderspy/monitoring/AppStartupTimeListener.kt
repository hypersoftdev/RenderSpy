package com.hypersoft.performance.renderspy.monitoring

import android.app.Activity
import android.util.Log
import com.hypersoft.renderspy.Constants.TAGS
import com.hypersoft.renderspy.startup.AppStartupTimeTracker

internal object AppStartupTimeListener : AppStartupTimeTracker.Listener {

    override fun onColdStartupTimeIsReady(
        startupTime: Long,
        firstActivity: Activity,
        isActualColdStart: Boolean
    ) {
        Log.d(TAGS, "Startup time = ${startupTime}ms")
    }
}