package com.itheamc.meatprocessing.utilities;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

public class PaintUtility {

     /*
    Function to draw text, line, circle and rectangle
     */

    // Function to draw text
    public static void drawText(Canvas canvas, float x, float y, Paint.Align align, Typeface typeface, int textStyle, float textSize, int color, String text) {
        Paint paint = new Paint();
        paint.setTextAlign(align);
        paint.setTypeface(Typeface.create(typeface, textStyle));
        paint.setColor(color);
        paint.setTextSize(textSize);
        canvas.drawText(text, x, y, paint);
    }

    // Function to draw text in various angles
    public static void drawText(Canvas canvas, float x, float y, Paint.Align align, Typeface typeface, int textStyle, float textSize, float degree,  int color, String text) {
        Paint paint = new Paint();
        paint.setTextAlign(align);
        paint.setTypeface(Typeface.create(typeface, textStyle));
        paint.setColor(color);
        paint.setTextSize(textSize);
        canvas.save();
        canvas.rotate(degree, x, y);
        canvas.drawText(text, x, y, paint);
        canvas.restore();

    }


    // Function to draw lines
    public static void drawLine(Canvas canvas, float startX, float startY, float stopX, float stopY, float strokeWidth, int color) {
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(strokeWidth);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    // Fumction to draw circle
    public static void drawCircle(Canvas canvas, float cX, float cY, float radius) {
        Paint paint = new Paint();
        canvas.drawCircle(cX, cY, radius, paint);
    }

    // Function to draw rectangle
    public static void drawRectangle(Canvas canvas, float left, float right, float top, float bottom) {
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        canvas.drawRect(left, top, right, bottom, paint);

    }
}
