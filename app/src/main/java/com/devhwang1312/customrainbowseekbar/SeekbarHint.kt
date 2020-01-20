package com.devhwang1312.customrainbowseekbar

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.Log
import android.widget.SeekBar

class SeekbarHint : SeekBar {

    private var mColorSeeds = intArrayOf(
        -0x1000000,
        -0x66ff01,
        -0xffff01,
        -0xff0100,
        -0xff0001,
        -0x10000,
        -0xff01,
        -0x9a00,
        -0x100,
        -0x1,
        -0x1000000
    )
    private var mColorRect = RectF()

    private var mColorRectPaint = Paint()


    private val MIN_PROGRESS_VALUE = 0
    private val MAX_PROGRESS_VALUE = 100

    private var mSeekBarHintPaint: Paint? = null
    private var mHintTextColor: Int = 0
    private var mHintTextSize: Float = 0.toFloat()

    private val rectTrigger = Rect()
    private var triggerDrawable: Drawable? = null
    private val triggerPaint = Paint()
    private var triggerWidth = 0f
    private var triggerHeight = 0f


    private var rootWidth = 0
    private var rootHeight = 0


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SeekbarHint,
            0, 0
        )

        try {
            mHintTextColor = a.getColor(R.styleable.SeekbarHint_hint_text_color, 0)
            mHintTextSize = a.getDimension(R.styleable.SeekbarHint_hint_text_size, 0f)
            triggerWidth = a.getDimension(R.styleable.SeekbarHint_hint_trigger_width, 13f)
            triggerDrawable = a.getDrawable(R.styleable.SeekbarHint_hint_trigger_src)
        } finally {
            a.recycle()
        }

        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }
    private var xx : LinearGradient ? = null
    private fun init() {
        max = MAX_PROGRESS_VALUE
        mSeekBarHintPaint = TextPaint()
        mSeekBarHintPaint!!.color = mHintTextColor
        mSeekBarHintPaint!!.textSize = mHintTextSize
        triggerPaint.color = Color.RED
        mColorRectPaint.isAntiAlias = true

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mColorRectPaint.shader = LinearGradient(
            0f,
            0f,
            w.toFloat(),
            0f,
            mColorSeeds,
            null,
            Shader.TileMode.CLAMP
        )
    }

    @Synchronized
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val label_x = thumb.bounds.centerX().toFloat() // - (mHintTextSize / 1)
        val trigger_x = thumb.bounds.centerX().toFloat() + triggerWidth/2
        val label_y = thumb.bounds.centerY().toFloat() + (mHintTextSize / 4)
        val trigger_y = thumb.bounds.centerY().toFloat() + (triggerWidth / 4)

        val findX =
            when {
            progress < 10 -> label_x + (mHintTextSize)
            else -> label_x
        }

        //init rect
//        mColorRect.set(0f, 0f, width.toFloat(), height.toFloat()/2)
//        canvas.drawRect(mColorRect,mColorRectPaint)


        rectTrigger.set(
            trigger_x.toInt(),
            0,
            (trigger_x + triggerWidth).toInt(),
            height/2 - thumbOffset / 2
        )




        triggerDrawable?.let {
            it.bounds.set(rectTrigger)
            it.draw(canvas)
        }

        canvas.drawText(
            "$progress%", findX,
            label_y,
            mSeekBarHintPaint!!
        )
    }


}