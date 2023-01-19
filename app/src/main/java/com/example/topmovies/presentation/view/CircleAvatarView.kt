package com.example.topmovies.presentation.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.example.topmovies.R
import kotlin.math.min
import kotlin.properties.Delegates.notNull

class CircleAvatarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private var axisY by notNull<Int>()
    private var axisX by notNull<Int>()
    private var radius by notNull<Float>()
    private val density by lazy { resources.displayMetrics.density }
    private var imageBitmap: Bitmap? = null
    private val bitmapRect by lazy { RectF() }
    private val destinationRect by lazy { RectF() }
    private val bitmapMatrix by lazy { Matrix() }
    private lateinit var label: String
    private val circlePaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }
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
                        R.styleable.CircleAvatarView_src, R.drawable.def_profile_image
                    )
                )
                if (imageBitmap != null) setMatrix()
            } finally {
                typedArray.recycle()
            }
        }
    }

    private fun setMatrix() {
        if (!destinationRect.isEmpty) {
            bitmapMatrix.setRectToRect(bitmapRect, destinationRect, Matrix.ScaleToFit.CENTER)
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
        axisX = width
        axisY = height
        radius = min(width, height) / 2f
        labelPaint.textSize = min(width.toFloat(), height.toFloat())
        destinationRect.set(0f, 0f, width.toFloat(), height.toFloat())
        setMatrix()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.apply {
            drawCircle(axisX / 2f, axisY / 2f, radius, circlePaint)
            imageBitmap?.let { drawBitmap(it, bitmapMatrix, bitmapPaint) }
            drawText(label, axisX / 2f, setHeightLabel(axisY / 2f), labelPaint)
        }
    }

    fun setLabel(name: String) {
        label = name.first().uppercase()
        invalidate()
    }

    fun setAvatarImage(orgBitmap: Bitmap) {
        imageBitmap = orgBitmap
        bitmapRect.set(0f, 0f, imageBitmap!!.width.toFloat(), imageBitmap!!.height.toFloat())
        setMatrix()
        invalidate()
    }

    private fun setHeightLabel(height: Float): Float {
        return height - ((labelPaint.descent() + labelPaint.ascent()) / 2)
    }

    companion object {
        private const val DEF_SIZE_VIEW = 200
    }
}
