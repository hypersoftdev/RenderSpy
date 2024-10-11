package com.hypersoft.performance.renderspy.monitoring

import android.app.Activity
import android.util.Log
import android.util.SparseIntArray
import com.hypersoft.renderspy.rendering.ActivityFrameMetricsTracker
import com.hypersoft.renderspy.rendering.RenderingMetricsMapper


internal object ActivityFrameMetricsListener : ActivityFrameMetricsTracker.Listener {

    override fun onFramesMetricsReady(
        activity: Activity,
        frameMetrics: Array<SparseIntArray>,
        foregroundTime: Long?
    ) {
        val activityName = activity.javaClass.simpleName
        val data = RenderingMetricsMapper.toRenderingMetrics(frameMetrics, foregroundTime) ?: return

        Log.d("PerfSuite", "Frame metrics for [$activityName] are collected: $data")
    }
}