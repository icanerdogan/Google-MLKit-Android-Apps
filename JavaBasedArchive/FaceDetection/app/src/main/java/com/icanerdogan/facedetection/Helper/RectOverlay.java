package com.icanerdogan.facedetection.Helper;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic {

    private int mRectColor = Color.GREEN;
    private float mStrokeWidth = 4.0f;
    private Paint mRectPaint;
    private GraphicOverlay graphicOverlay;
    private Rect rect;

    public RectOverlay(GraphicOverlay overlay, Rect rect) {
        super(overlay);

        mRectPaint = new Paint();
        mRectPaint.setStrokeWidth(mStrokeWidth);
        mRectPaint.setStyle(Paint.Style.STROKE);
        mRectPaint.setColor(mRectColor);

        this.graphicOverlay = graphicOverlay;
        this.rect = rect;
    }

    @Override
    public void draw(Canvas canvas) {
        RectF rectF = new RectF(rect);

        rectF.left = translateX(rectF.left);
        rectF.right = translateX(rectF.right);
        rectF.top = translateX(rectF.top);
        rectF.bottom = translateX(rectF.bottom);

        canvas.drawRect(rectF, mRectPaint);
    }
}
