package com.ibrahimcanerdogan.digitalinkrecognition.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.ibrahimcanerdogan.digitalinkrecognition.StrokeManager

class DrawView(context: Context, attributeSet: AttributeSet?) : View(context, attributeSet) {

    private var currentStrokePaint : Paint = Paint()
    private val canvasPaint : Paint = Paint(Paint.DITHER_FLAG)
    private val currentStroke : Path = Path()

    private var drawCanvas : Canvas? = null
    private lateinit var canvasBitmap : Bitmap

    init {
        currentStrokePaint.color = Color.BLACK
        currentStrokePaint.isAntiAlias = true
        currentStrokePaint.strokeWidth = STROKE_WIDTH_DP
        currentStrokePaint.style = Paint.Style.STROKE
        currentStrokePaint.strokeJoin = Paint.Join.ROUND
        currentStrokePaint.strokeCap = Paint.Cap.ROUND
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        val x = event.x
        val y = event.y

        when(action) {
            MotionEvent.ACTION_DOWN -> currentStroke.moveTo(x, y)
            MotionEvent.ACTION_MOVE -> currentStroke.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
                currentStroke.lineTo(x, y)
                drawCanvas?.drawPath(currentStroke, currentStrokePaint)
                currentStroke.reset()
            }
            else -> {}
        }
        StrokeManager.addNewTouchEvent(event)
        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBitmap(canvasBitmap, 0f, 0f, canvasPaint)
        canvas.drawPath(currentStroke, currentStrokePaint)
    }

    fun clear() {
        onSizeChanged(canvasBitmap.width, canvasBitmap.height, canvasBitmap.width, canvasBitmap.height)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)
        invalidate()
    }
    companion object {
        private const val STROKE_WIDTH_DP = 6.0f
    }
}