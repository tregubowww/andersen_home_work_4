package ru.tregubowww.andersen_home_work_4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorLong
import androidx.annotation.DimenRes
import androidx.core.content.withStyledAttributes
import java.util.Calendar
import kotlin.math.min

class WatchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultAttributeSet: Int = 0,
) : View(context, attrs, defaultAttributeSet) {


    private var colorHourHand = 0
    private var colorMinuteHand = 0
    private var colorSecondHand = 0
    private var colorClockFace = 0
    private var colorBackgroundClockFace = 0
    private var customSizeLineHourHand = 0
    private var customSizeLineMinuteHand = 0
    private var customSizeLineSecondHand = 0

    private lateinit var painterClockFace: Paint
    private lateinit var painterHourHand: Paint
    private lateinit var painterMinuteHand: Paint
    private lateinit var painterSecondHand: Paint
    private lateinit var painterBackgroundClockFace: Paint

    private var isStopWatch = true

    init {
        context.withStyledAttributes(attrs, R.styleable.WatchView) {
            colorHourHand = getColor(R.styleable.WatchView_colorHourHand, Color.BLACK)
            colorMinuteHand = getColor(R.styleable.WatchView_colorMinuteHand, Color.BLACK)
            colorSecondHand = getColor(R.styleable.WatchView_colorSecondHand, Color.RED)
            colorClockFace = getColor(R.styleable.WatchView_colorClockFaceHand, Color.BLACK)
            colorBackgroundClockFace = getColor(R.styleable.WatchView_colorBackgroundClockFaceHand, Color.WHITE)
            customSizeLineHourHand = getDimensionPixelSize( R.styleable.WatchView_lineHourHandSize, 0)
            customSizeLineMinuteHand = getDimensionPixelSize( R.styleable.WatchView_lineMinuteHandSize, 0)
            customSizeLineSecondHand = getDimensionPixelSize( R.styleable.WatchView_lineSecondHandSize, 0)
        }

        initPainters()
    }

    override fun onDraw(canvas: Canvas?) {

        paintersSetStrokeWith()

        val indent = 0.9f
        val radius = min(width, height) / 2f * indent
        val xCenter = width / 2f
        val yCenter = height / 2f

        val yStartClockFace = height / 2 - radius * indent
        val yEndClockFace = height / 2 - radius

        val lengthBackArrows = 0.2f

        val defaultSizeLineHourHand = radius * 0.4f
        val lineHourHand = choiceSize(customSizeLineHourHand, radius, defaultSizeLineHourHand)
        val yStartHourHand = yCenter + lineHourHand * lengthBackArrows
        val yEndHourHand = yCenter - lineHourHand

        val defaultSizeLineMinuteHand = radius * 0.6f
        val lineMinuteHand = choiceSize(customSizeLineMinuteHand, radius, defaultSizeLineMinuteHand)
        val yStartMinuteHand = yCenter + lineMinuteHand * lengthBackArrows
        val yEndMinuteHand = yCenter - lineMinuteHand

        val defaultSizeLineSecondHand = radius * 0.9f
        val lineSecondHand = choiceSize(customSizeLineSecondHand, radius, defaultSizeLineSecondHand)
        val yStartSecondHand = yCenter + lineSecondHand * lengthBackArrows
        val yEndSecondHand = yCenter - lineSecondHand

        val time = Calendar.getInstance()
        val hour = time.get(Calendar.HOUR)
        val minute = time.get(Calendar.MINUTE)
        val second = time.get(Calendar.SECOND)

        val stepHourAngle = 30f
        val stepMinuteAndSecondAngle = 6f
        val stepWatch= 60f
        val angleHour = hour * stepHourAngle + stepHourAngle / stepWatch * minute
        val angleMinute = minute * stepMinuteAndSecondAngle + stepMinuteAndSecondAngle / stepWatch * second
        val angleSecond = second * stepMinuteAndSecondAngle

        canvas?.apply {

            drawCircle(xCenter, yCenter, radius , painterBackgroundClockFace)
            drawCircle(xCenter, yCenter, radius, painterClockFace)

            for (i in 0..11) {
                drawLine(xCenter, yStartClockFace, xCenter, yEndClockFace, painterClockFace)
                rotate(stepHourAngle, xCenter, yCenter)
            }

            rotate(angleHour, xCenter, yCenter)
            drawLine(xCenter, yStartHourHand, xCenter, yEndHourHand, painterHourHand)

            rotate(angleMinute, xCenter, yCenter)
            drawLine(xCenter, yStartMinuteHand, xCenter, yEndMinuteHand, painterMinuteHand)

            rotate(angleSecond, xCenter, yCenter)
            drawLine(xCenter, yStartSecondHand, xCenter, yEndSecondHand, painterSecondHand)
        }
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isStopWatch = true

    }

    fun setColorHourHand(@ColorLong color: Int) {
        painterHourHand.color = color
        invalidate()
    }

    fun setColorMinuteHand(@ColorLong color: Int) {
        painterMinuteHand.color = color
        invalidate()
    }

    fun setColorSecondHand(@ColorLong color: Int) {
        painterSecondHand.color = color
        invalidate()
    }

    fun setColorClockFace(@ColorLong color: Int) {
        painterClockFace.color = color
        invalidate()
    }

    fun setColorBackgroundClockFace(@DimenRes color: Int) {
        painterBackgroundClockFace.color = color
        invalidate()
    }

    fun setCustomSizeLineHourHand(@DimenRes id: Int) {
        customSizeLineHourHand = resources.getDimensionPixelSize(id)
        invalidate()
    }

    fun setCustomSizeLineMinuteHand(@DimenRes id: Int) {
        this.customSizeLineMinuteHand = resources.getDimensionPixelSize(id)
        invalidate()
    }

    fun setCustomSizeLineSecondHand(@DimenRes id: Int) {
        customSizeLineSecondHand = resources.getDimensionPixelSize(id)
        invalidate()
    }

    private fun initPainters() {
        painterClockFace = Paint().apply {
            color = colorClockFace
            isAntiAlias = true
            style = Paint.Style.STROKE

        }

        painterHourHand = Paint().apply {
            color = colorHourHand
            isAntiAlias = true
            style = Paint.Style.STROKE

        }

        painterMinuteHand = Paint().apply {
            color = colorMinuteHand
            isAntiAlias = true
            style = Paint.Style.STROKE

        }

        painterSecondHand = Paint().apply {
            color = colorSecondHand
            isAntiAlias = true
            style = Paint.Style.STROKE

        }

        painterBackgroundClockFace = Paint().apply {
            color = colorBackgroundClockFace
        }
    }

    private fun choiceSize(customSizeHand: Int, radius: Float, size: Float) = if (customSizeHand == 0) size
    else min(customSizeHand.toFloat(), radius)

    private fun paintersSetStrokeWith() {
        val thicknessClockFaceAndHourHand = 50f
        val thicknessMinuteHand = 100f
        val thicknessSecondHand = 100f
        painterClockFace.strokeWidth = min(width, height) / thicknessClockFaceAndHourHand
        painterHourHand.strokeWidth = min(width, height) / thicknessClockFaceAndHourHand
        painterMinuteHand.strokeWidth = min(width, height) / thicknessMinuteHand
        painterSecondHand.strokeWidth = min(width, height) / thicknessSecondHand
    }
}