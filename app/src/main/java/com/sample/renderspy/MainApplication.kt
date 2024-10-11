package com.sample.renderspy

import android.app.Activity
import android.app.Application
import android.view.View
import com.base.render.monitoring.AppTtiListener
import com.performance.renderspy.rendering.ActivityFrameMetricsTracker
import com.performance.renderspy.startup.AppStartupTimeTracker
import com.performance.renderspy.tti.BaseTtiTracker
import com.performance.renderspy.tti.ViewTtiTracker
import com.performance.renderspy.tti.helper.ActivityTtfrHelper
import com.sample.renderspy.monitoring.ActivityFrameMetricsListener
import com.sample.renderspy.monitoring.AppStartupTimeListener

class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

//        // setup startup time tracking
        AppStartupTimeTracker.register(this, AppStartupTimeListener)
//
//        // setup rendering performance tracking
        ActivityFrameMetricsTracker.register(this, ActivityFrameMetricsListener)
//
        // setup Activity TTI tracking
        ActivityTtfrHelper.register(this, viewTtiTracker)
    }
}

val ttiTracker = BaseTtiTracker(AppTtiListener)
val viewTtiTracker = ViewTtiTracker(ttiTracker)

fun Activity.reportIsUsable(contentView: View = this.window.decorView) {
    viewTtiTracker.onScreenIsUsable(this.javaClass.name, contentView)
}
