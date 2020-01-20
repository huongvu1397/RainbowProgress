package com.devhwang1312.customrainbowseekbar

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.sign

class RainbowSeekbar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

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

    private var mColorRect: RectF = RectF()
    private var mThumbHeight = 20
    private var mBarHeight = 2
    private var mColorRectPaint = Paint()
    private var mBarWidth: Int = 0
    private var mMaxPosition: Int = 0
    private var mColorBarPosition: Int = 0
    private val colorPaint = Paint()
    private var mThumbRadius: Float = 0.toFloat()
    private var realLeft: Int = 0
    private var realRight: Int = 0
    private val rectTrigger = Rect()
    private var triggerDrawable: Drawable? = null
    private var triggerWidth = 0f
    private var triggerHeight = dp2px(3f)


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        init()
    }

    init {
        val a = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.RainbowSeekbar,
            0, 0
        )
        mColorBarPosition = a.getInt(R.styleable.RainbowSeekbar_rainbow_progress, 0)
        triggerWidth = a.getDimension(R.styleable.RainbowSeekbar_rainbow_trigger_width, 13f)
        triggerDrawable = a.getDrawable(R.styleable.RainbowSeekbar_rainbow_trigger_src)
        mMaxPosition = 100
        mBarHeight = dp2px(2f)
        mThumbHeight = dp2px(30f)
        a.recycle()
    }

    private fun init() {
        mThumbRadius = (mThumbHeight / 2).toFloat()
        val mPaddingSize = mThumbRadius.toInt()
        val viewRight = width - paddingRight - mPaddingSize
        //init left right top bottom
        realLeft = paddingLeft + mPaddingSize
        realRight = viewRight
        val realTop = paddingTop + mPaddingSize
        mBarWidth = realRight - realLeft
        //init rect
        mColorRect = RectF(
            realLeft.toFloat(),
            realTop.toFloat(),
            realRight.toFloat(),
            (realTop + mBarHeight).toFloat()
        )
        //init paint
        val mColorGradient =
            LinearGradient(0f, 0f, mColorRect.width(), 0f, mColorSeeds, null, Shader.TileMode.CLAMP)
        mColorRectPaint = Paint()
        mColorRectPaint.shader = mColorGradient
        mColorRectPaint.isAntiAlias = true
        colorPaint.isAntiAlias = true
        colorPaint.color = Color.RED
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.let {
            canvas.drawRect(mColorRect,mColorRectPaint)
            val colorPosition = mColorBarPosition.toFloat() / mMaxPosition * mBarWidth
            val thumbX = colorPosition + realLeft
            val thumbY = mColorRect.top + mColorRect.height() / 2

            rectTrigger.set(
                thumbX.toInt(),
                (mColorRect.top-triggerHeight*2).toInt(),
                (thumbX + triggerWidth).toInt(),
                thumbY.toInt()
            )
            triggerDrawable?.let {
                it.bounds.set(rectTrigger)
                it.draw(canvas)
            }
        }
        super.onDraw(canvas)
    }

    private fun dp2px(dpValue: Float): Int {
        val scale = resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun setColorBarPosition(value: Int) {
        this.mColorBarPosition = value
        mColorBarPosition =
            if (mColorBarPosition > mMaxPosition) mMaxPosition else mColorBarPosition
        mColorBarPosition = if (mColorBarPosition < 0) 0 else mColorBarPosition
        invalidate()
    }
}