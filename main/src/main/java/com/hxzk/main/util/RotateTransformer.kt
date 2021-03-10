package com.hxzk.main.util

import android.view.View
import androidx.viewpager2.widget.ViewPager2

/**
 *作者：created by zjt on 2021/3/2
 *描述: ViewPager2的旋转动画
 *
 */
class RotateTransformer  : ViewPager2.PageTransformer{
    override fun transformPage(page: View, position: Float) {
        //第一个参数是页面对象，第二个参数是偏移量,取值范围（-1,1）当前页面显示位置值为0,左为-1右为1
        if (position > -1 && position < 1) {
            //页面X轴的中心点为page.width或者0.
            var pivotX : Float = if (position < 0f) page.getWidth().toFloat() else 0f
            page.setPivotX(pivotX)
            //页面Y轴的中心点为Y轴的一半
            page.setPivotY(page.getHeight() * 0.5f)
            //设置相对Y轴（竖直轴）旋转，正值为从Y轴向下看顺时针旋转
            page.setRotationY(position * 60f)
        }
    }


}