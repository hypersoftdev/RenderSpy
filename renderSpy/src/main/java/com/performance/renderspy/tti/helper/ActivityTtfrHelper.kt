package com.performance.renderspy.tti.helper

import android.app.Activity
import android.app.Application
import android.app.Application.ActivityLifecycleCallbacks
import android.os.Bundle
import com.performance.renderspy.tti.ViewTtiTracker


/**
 * This class helps to automatically track TTFR metric for every activity by handling
 * [android.app.Application.ActivityLifecycleCallbacks]
 *
 * @param tracker TTI tracker instance
 * @param screenNameProvider function used to generate unique screen name/identifier for activity.
 *      If it returns null, then activity won't be tracked.
 *      By default it uses the implementation based on Activity's class name
 */

class ActivityTtfrHelper (
    private val tracker: ViewTtiTracker,
    private val screenNameProvider: (Activity) -> String? = { it.javaClass.name }
) : ActivityLifecycleCallbacks {

     companion object {

        /**
         * Registers [ActivityTtfrHelper] instance with the app as
         * [android.app.Application.ActivityLifecycleCallbacks] to collect TTFR metrics for
         * every activity
         *
         * Call this method at the app startup, before the first activity is created
         *
         * @param application current [Application] instance
         * @param tracker configured for the app [ViewTtiTracker] instance
         */
        @JvmStatic
         fun register(application: Application, tracker: ViewTtiTracker) {
            val activityHelper = ActivityTtfrHelper(tracker)
            application.registerActivityLifecycleCallbacks(activityHelper)
        }
    }

    override fun onActivityPreCreated(activity: Activity, savedInstanceState: Bundle?) {
        val screenKey = screenNameProvider(activity) ?: return
        tracker.onScreenCreated(screenKey)
    }

    override fun onActivityStarted(activity: Activity) {
        val screenKey = screenNameProvider(activity) ?: return
        val rootView = activity.window.decorView
        tracker.onScreenViewIsReady(screenKey, rootView)
    }

    override fun onActivityStopped(activity: Activity) {
        val screenKey = screenNameProvider(activity) ?: return
        tracker.onScreenStopped(screenKey)
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) { }
    override fun onActivityResumed(activity: Activity) { }
    override fun onActivityPaused(activity: Activity) { }
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) { }
    override fun onActivityDestroyed(activity: Activity) { }


}
