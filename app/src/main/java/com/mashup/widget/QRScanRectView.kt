package com.mashup.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.mashup.R

class QRScanRectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val bitmap = Bitmap.createBitmap(
        width, height, Bitmap.Config.ARGB_8888
    )

    private val transparentPaint = Paint().apply {
        color = Color.TRANSPARENT
        isAntiAlias = true
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OUT)
    }

    private val bgPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.default_bg_qr_scan)
        isAntiAlias = true
    }

    private val scanRectHorizontalMargin = context.resources.getDimension(
        R.dimen.margin_qr_scan_rect_horizontal
    ).toInt()

    private val scanRectTopMargin = context.resources.getDimension(
        R.dimen.margin_qr_scan_rect_top
    ).toInt()

    // ScanRect Size
    private var scanRectWidth = 0
    private var scanRectHeight = 0

    // ScanRect Position
    private var scanRectLeft = 0
    private var scanRectRight = 0
    private var scanRectTop = 0
    private var scanRectBottom = 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        scanRectWidth = widthSize - paddingLeft - paddingRight - (2 * scanRectHorizontalMargin)
        scanRectHeight = scanRectWidth

        setMeasuredDimension(widthSize, heightSize)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        scanRectLeft = (left.toFloat() + scanRectHorizontalMargin).toInt()
        scanRectRight = (right.toFloat() - scanRectHorizontalMargin).toInt()
        scanRectTop = scanRectTopMargin
        scanRectBottom = (scanRectTop + scanRectHeight)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val osCanvas = Canvas(bitmap)

        val outerRectangle =
            RectF(0f, 0f, width.toFloat(), height.toFloat())
        osCanvas.drawRect(outerRectangle, bgPaint)

        osCanvas.drawRect(
            RectF(
                scanRectLeft.toFloat(),
                scanRectTop.toFloat(),
                scanRectRight.toFloat(),
                scanRectBottom.toFloat()
            ),
            transparentPaint
        )
    }
}  