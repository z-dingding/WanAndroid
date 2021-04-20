package com.hxzk.tencentx5.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.hxzk.tencentx5.R

/**
 *作者：created by zjt on 2021/4/20
 *描述:自定义用户WebView页面加载的圆形进度条
 *
 */
class WebProgressView constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : View(context, attrs) {

    /**
     * 进度边框的画笔
     */
    lateinit var borderPrint: Paint
    /**
     * 圆的画笔(实心圆)
     */
    lateinit var circlePrint: Paint

    /**
     * 边框宽度,默认为4dp
     */
    var boderWidth: Float = 4.0F

    /**
     * 边框颜色,默认为white
     */
    var boderColor: Int = Color.WHITE

    /**
     * 起始位置,默认从左部开始
     */
    var startLoaction: Int = 1

    /**
     * 起始位置对应的角度
     */
    var startAngle: Int = 180

    /**
     * 当前完成的进度
     */
    var currentProgress : Int = 0


    init {
        val typeArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.web_progress)
        boderWidth = typeArray.getDimension(
            R.styleable.web_progress_progress_border_width,
            dip2px(context, boderWidth)
        )
        boderColor = typeArray.getColor(R.styleable.web_progress_progress_border_color, boderColor)
        startLoaction = typeArray.getInt(R.styleable.web_progress_progress_border_startPosition, 1)
        when (startLoaction) {
            1 -> startAngle = 180
            2 -> startAngle = 270
            3 -> startAngle = 0
            4 -> startAngle = 90
        }
        //程序在运行时维护了一个 TypedArray的池，程序调用时，会向该池中请求一个实例，
        // TypedArray是单例,用完之后，调用 recycle() 方法来释放该实例，从而使其可被其他模块复用。
        typeArray.recycle()

        //画笔->进度圆弧(进度条)
        borderPrint = Paint()
        //来动态开关抗锯齿,让图形和文字的边缘更加平滑
        borderPrint.setAntiAlias(true)
        borderPrint.setStrokeWidth(boderWidth)
        //只画线不填充
        borderPrint.setStyle(Paint.Style.STROKE)
        borderPrint.setColor(boderColor)
        //设置：ROUND 画出来是圆形的点，SQUARE 或 BUTT 画出来是方形的点
        borderPrint.setStrokeCap(Paint.Cap.ROUND)

        //画笔->圆的画笔(主要是为了增加背景)
        circlePrint = Paint()
        //只填充不画线
        circlePrint.setStyle(Paint.Style.FILL)
        circlePrint.setColor(Color.WHITE)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        //获取View的宽 450px
        val width = MeasureSpec.getSize(widthMeasureSpec)
        //获取View的高 450px
        val height = MeasureSpec.getSize(heightMeasureSpec)
        //因为画圆，所以这步实际是确认圆直径通过比较用户设置的view宽高(如果宽高不一致，获取最短的作为直径)
        val size = Math.min(width, height)
        //setMeasuredDimension这个方法，这个方法决定了当前View的大小
        setMeasuredDimension(size, size)
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        //绘制背景圆弧,因为画笔有一定的宽度，所有画圆弧的范围要比View本身的大小稍微小一些，不然画笔画出来的东西会显示不完整
        //两个点就可以确定一个矩形,在矩形中画圆
        @SuppressLint("DrawAllocation")
        val rectF = RectF(
            boderWidth / 2,
            boderWidth / 2,
            width - boderWidth / 2,
            height - boderWidth / 2
        )
        //画圆
        canvas!!.drawArc(rectF, 0F, 360F, false, circlePrint)

        //drawArc() 是使用一个椭圆来描述弧形的。left, top, right, bottom 描述的是这个弧形所在的椭圆；用RectF代替
        //startAngle 是弧形的起始角度（x 轴的正向，即正右的方向，是 0 度的位置；顺时针为正角度，逆时针为负角度），sweepAngle 是弧形划过的角度；
        //useCenter 表示是否连接到圆心，如果不连接到圆心，就是弧形，如果连接到圆心，就是扇形
        //绘制当前进度
        val sweepAngle: Float = 360 * currentProgress / 100.toFloat()
        //画圆的边框(进度)
        canvas!!.drawArc(rectF, startAngle.toFloat(), sweepAngle, false, borderPrint)

        //将图片画上
        val bitmap = BitmapFactory.decodeResource(resources, R.mipmap.back)
        // left 和 top 是要把 bitmap 绘制到的位置坐标
        canvas.drawBitmap(
            bitmap,
            (width - bitmap.width) / 2F,
            (height - bitmap.height) / 2F,
            borderPrint
        )

        if(currentProgress == 100){
            //如果加载完毕，隐藏进度条
            borderPrint.color = Color.WHITE
            canvas.drawArc(rectF, sweepAngle, sweepAngle, false, borderPrint)
        }
    }

    /**
     * 设置当前进度并重新绘制界面
     */
    fun setmCurrent(current: Int) {
        currentProgress = current
        //不断的进行绘制界面
        invalidate()
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Float {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f)
    }
}