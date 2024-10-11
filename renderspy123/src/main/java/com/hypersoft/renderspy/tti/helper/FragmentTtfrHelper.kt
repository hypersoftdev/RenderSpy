package com.hypersoft.renderspy.tti.helper

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.hypersoft.renderspy.tti.ViewTtiTracker

/**
 * This class helps to automatically track TTFR metric for every activity by handling
 * [android.app.Application.ActivityLifecycleCallbacks]
 *
 * @param tracker TTI tracker instance
 * @param screenNameProvider function used to generate unique screen name/identifier for activity.
 *      If it returns null, then activity won't be tracked.
 *      By default it uses the implementation based on Activity's class name
 */

class FragmentTtfrHelper (
    private val tracker: ViewTtiTracker,
    private val screenNameProvider: (Fragment) -> String? = { it.javaClass.name }
) : FragmentManager.FragmentLifecycleCallbacks() {

    override fun onFragmentPreCreated(
        fm: FragmentManager,
        fragment: Fragment,
        savedInstanceState: Bundle?
    ) {
        val screenKey = screenNameProvider(fragment) ?: return
        tracker.onScreenCreated(screenKey)
    }

    override fun onFragmentStarted(fm: FragmentManager, fragment: Fragment) {
        val screenKey = screenNameProvider(fragment) ?: return
        val rootView = fragment.view ?: return
        tracker.onScreenViewIsReady(screenKey, rootView)
    }

    override fun onFragmentStopped(fm: FragmentManager, fragment: Fragment) {
        val screenKey = screenNameProvider(fragment) ?: return
        tracker.onScreenStopped(screenKey)
    }
}