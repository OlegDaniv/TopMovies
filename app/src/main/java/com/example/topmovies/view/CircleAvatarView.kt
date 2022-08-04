package com.example.topmovies.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.topmovies.R
import com.example.topmovies.unit.DEF_SIZE_VIEW
import kotlin.math.min
import kotlin.properties.Delegates.notNull

class CircleAvatarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private lateinit var label: String
    private lateinit var imageBitmap: Bitmap
    private var viewCenter by notNull<Float>()
    private var radius by notNull<Float>()
    private val density by lazy { resources.displayMetrics.density }
    private val bitmapRect by lazy { RectF() }
    private val destinationRect by lazy { RectF() }
    private val bitmapMatrix by lazy { Matrix() }
    private val circlePaint by lazy { Paint(Paint.ANTI_ALIAS_FLAG) }
    private val bitmapPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        }
    }
    private val labelPaint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.argb(170, 255, 255, 255)
            textAlign = Paint.Align.CENTER
            xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)
        }
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.CircleAvatarView, 0, 0)
            try {
                label = typedArray.getString(R.styleable.CircleAvatarView_text) ?: ""
                imageBitmap = BitmapFactory.decodeResource(
                    resources, typedArray.getResourceId(
                        R.styleable.CircleAvatarView_src, R.drawable.empty_image
                    )
                )
            } finally {
                typedArray.recycle()
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredWidth = (DEF_SIZE_VIEW * density).toInt()
        val desiredHeight = (DEF_SIZE_VIEW * density).toInt()
        val resolveWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val resolveHeight = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        viewCenter = min(width.toFloat(), height.toFloat()) / 2
        radius = viewCenter
        labelPaint.textSize = min(width.toFloat(), height.toFloat())
        destinationRect.set(0f, 0f, width.toFloat(), height.toFloat())
        bitmapMatrix.setRectToRect(bitmapRect, destinationRect, Matrix.ScaleToFit.CENTER)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            drawARGB(0, 255, 255, 255)
            drawCircle(viewCenter, viewCenter, radius, circlePaint)
            drawBitmap(imageBitmap, bitmapMatrix, bitmapPaint)
            drawText(label, viewCenter, positionLabel(viewCenter), labelPaint)
        }
    }

    fun setLabel(name: String) {
        label = name.first().uppercase()
        invalidate()
    }

    fun setAvatarImage(orgBitmap: Bitmap) {
        imageBitmap = orgBitmap
        bitmapRect.set(0f, 0f, imageBitmap.width.toFloat(), imageBitmap.height.toFloat())
        if (!destinationRect.isEmpty) {
            bitmapMatrix.setRectToRect(bitmapRect, destinationRect, Matrix.ScaleToFit.CENTER)
            invalidate()
        }
    }

    private fun positionLabel(height: Float): Float {
        return height - ((labelPaint.descent() + labelPaint.ascent()) / 2)
    }
}
