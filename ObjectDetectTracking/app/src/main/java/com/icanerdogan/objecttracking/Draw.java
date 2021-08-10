package com.icanerdogan.objecttracking;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

public class Draw extends View {
    Paint rectPaint, textPaint;
    Rect rect;
    String text;
    String confidenceText;

    // ÇİZİM ÖZELLİKLERİ
    public Draw(Context context, Rect rect, String text, String confidenceText) {
        super(context);
        this.rect = rect;
        this.text = text;
        this.confidenceText = confidenceText;

        // Dikdörtgen Rengi
        rectPaint = new Paint();
        rectPaint.setColor(Color.GREEN);
        rectPaint.setStrokeWidth(10f);
        rectPaint.setStyle(Paint.Style.STROKE);

        // Yazıların Rengi
        textPaint = new Paint();
        textPaint.setColor(Color.GREEN);
        textPaint.setStrokeWidth(50f);
        textPaint.setTextSize(40f);
        textPaint.setStyle(Paint.Style.FILL);
    }

    // CANVAS İLE EKRANA ÇİZDİRME
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText(text, rect.centerX(), rect.centerY(), textPaint);
        canvas.drawText(confidenceText, rect.centerX(), rect.centerY()+40, textPaint);
        canvas.drawRect(rect.left, rect.top, rect.right, rect.bottom, rectPaint);
    }
}
