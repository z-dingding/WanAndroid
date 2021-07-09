/*
 * Copyright 2017 Google Inc. All rights reserved.
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

package com.hxzk.main.ui.adapter

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayout
import com.google.android.flexbox.FlexboxLayoutManager
import com.hxzk.main.R
import com.hxzk.main.callback.FlexItemClickListener
import com.hxzk.main.callback.HotFlexItemClickListener
import com.hxzk.main.callback.SearchFlexItemClickListener
import com.hxzk.main.ui.search.SearchViewModel


/**
 * [RecyclerView.Adapter] implementation for [FlexItemViewHolder].
 */
internal class FlexItemAdapter(private val viewModel :SearchViewModel,val isHotKey: Boolean)
    : RecyclerView.Adapter<FlexItemViewHolder>() {

    private val layoutParams = mutableListOf<FlexboxLayoutManager.LayoutParams>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlexItemViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.adapter_hotkey_flexitem, parent, false)
        return FlexItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FlexItemViewHolder, position: Int) {
        val adapterPosition = holder.adapterPosition
            if(isHotKey){
                holder.itemView.setOnClickListener {
                    mListener.onItemClick(viewModel.hotKeys.value!![adapterPosition])
                }
                holder.bindTo(layoutParams[position],viewModel.hotKeys.value!![adapterPosition].name)
            }else{
                holder.itemView.setOnClickListener {
                    mSearchListener.onItemClick(viewModel.searchKeyword.value!![adapterPosition])
                }
                holder.bindTo(layoutParams[position],viewModel.searchKeyword.value!![adapterPosition].searchKey)
            }

    }
    /**
     * 增加单个item
     */
    fun addItem(lp: FlexboxLayoutManager.LayoutParams) {
        layoutParams.add(lp)
        notifyItemInserted(layoutParams.size - 1)
    }

    /**
     * 移除单个item
     */
    fun removeItem(position: Int) {
        if (position < 0 || position >= layoutParams.size) {
            return
        }
        //把数据从list中remove掉
        layoutParams.removeAt(position)
        //显示动画效果
        notifyItemRemoved(layoutParams.size)
        //对于被删掉的位置及其后range大小范围内的view进行重新
        notifyItemRangeChanged(position, layoutParams.size)
    }

    /**
     * 清空所有item
     */
    fun removeAllItem() {
        if (layoutParams.size == 0) {
            return
        }
        //把数据从list中remove掉
        layoutParams.clear()
        //显示动画效果
        notifyItemRemoved(layoutParams.size)
        //对于被删掉的位置及其后range大小范围内的view进行重新
        notifyDataSetChanged()
    }

    val items get() = layoutParams

    override fun getItemCount() = layoutParams.size

    lateinit var mListener : HotFlexItemClickListener
    fun setFlexItemClickListener(listener:HotFlexItemClickListener){
        this.mListener = listener
    }

    lateinit var mSearchListener : SearchFlexItemClickListener
    fun setSearchFexItemClickListener(listener:SearchFlexItemClickListener){
        this.mSearchListener = listener
    }
}

class FlexItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val textView: TextView = itemView.findViewById(R.id.tvHotKey)

    fun bindTo(params: RecyclerView.LayoutParams,tvHotKeys : String) {
        textView.apply {
            text = tvHotKeys
            setBackgroundResource(R.drawable.shape_flex_item)
            gravity = Gravity.CENTER
            layoutParams = params
        }
    }


}

