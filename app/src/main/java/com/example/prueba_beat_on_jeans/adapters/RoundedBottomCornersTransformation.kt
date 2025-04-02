package com.example.prueba_beat_on_jeans.adapters
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import coil.size.Size
import coil.transform.Transformation

class RoundedBottomCornersTransformation(private val radius: Float) : Transformation {

    override val cacheKey: String = "RoundedBottomCornersTransformation(radius=$radius)"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val output = input.config?.let { Bitmap.createBitmap(input.width, input.height, it) }
        val canvas = output?.let { Canvas(it) }
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        // Redondeamos solo las esquinas inferiores
        val rectF = RectF(0f, 0f, input.width.toFloat(), input.height.toFloat())
        val roundRect = RectF(0f, input.height - radius, input.width.toFloat(), input.height.toFloat())

        // Dibuja la imagen original en el canvas
        if (canvas != null) {
            canvas.drawBitmap(input, 0f, 0f, paint)
        }

        // Redondear solo las esquinas inferiores
        if (canvas != null) {
            canvas.drawRoundRect(roundRect, radius, radius, paint)
        }

        return output!!
    }
}
