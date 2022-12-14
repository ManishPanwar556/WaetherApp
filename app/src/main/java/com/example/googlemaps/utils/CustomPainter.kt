package com.example.googlemaps.utils

import android.content.Context
import android.graphics.Canvas
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension

class CustomPainter(
    context: Context,
    @Dimension(unit = Dimension.PX) width: Int,
    @Dimension(unit = Dimension.PX) height: Int,
    private val painter: Painter
) : View(context) {

    init {

        layoutParams = ViewGroup.LayoutParams(width, height)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            painter.paint(canvas)
        }
    }
}