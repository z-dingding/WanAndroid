package com.hxzk.main.ui.system.sysitem

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.ui.adapter.AnswerAdapter
import com.hxzk.main.ui.adapter.AnswerItemListAdapter
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.network.model.DataX

/**
 * @author: hxzk_zjt
 * @date: 2021/5/8
 * 描述:
 */

@BindingAdapter("sysItems")
 fun setSysItems(recycler: RecyclerView, items :List<DataX>?){
    items?.let {
        ( recycler.adapter as AnswerItemListAdapter).submitList(items)
    }
}