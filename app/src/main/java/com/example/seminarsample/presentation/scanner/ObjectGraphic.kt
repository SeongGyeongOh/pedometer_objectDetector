package com.example.seminarsample.presentation.scanner

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import com.example.seminarsample.utils.GraphicOverlay
import com.google.mlkit.vision.objects.DetectedObject

class ObjectGraphic(
    overlay: GraphicOverlay,
    private val obj: DetectedObject,
    private val objLabel: String,
    private val imageRect: Rect
) : GraphicOverlay.Graphic(overlay) {

    private var rectPaint = Paint().apply {
        color = TEXT_COLOR
        style = Paint.Style.STROKE
        strokeWidth = STROKE_WIDTH
    }

    private var barcodePaint = Paint().apply {
        color = TEXT_COLOR
        textSize = TEXT_SIZE
    }


    companion object {
        private const val TEXT_COLOR = Color.WHITE
        private const val TEXT_SIZE = 54.0f
        private const val STROKE_WIDTH = 4.0f
        private const val ROUND_RECT_CORNER = 4f
    }

    override fun draw(canvas: Canvas?) {
        obj.boundingBox.let { box ->
            // Draws the bounding box around the object block.
            val rect = calculateRect(
                imageRect.height().toFloat(),
                imageRect.width().toFloat(),
                box
            )
            canvas?.drawRoundRect(rect, ROUND_RECT_CORNER, ROUND_RECT_CORNER, rectPaint)

            // Renders the barcode at the bottom of the box.
            canvas?.drawText(objLabel, rect.centerX(), rect.bottom, barcodePaint)
        }
    }
}