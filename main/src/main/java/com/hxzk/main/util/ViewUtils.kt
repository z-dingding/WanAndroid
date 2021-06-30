/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hxzk.main.util

import android.graphics.PorterDuff
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.hxzk.base.extension.logWarn
import com.hxzk.main.R


/**
 * Utility methods for working with Views.
 */
object ViewUtils {

    private val TAG = "ViewUtils"

    /**
     * 设置Toolbar上的图标颜色。
     * @param isDark
     * true表示设置成深色，false表示设置成浅色。
     */
    fun setToolbarIconColor(activity: AppCompatActivity, toolbar: Toolbar, isDark: Boolean) {
        try {
            // change back button color.
            val color: Int
            if (isDark) {
                color = ContextCompat.getColor(activity, R.color.text_black)
            } else {
                color = ContextCompat.getColor(activity, R.color.text_white)
            }
            val backArrow = ContextCompat.getDrawable(activity, R.drawable.abc_ic_ab_back_material)
            backArrow?.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
            val actionBar = activity.supportActionBar
            actionBar?.setHomeAsUpIndicator(backArrow)
            // change overflow button color.
            var drawable = toolbar.overflowIcon
            if (drawable != null) {
                drawable = DrawableCompat.wrap(drawable)
                DrawableCompat.setTint(drawable!!.mutate(), color)
                toolbar.overflowIcon = drawable
            }
            // change title text color.
            toolbar.setTitleTextColor(color)
        } catch (e: Exception) {
            logWarn(TAG, e.message, e)
        }

    }

    /**
     * 状态栏为亮色模式（状态栏图标和文字变成黑色）
     */
    fun setLightStatusBar(window: Window, view: View) {
        if (OSUtil.isMiUI8OrLower) {
            setMiUIStatusBarLightMode(window, true)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var flags = view.systemUiVisibility
                flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                view.systemUiVisibility = flags
            }
        }
    }
    /**
     * 状态栏为暗色模式（状态栏图标和文字变成白色）
     */
    fun clearLightStatusBar(window: Window, view: View) {
        if (OSUtil.isMiUI8OrLower) {
            setMiUIStatusBarLightMode(window, false)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var flags = view.systemUiVisibility
                flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                view.systemUiVisibility = flags
            }
        }
    }

    /**
     * 判断两个View是否在屏幕上有相交(交际)
     */
    fun viewsIntersect(view1: View?, view2: View?): Boolean {
        if (view1 == null || view2 == null) return false

        val view1Loc = IntArray(2)
        //一个控件在其整个屏幕上的坐标位置
        view1.getLocationOnScreen(view1Loc)
        val view1Rect = Rect(view1Loc[0],
                view1Loc[1],
                view1Loc[0] + view1.width,
                view1Loc[1] + view1.height)
        val view2Loc = IntArray(2)
        view2.getLocationOnScreen(view2Loc)
        val view2Rect = Rect(view2Loc[0],
                view2Loc[1],
                view2Loc[0] + view2.width,
                view2Loc[1] + view2.height)
        //查找当前 Rect 所表示的矩形与指定的 Rect 所表示的矩形之间的交集，并将结果存储为当前 Rect。
        return view1Rect.intersect(view2Rect)
    }

    /**
     * 设置小米手机状态栏字体图标颜色模式，需要MIUI 6以上系统才支持。
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     */
    fun setMiUIStatusBarLightMode(window: Window?, dark: Boolean) {
        if (window != null) {
            val clazz = window.javaClass
            try {
                val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
                val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
                val darkModeFlag = field.getInt(layoutParams)
                val extraFlagField = clazz.getMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
                if (dark) { // 将状态栏字体和图标设成黑色
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
                } else { // 将状态栏字体设成原色
                    extraFlagField.invoke(window, 0, darkModeFlag)
                }
            } catch (e: Exception) {
                logWarn(TAG, e.message, e)
            }

        }
    }

}
