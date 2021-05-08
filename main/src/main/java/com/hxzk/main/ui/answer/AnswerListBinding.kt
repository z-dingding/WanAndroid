package com.hxzk.main.ui.answer

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.ui.adapter.AnswerAdapter
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.network.model.DataX

/**
 * @author: hxzk_zjt
 * @date: 2021/5/8
 * 描述:
 */

@BindingAdapter("answerItems")
 fun setAnswerItems(recycler: RecyclerView, items :MutableList<DataX>?){
    items?.let {
        ( recycler.adapter as AnswerAdapter).submitList(items)
    }
}