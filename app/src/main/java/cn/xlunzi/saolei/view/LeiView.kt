package cn.xlunzi.saolei.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.MotionEvent


/**
 * Created by SunLW on 2018-04-01.
 */
class LeiView(ctx: Context, attrs: AttributeSet?, defStyleRes: Int) : SurfaceView(ctx, attrs, defStyleRes),
        SurfaceHolder.Callback {

    constructor(ctx: Context) : this(ctx, null)
    constructor(ctx: Context, attrs: AttributeSet?) : this(ctx, attrs, 0)

    // SurfaceHolder
    private lateinit var mHolder: SurfaceHolder
    // 用于绘图的Canvas
    private lateinit var mCanvas: Canvas
    // 子线程标志位
    private var mIsDrawing: Boolean = false

    private var mIsRunning = false

    /** 每30帧刷新一次屏幕 */
    val TIME_IN_FRAME = 30

    private lateinit var mPath: Path
    private lateinit var mPaint: Paint

    init {
        initView()
    }

    private fun initView() {
        mHolder = holder //获取SurfaceHolder对象
        mHolder.addCallback(this) //注册SurfaceHolder的回调方法
        isFocusable = true
        isFocusableInTouchMode = true
        this.keepScreenOn = true

        mPath = Path()
        mPaint = Paint()
        mPaint.color = Color.CYAN
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 10.0f
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        mIsDrawing = true
        mIsRunning = true
        Thread {
            while (mIsRunning) {
                val startTime = System.currentTimeMillis()

                synchronized(mHolder) {
                    draw()
                }

                val endTime = System.currentTimeMillis()
                var diffTime = endTime - startTime

                while (diffTime < TIME_IN_FRAME) {
                    diffTime = System.currentTimeMillis() - startTime
                    // 线程等待
                    Thread.yield()
                }
            }
        }.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mIsDrawing = false
        mIsRunning = false
    }

    //绘图操作
    private fun draw() {
        try {
            // 拿到当前画布 然后锁定
            mCanvas = mHolder.lockCanvas()
            // SurfaceView背景
            mCanvas.drawColor(Color.WHITE)
            mCanvas.drawPath(mPath, mPaint)
        } catch (e: Exception) {
        } finally {
            // 绘制结束后解锁显示在屏幕上
            try {
                mHolder.unlockCanvasAndPost(mCanvas)
            } catch (e: Exception) {
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> mPath.moveTo(x, y)
            MotionEvent.ACTION_MOVE -> mPath.lineTo(x, y)
            MotionEvent.ACTION_UP -> {
            }
        }
        return true // 表示此View拦截处理触摸事件
    }
}