package com.example.googlemaps.utils

import android.graphics.*
import androidx.annotation.ColorInt

class GraphPainter(
    @ColorInt val colorInt: Int,
    private val startPointX: Float,
    private val startPointY: Float,
    private val endPointX: Float,
    private val endPointY: Float,
    private val temp:Float
) : Painter {

    override fun paint(canvas: Canvas) {
        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()
        val shapeBounds = RectFFactory.fromLTWH(0f, 0f, width, height)
        drawLine(canvas,shapeBounds)

    }

    private fun drawLine(canvas: Canvas, bounds: RectF) {
        val pLine: Paint = object : Paint() {
            init {
                style = Style.STROKE
                strokeWidth = 6f
                color = colorInt

                // Line color
            }
        }

        val pText = Paint().apply {
            color = Color.WHITE
            textSize = 40f
        }

        val curvePath = Path().apply {
            moveTo(startPointX, startPointY)
            lineTo(endPointX,endPointY)
            close()

        }
        canvas.drawPath(curvePath, pLine)
        canvas.drawText(temp.toString(),80f,80f,pText)

    }
}