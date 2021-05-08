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
package com.hxzk.main.ui.home

import android.text.Html
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.R
import com.hxzk.main.ui.adapter.BannerImageAdapter
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.network.model.ArticleListModel
import com.hxzk.network.model.HomeBanner
import com.youth.banner.Banner

/**
 * 首页列表的展示数据源
 */
@BindingAdapter("app:items")
 fun setItems(listView: RecyclerView, items: ArticleListModel?) {
    items?.let {
        //submitList()更新现有列表
        (listView.adapter as HomeItemAdapter).submitList(items.datas)
    }
}

/**
 * 首页轮播展示数据源(目前不能正常使用)
 */
@BindingAdapter("bannerItems")
fun setBannerItems(mBanner:Banner<HomeBanner, BannerImageAdapter>, items: List<HomeBanner>?) {
    items?.let {
        // 暂时不支持kotlin,所以只能这样写
        mBanner.adapter = BannerImageAdapter(items,mBanner.context)
        mBanner.start()
    }
}

/**
 * 首页文章列表项是否收藏的属性
 */
@BindingAdapter("app:isCollecte")
fun setStyle(imageView: ImageView, enabled: Boolean) {
    if (enabled) {
        //imageView.setBackgroundResource()
    } else {
        //imageView.setBackgroundResource()
    }
}

/**
 * 首页文章列表项的分类属性
 */
@BindingAdapter(value = ["type", "capterName"], requireAll = true)
fun setChapterName(textView: TextView, type: Int, capterName: String){
    if (type == 1){
         val sHtml = "<font color='#E784A2'>置顶&nbsp;&nbsp;</font>$capterName"
        val  spanned = Html.fromHtml(sHtml)
        textView.text =spanned
    }else {
        textView.text = capterName
    }
}


/**
 * 首页文章列表项的标签属性
 */
@BindingAdapter("app:tag")
fun setTagName(textView: TextView, isShow: Boolean){
    if(isShow) textView.setBackgroundResource(R.drawable.shape_tag)  else  textView.setBackgroundResource(
        0
    )
}
/**
 * 首页文章列表项的描述属性
 */
@BindingAdapter("app:desc")
fun setDesc(textView: TextView, desc: String){
    if(!TextUtils.isEmpty(desc)) {
        textView.visibility = View.VISIBLE
        textView.text = Html.fromHtml(desc)
    } else {
        textView.visibility = View.GONE
    }
}