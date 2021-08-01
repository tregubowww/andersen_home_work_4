package ru.tregubowww.andersen_home_work_4

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.DimenRes
import androidx.core.content.withStyledAttributes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar
import kotlin.math.min

class WatchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defaultAttributeSet: Int = 0
) : View(context, attrs, defaultAttributeSet) {

    private var colorHourHand = 0
    private var colorMinuteHand = 0
    private var colorSecondHand = 0
    private var colorClockFace = 0
    private var colorBackgroundClockFace = 0
    private var customSizeLineHourHand = 0
    private var customSizeLineMinuteHand = 0
    private var customSizeLineSecondHand = 0

    private val time = Calendar.getInstance()
    private var hour = time.get(Calendar.HOUR)
    private var minute = time.get(Calendar.MINUTE)
    private var second = time.get(Calendar.SECOND)

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
            customSizeLineHourHand = getDimensionPixelSize(R.styleable.WatchView_lineHourHandSize, 0)
            customSizeLineMinuteHand = getDimensionPixelSize(R.styleable.WatchView_lineMinuteHandSize, 0)
            customSizeLineSecondHand = getDimensionPixelSize(R.styleable.WatchView_lineSecondHandSize, 0)
        }
        
        initPainters()
    }

    override fun onDraw(canvas: Canvas?) {

        paintersSetStrokeWith()

        val radius = min(width, height) / 2 * 0.9f
        val xCenter = width / 2f
        val yCenter = height / 2f

        val yStartClockFace = height / 2 - radius * 0.9f
        val yEndClockFace = height / 2 - radius

        val lineHourHand = choiceSize(customSizeLineHourHand, radius, radius * 0.4f)
        val yStartHourHand = yCenter + lineHourHand * 0.2f
        val yEndHourHand = yCenter - lineHourHand

        val lineMinuteHand = choiceSize(customSizeLineMinuteHand, radius, radius * 0.6f)
        val yStartMinuteHand = yCenter + lineMinuteHand * 0.2f
        val yEndMinuteHand = yCenter - lineMinuteHand

        val lineSecondHand = choiceSize(customSizeLineSecondHand, radius, radius * 0.9f)
        val yStartSecondHand = yCenter + lineSecondHand * 0.2f
        val yEndSecondHand = yCenter - lineSecondHand

        val angleHour = hour * 30f
        val angleMinute = minute * 6f + (360 - angleHour)
        val angleSecond = second * 6f + (360 - minute *6f)

        canvas?.apply {

            drawCircle(xCenter, yCenter, radius, painterBackgroundClockFace)
            drawCircle(xCenter, yCenter, radius, painterClockFace)

            for (i in 0..11) {
                drawLine(xCenter, yStartClockFace, xCenter, yEndClockFace, painterClockFace)
                rotate(30f, xCenter, yCenter)
            }

            rotate(angleHour, xCenter, yCenter)
            drawLine(xCenter, yStartHourHand, xCenter, yEndHourHand, painterHourHand)

            rotate(angleMinute, xCenter, yCenter)
            drawLine(xCenter, yStartMinuteHand, xCenter, yEndMinuteHand, painterMinuteHand)

            rotate(angleSecond, xCenter, yCenter)
            drawLine(xCenter, yStartSecondHand, xCenter, yEndSecondHand, painterSecondHand)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        isStopWatch = true

    }

    fun startTime() {
        isStopWatch = false
        CoroutineScope(Dispatchers.Main).launch {
            while (!isStopWatch) {
                delay(1000)
                val time = Calendar.getInstance()
                hour = time.get(Calendar.HOUR)
                minute = time.get(Calendar.MINUTE)
                second = time.get(Calendar.SECOND)
                invalidate()
            }
        }

    }

    fun setColorHourHand(color: Int) {
        painterHourHand.color = color
        invalidate()
    }

    fun setColorMinuteHand(color: Int) {
        painterMinuteHand.color = color
        invalidate()
    }

    fun setColorSecondHand(color: Int) {
        painterSecondHand.color = color
        invalidate()
    }

    fun setColorClockFace(color: Int) {
        painterClockFace.color = color
        invalidate()
    }

    fun setColorBackgroundClockFace(color: Int) {
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
    else min( customSizeHand.toFloat(), radius)

    private fun paintersSetStrokeWith() {
        painterClockFace.strokeWidth = min(width, height) / 50f
        painterHourHand.strokeWidth = min(width, height) / 50f
        painterMinuteHand.strokeWidth = min(width, height) / 100f
        painterSecondHand.strokeWidth = min(width, height) / 120f
    }
}