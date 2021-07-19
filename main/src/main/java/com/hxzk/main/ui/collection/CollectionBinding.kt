package com.hxzk.main.ui.collection

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.ui.adapter.BrowseHistoryAdapter
import com.hxzk.main.ui.adapter.CollectionAdapter
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.DataX

/**
 * @author: hxzk_zjt
 * @date: 2021/7/19
 * 描述:
 */


@BindingAdapter("items")
fun setItems(rv: RecyclerView, items: List<DataX>?) {
    items?.let {
        //submitList()更新现有列表
        (rv.adapter as CollectionAdapter).submitList(items)
    }
}