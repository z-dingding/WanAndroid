package com.hxzk.main.ui.splash

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.TextView
import java.util.jar.Attributes

@SuppressLint("AppCompatCustomView")
class RunTextView(mContext : Context, attributes: AttributeSet) :TextView(mContext,attributes) {


    val  duration : Int= 1500
    private var  number : Float= 0f

    fun getNumber(): Float{
        return number;
    }

  fun setNumber( number :Float) {
        this.number = number
        text = String.format("%.2f",number)
    }


    /**
     * 显示
     * @param number
     */
     fun runWithAnimation(number : Float){
        val objectAnimator = ObjectAnimator.ofFloat(
                this, "number", 0f, number);
        objectAnimator.duration = duration.toLong();
        //AccelerateDecelerateInterpolator插值器其变化开始和结束速率较慢，中间加速
        objectAnimator.interpolator = AccelerateDecelerateInterpolator();
        objectAnimator.start();
    }

}