package com.example.lab2

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class MyView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var bitmap: Bitmap = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_8888)
    private val bitmapCanvas: Canvas = Canvas(bitmap)
    private val paint: Paint = Paint()
    private val rect: Rect by lazy { Rect(0, 0, width, height) }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, null, rect, paint)
    }

    fun drawCircle(color: Color) {
        paint.color = Color.WHITE
        bitmapCanvas.drawRect(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat(), paint)
        paint.color = color.toArgb()
        bitmapCanvas.drawCircle(0.5f * bitmap.width, 0.5f * bitmap.height, 0.25f * bitmap.width, paint)
        invalidate()
    }

    fun getBitmap(): Bitmap {
        return bitmap
    }

    fun restoreBitmap(newBitmap: Bitmap) {
        bitmap = newBitmap.copy(Bitmap.Config.ARGB_8888, true)
        bitmapCanvas.setBitmap(bitmap)
        invalidate()
    }
}