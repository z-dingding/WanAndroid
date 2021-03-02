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
        if (position > -1 && position < 1) {
            var pivotX : Float = if (position < 0f) page.getWidth().toFloat() else 0f
            page.setPivotX(pivotX)
            page.setPivotY(page.getHeight() * 0.5f)
            page.setRotationY(position * 45f)
        }
    }


}