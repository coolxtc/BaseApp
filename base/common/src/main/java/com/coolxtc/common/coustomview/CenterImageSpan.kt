package com.coolxtc.common.coustomview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.style.ImageSpan
import java.lang.ref.WeakReference

/**
 * Created by xtc on 2018/4/25.
 */
class CenterImageSpan(context: Context, resourceId: Int, verticalAlignment: Int) : ImageSpan(context, resourceId, verticalAlignment) {

    private var mDrawableRef: WeakReference<Drawable>? = null

    private val cachedDrawable: Drawable
        get() {
            val wr = mDrawableRef
            var d = wr?.get()
            if (d == null) {
                d = drawable
                mDrawableRef = WeakReference(d)
            }
            return d!!
        }

    override fun getSize(paint: Paint, text: CharSequence, start: Int, end: Int,
                         fontMetricsInt: Paint.FontMetricsInt?): Int {
        val drawable = drawable
        val rect = drawable.bounds
        if (fontMetricsInt != null) {
            val fmPaint = paint.fontMetricsInt
            val fontHeight = fmPaint.descent - fmPaint.ascent
            val drHeight = rect.bottom - rect.top
            val centerY = fmPaint.ascent + fontHeight / 2

            fontMetricsInt.ascent = centerY - drHeight / 2
            fontMetricsInt.top = fontMetricsInt.ascent
            fontMetricsInt.bottom = centerY + drHeight / 2
            fontMetricsInt.descent = fontMetricsInt.bottom
        }
        return rect.right
    }

    override fun draw(canvas: Canvas, text: CharSequence, start: Int, end: Int, x: Float, top: Int, y: Int,
                      bottom: Int, paint: Paint) {
        val drawable = cachedDrawable
        canvas.save()
        val fmPaint = paint.fontMetricsInt
        val fontHeight = fmPaint.descent - fmPaint.ascent
        val centerY = y + fmPaint.descent - fontHeight / 2
        val transY = centerY - (drawable.bounds.bottom - drawable.bounds.top) / 2
        canvas.translate(x, transY.toFloat())
        drawable.draw(canvas)
        canvas.restore()
    }
}