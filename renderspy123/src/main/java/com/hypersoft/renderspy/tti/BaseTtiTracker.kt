package com.hypersoft.renderspy.tti

import com.hypersoft.renderspy.internal.nowMillis

class BaseTtiTracker(
    private val listener: Listener
) {

    private val screenCreationTimestamp = HashMap<String, Long>()

    /**
     * Call this method immediately on screen creation as early as possible
     *
     * @param screen - unique screen identifier
     * @param timestamp - the time the screen was created at.
     */
     fun onScreenCreated(screen: String, timestamp: Long = nowMillis()) {
        screenCreationTimestamp[screen] = timestamp
        listener.onScreenCreated(screen)
    }

    /**
     * Call this method when screen is rendered for the first time
     *
     * @param screen - unique screen identifier
     */
     fun onScreenViewIsReady(screen: String) {
        screenCreationTimestamp[screen]?.let { creationTimestamp ->
            val duration = nowMillis() - creationTimestamp
            listener.onFirstFrameIsDrawn(screen, duration)
        }
    }

    /**
     * Call this method when the screen is ready for user interaction
     * (e.g. all data is ready and meaningful content is shown).
     *
     * The method is optional, whenever it is not called TTI won't be measured
     *
     * @param screen - unique screen identifier
     */
     fun onScreenIsUsable(screen: String) {
        screenCreationTimestamp[screen]?.let { creationTimestamp ->
            val duration = nowMillis() - creationTimestamp
            listener.onFirstUsableFrameIsDrawn(screen, duration)
            screenCreationTimestamp.remove(screen)
        }
    }

    /**
     * Call this when user leaves the screen.
     *
     * This prevent us from producing outliers and avoid tracking cheap screen transitions
     * (e.g. back navigation, when the screen is already created in memory),
     * so we're able to track only real screen creation performance
     */
     fun onScreenStopped(screen: String) {
        screenCreationTimestamp.remove(screen)
    }

    /**
     * Returns true if the screen is still in the state of collecting metrics.
     * When result is false,that means that both TTFR/TTI metrics were already collected or
     * discarded for any reason
     */
     fun isScreenEnabledForTracking(screen: String): Boolean =
        screenCreationTimestamp.containsKey(screen)

    /**
     * Listener interface providing TTFR/TTI metrics when they're ready
     */
     interface Listener {

        /**
         * Called as early as possible after the screen [screen] is created.
         *
         * @param screen - screen key
         */
         fun onScreenCreated(screen: String)

        /**
         * Called when the very first screen frame is drawn
         *
         * @param screen - screen key
         * @param duration - elapsed time since screen's creation till the first frame is drawn
         */
         fun onFirstFrameIsDrawn(screen: String, duration: Long)

        /**
         * Called when the first usable/meaningful screen frame is drawn
         *
         * @param screen - screen key
         * @param duration - elapsed time since screen's creation till the usable frame is drawn
         */
         fun onFirstUsableFrameIsDrawn(screen: String, duration: Long)
    }
}