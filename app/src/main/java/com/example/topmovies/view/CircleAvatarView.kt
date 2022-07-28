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

    private lateinit var text: String
    private lateinit var imageBitmap: Bitmap
    private var viewCenter = 0.0f
    private var radius = 0.0f
    private val destiny by lazy { resources.displayMetrics.density }
    private val bitmapRect = RectF()
    private val dstRect = RectF()
    private val mMatrix = Matrix()
    private val paintForCircle = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paintForBitmap = Paint().apply {
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }
    private val paintForText = Paint().apply {
        isAntiAlias = true
        color = Color.argb(170, 255, 255, 255)
        textAlign = Paint.Align.CENTER
        xfermode = PorterDuffXfermode(PorterDuff.Mode.LIGHTEN)
    }

    init {
        setLayerType(LAYER_TYPE_SOFTWARE, null)
        if (attributeSet != null) {
            val typedArray =
                context.obtainStyledAttributes(attributeSet, R.styleable.CircleAvatarView, 0, 0)
            try {
                text = typedArray.getString(R.styleable.CircleAvatarView_text) ?: ""
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
        val desiredWidth = (200 * destiny).toInt()
        val desiredHeight = (200 * destiny).toInt()
        val resolveWidth = resolveSize(desiredWidth, widthMeasureSpec)
        val resolveHeight = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(resolveWidth, resolveHeight)
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        viewCenter = min(width.toFloat(), height.toFloat()) / 2
        radius = viewCenter
        paintForText.textSize = min(width.toFloat(), height.toFloat())
        dstRect.set(0f, 0f, width.toFloat(), height.toFloat())
    }

    private fun positionLatter(height: Float): Float {
        return height - ((paintForText.descent() + paintForText.ascent()) / 2)
    }

    fun addLetterInCircleAvatar(name: String) {
        text = name.first().uppercase()
        invalidate()
    }

    fun changeAvatarImage(orgBitmap: Bitmap) {
        imageBitmap = orgBitmap
        bitmapRect.set(0f, 0f, imageBitmap.width.toFloat(), imageBitmap.height.toFloat())
        mMatrix.setRectToRect(bitmapRect, dstRect, Matrix.ScaleToFit.CENTER)
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.apply {
            drawARGB(0, 255, 255, 255)
            drawCircle(viewCenter, viewCenter, radius, paintForCircle)
            drawBitmap(imageBitmap, mMatrix, paintForBitmap)
            drawText(text, viewCenter, positionLatter(viewCenter), paintForText)
        }
    }
}
