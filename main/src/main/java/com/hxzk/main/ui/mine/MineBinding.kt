package com.hxzk.main.ui.mine

import androidx.databinding.BindingAdapter
import com.allen.library.SuperTextView

/**
 *作者：created by zjt on 2021/4/26
 *描述:
 */

@BindingAdapter("app:iconCount")
fun setIconCount(tv: SuperTextView, iconCount: Int) {
    tv.setRightString(iconCount.toString())
}