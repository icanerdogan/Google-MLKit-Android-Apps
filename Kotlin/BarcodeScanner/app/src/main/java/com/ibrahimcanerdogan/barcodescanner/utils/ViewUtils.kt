package com.ibrahimcanerdogan.barcodescanner.utils

import android.util.DisplayMetrics
import androidx.camera.core.AspectRatio
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object ViewUtils {

    fun DisplayMetrics.getAspectRatio() : Int {
        val ratio43Value = 4.0 / 3.0
        val ratio169Value = 16.0 / 9.0

        val previewRatio = max(this.widthPixels, this.heightPixels).toDouble() / min(this.widthPixels, this.heightPixels)

        if (abs(previewRatio - ratio43Value) <= abs(previewRatio - ratio169Value)) {
            return AspectRatio.RATIO_4_3
        }
        return AspectRatio.RATIO_16_9
    }
}