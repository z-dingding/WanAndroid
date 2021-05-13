/*
 * Copyright (C) 2019 The Android Open Source Project
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
package com.hxzk.main.ui.system.child_nav

import android.content.Context
import android.text.Html
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.hxzk.base.extension.dpToPixel
import com.hxzk.main.R
import com.hxzk.main.callback.FlexItemClickListener
import com.hxzk.main.callback.NavFlexItemClickListener
import com.hxzk.main.ui.adapter.BannerImageAdapter
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.main.ui.adapter.NavigationItemAdapter
import com.hxzk.main.ui.adapter.SystemItemAdapter
import com.hxzk.network.NetWork.Companion.context
import com.hxzk.network.model.*
import com.youth.banner.Banner

/**
 * 体系列表的展示数据源
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<NavigationModel>?) {
    items?.let {
        //submitList()更新现有列表
        (listView.adapter as NavigationItemAdapter).submitList(items)
    }
}

/**
 * 体系FlexLayout的展示数据源
 */
@BindingAdapter(value = ["flexItems", "listener"], requireAll = true)
fun setFlexItems(flexContainer: FlexboxLayout, items: List<Article>?,listener: NavFlexItemClickListener) {
    items?.let {
        //由于recyclerview的复用问题,每次滚动完都重新移除一次
        flexContainer.removeAllViews()
        //给FlexboxLayout添加TextView
        for (viewIndex in items.indices) {
            val textView = createBaseFlexItemTextView(flexContainer.context, viewIndex, items)
            val lp = FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT)
            //设置父控件的宽高进而影响textVeiw
            //lp.width = context.dpToPixel()
            //lp.height = context.dpToPixel()
            val margin = flexContainer.context.dpToPixel(10)
            val padding = flexContainer.context.dpToPixel(5)
            lp.setMargins(margin, margin, margin, margin)
            textView.layoutParams = lp
            textView.setPadding(padding,padding,padding,padding)
            //设置监听
            textView.setOnClickListener {
                listener.onItemClick(items[viewIndex])
            }
            flexContainer.addView(textView)
        }
    }
}

private fun createBaseFlexItemTextView(context: Context, index: Int, items: List<Article>): TextView {
    return TextView(context).apply {
        //设置shape
        setBackgroundResource(R.drawable.shape_flex_item)
        text = items[index].title
        gravity = Gravity.CENTER
    }
}

