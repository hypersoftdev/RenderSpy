package com.hypersoft.performance.renderspy

import android.app.Activity
import android.app.Application
import android.view.View
import com.base.render.monitoring.AppTtiListener
import com.hypersoft.renderspy.rendering.ActivityFrameMetricsTracker
import com.hypersoft.renderspy.startup.AppStartupTimeTracker
import com.hypersoft.renderspy.tti.BaseTtiTracker
import com.hypersoft.renderspy.tti.ViewTtiTracker
import com.hypersoft.renderspy.tti.helper.ActivityTtfrHelper
import com.hypersoft.performance.renderspy.monitoring.ActivityFrameMetricsListener
import com.hypersoft.performance.renderspy.monitoring.AppStartupTimeListener

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
