package com.hxzk.main.ui.integral

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allen.library.SuperTextView
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.main.ui.adapter.IntegralItemAdapter
import com.hxzk.network.model.ArticleListModel
import com.hxzk.network.model.DataItem
import com.hxzk.network.model.IntegralItemModel

/**
 * @author: hxzk_zjt
 * @date: 2021/4/29
 * 描述:
 */

/**
 * 我的积分列表的展示数据源
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<DataItem>?) {
    items?.let {
        //submitList()更新现有列表
        (listView.adapter as IntegralItemAdapter).submitList(items)
    }
}

@BindingAdapter("stvLeftTopTextString")
fun setstvLeftTopTextString(stv: SuperTextView, content : String) {
    content?.let {
        stv.setLeftTopString(content)
    }
}
@BindingAdapter("stvLeftBottomTextString")
fun setstvLeftBottomTextString(stv: SuperTextView, content : String) {
    content?.let {
        stv.setLeftBottomString(content)
    }
}
@BindingAdapter("stvRightTextString")
fun setstvRightTextString(stv: SuperTextView, content : String) {
    content?.let {
        stv.setRightString("+$content")
    }
}
