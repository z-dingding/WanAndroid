package com.hxzk.main.ui.browsehistroy

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.allen.library.SuperTextView
import com.hxzk.main.ui.adapter.BrowseHistoryAdapter
import com.hxzk.main.ui.adapter.IntegralItemAdapter
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.DataItem

/**
 * @author: hxzk_zjt
 * @date: 2021/6/29
 * 描述:
 */

@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<CommonItemModel>?) {
    items?.let {
        //submitList()更新现有列表
        (listView.adapter as BrowseHistoryAdapter).submitList(items)
    }
}

@BindingAdapter("sRightTextString")
fun setsRightTextString(stv: SuperTextView, content : String) {
    content?.let {
        stv.setLeftBottomString(content)
    }
}
@BindingAdapter("sLeftTextString")
fun setsLeftTextString(stv: SuperTextView, content : String) {
    content?.let {
        stv.setRightString("+$content")
    }
}
