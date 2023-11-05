package com.ibrahimcanerdogan.selfiesegmentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.util.AttributeSet
import android.view.SurfaceView

class DrawOverlay(
    context: Context,
    attributeSet: AttributeSet
) : SurfaceView(context, attributeSet) {

    var segment : Bitmap? = null

    override fun onDraw(canvas: Canvas?) {
        segment?.let {
            val matrix = Matrix()
            matrix.postScale(-1f, 1f, it.width / 2f, it.height / 2f)
            val reverseBitmap = Bitmap.createBitmap(it, 0, 0, it.width, it.height, matrix, true)
            canvas?.drawBitmap(
                reverseBitmap,
                50f,
                100f,
                null
            )
        }
    }
}