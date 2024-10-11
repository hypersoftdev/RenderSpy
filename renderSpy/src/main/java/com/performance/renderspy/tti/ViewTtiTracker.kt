package com.performance.renderspy.tti

import android.view.View
import com.performance.renderspy.internal.doOnNextDraw

class ViewTtiTracker (private val tracker: BaseTtiTracker) {

    /**
     * Call this method immediately on screen creation as early as possible
     *
     * @param screen - unique screen identifier
     */
     fun onScreenCreated(screen: String) {
        tracker.onScreenCreated(screen)
    }

    /**
     * Call this when screen View is ready but it is not drawn yet
     *
     * @param screen - unique screen identifier
     * @param rootView - root view of the screen, metric is ready when this view is next drawn
     */
     fun onScreenViewIsReady(screen: String, rootView: View) {
        if (tracker.isScreenEnabledForTracking(screen)) {
            rootView.doOnNextDraw { tracker.onScreenViewIsReady(screen) }
        }
    }

    /**
     * Call this when the screen View is ready for user interaction.
     * Only the first call after screen creation is considered, repeat calls are ignored
     *
     * @see BaseTtiTracker.onScreenIsUsable
     *
     * @param screen - unique screen identifier
     * @param rootView - root view of the screen, metric is ready when this view is next drawn
     *
     *
     */
     fun onScreenIsUsable(screen: String, rootView: View) {
        if (tracker.isScreenEnabledForTracking(screen)) {
            rootView.doOnNextDraw { tracker.onScreenIsUsable(screen) }
        }
    }

    /**
     * Call this when user leaves the screen.
     *
     * This prevent us from tracking cheap screen transitions (e.g. back navigation,
     * when the screen is already created in memory), so we're able to track
     * only real screen creation performance, removing outliers
     */
     fun onScreenStopped(screen: String) {
        tracker.onScreenStopped(screen)
    }
}