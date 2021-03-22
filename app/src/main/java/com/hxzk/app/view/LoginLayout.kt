package com.hxzk.app.view

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_open_source_login.view.*

/**
 *作者：created by zjt on 2021/3/22
 *描述:自定义LinearLayout处理软键盘弹出问题
 *
 */
class LoginLayout(context: Context, attributes: AttributeSet) : LinearLayout(context,attributes){

  var keyboardShowed = false

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        //布局发生改变
        if(changed) {
            var width = r - l
            var height = bottom - top
            //如果高宽比小于4::3说明软键盘弹出
            if (height.toFloat() / width.toFloat() < 4f / 3f) {
                post {
                    //先隐藏删除按钮
                    close.visibility = View.INVISIBLE
                    //是针对在父控件中的View参数获取(本身)
                    val params = loginLayoutTop.layoutParams as LayoutParams
                    params.weight = 2.5f
                    keyboardShowed = true
                    //从View树重新进行一次测量、布局、绘制这三个流程
                    loginLayoutTop.requestLayout()
                }
            } else {
                if (keyboardShowed) {
                    post {
                    //软键盘退出
                    close.visibility = View.VISIBLE
                    //是针对在父控件中的View参数获取(本身)
                    val params = loginLayoutTop.layoutParams as LayoutParams
                    params.weight = 4f
                    //从View树重新进行一次测量、布局、绘制这三个流程
                    loginLayoutTop.requestLayout()
                }
                }
            }
        }
    }
}