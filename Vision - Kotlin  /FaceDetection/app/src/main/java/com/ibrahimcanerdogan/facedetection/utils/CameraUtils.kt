package com.ibrahimcanerdogan.facedetection.utils

import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.RectF
import androidx.camera.core.CameraSelector
import com.ibrahimcanerdogan.facedetection.graphic.GraphicOverlay
import kotlin.math.ceil

object CameraUtils {

    private var mScale: Float? = null
    private var mOffsetX: Float? = null
    private var mOffsetY: Float? = null
    private var cameraSelector: Int = CameraSelector.LENS_FACING_FRONT

    fun calculateRect(
        overlay: GraphicOverlay<*>,
        height: Float,
        width: Float,
        boundingBoxT: Rect
    ) : RectF {

        // for land scape
        fun isLandScapeMode(): Boolean {
            return overlay.context.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
        }

        fun whenLandScapeModeWidth(): Float {
            return when (isLandScapeMode()) {
                true -> width
                false -> height
            }
        }

        fun whenLandScapeModeHeight(): Float {
            return when (isLandScapeMode()) {
                true -> height
                false -> width
            }
        }

        val scaleX = overlay.width.toFloat() / whenLandScapeModeWidth()
        val scaleY = overlay.height.toFloat() / whenLandScapeModeHeight()
        val scale = scaleX.coerceAtLeast(scaleY)
        this.mScale = scale

        // Calculate offset (we need to center the overlay on the target)
        val offsetX = (overlay.width.toFloat() - ceil(whenLandScapeModeWidth() * scale)) / 2.0f
        val offsetY = (overlay.height.toFloat() - ceil(whenLandScapeModeHeight() * scale)) / 2.0f

        this.mOffsetX = offsetX
        this.mOffsetY = offsetY

        val mappedBox = RectF().apply {
            left = boundingBoxT.right * scale + offsetX
            top = boundingBoxT.top * scale + offsetY
            right = boundingBoxT.left * scale + offsetX
            bottom = boundingBoxT.bottom * scale + offsetY
        }

        // for front mode
        if (isFrontMode()) {
            val centerX = overlay.width.toFloat() / 2
            mappedBox.apply {
                left = centerX + (centerX - left)
                right = centerX - (right - centerX)
            }
        }

        return mappedBox
    }

    private fun isFrontMode() = cameraSelector == CameraSelector.LENS_FACING_FRONT

    fun toggleSelector() {
        cameraSelector = if (cameraSelector == CameraSelector.LENS_FACING_BACK) CameraSelector.LENS_FACING_FRONT
        else CameraSelector.LENS_FACING_BACK
    }
}