package com.example.topmovies.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.topmovies.R
import kotlin.math.min

class CircleAvatarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {
    private var positionHeight: Float = 0.0f
    private var positionWidth = 0.0f
    private var radius: Float = 0.0f
    private var text: String = ""
    private lateinit var imageBitmap: Bitmap
    private var diameter: Int = 0
    private val destiny = context.resources.displayMetrics.density

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.CircleAvatarView, 0, 0)
            try {
                text = typedArray.getString(R.styleable.CircleAvatarView_text) ?: ""
                imageBitmap = BitmapFactory.decodeResource(
                    context.resources, typedArray.getResourceId(
                        R.styleable.CircleAvatarView_src, R.drawable.empty_avatar
                    )
                )
            } finally {
                typedArray.recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = (200 * destiny).toInt()
        val desiredHeight = (200 * destiny).toInt()
        val resolveWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val resolveHeight = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        positionWidth = (width / 2).toFloat()
        positionHeight = (height / 2).toFloat()
        diameter = min(width, height)
        radius = diameter / 2.0f
        imageBitmap = getResizedBitmap(imageBitmap, diameter, diameter)
    }

    private fun getResizedBitmap(imageBitmap: Bitmap, width: Int, height: Int): Bitmap {
        val matrix = Matrix()
        val src = RectF(0f, 0f, imageBitmap.width.toFloat(), imageBitmap.height.toFloat())
        val dts = RectF(0f, 0f, width.toFloat(), height.toFloat())
        matrix.setRectToRect(src, dts, Matrix.ScaleToFit.FILL)
        return Bitmap.createBitmap(
            imageBitmap, 0, 0, imageBitmap.width, imageBitmap.height, matrix, true
        )
    }

    private val srcPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
    private val dtsPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val textPaint = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)
        color = Color.argb(170, 255, 255, 255)
        textAlign = Paint.Align.CENTER
    }

    private fun positionLatter(height: Float): Float {
        return height - ((textPaint.descent() + textPaint.ascent()) / 2)
    }

    fun addLetterInCircleAvatar(name: String) {
        text = name.first().uppercase()
        invalidate()
    }

    fun changeAvatarImage(resource: Bitmap) {
        imageBitmap = getResizedBitmap(resource, diameter, diameter)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.apply {
            drawARGB(0, 0, 0, 0)
            drawCircle(positionWidth, positionHeight, radius, dtsPaint)
            drawBitmap(imageBitmap, positionWidth - radius, positionHeight - radius, srcPaint)
            textPaint.textSize = diameter.toFloat()
            drawText(text, positionWidth, positionLatter(positionHeight), textPaint)
        }
    }
}