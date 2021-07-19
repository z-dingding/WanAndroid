package com.hxzk.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.databinding.AdapterCollectionitemBinding
import com.hxzk.main.ui.collection.CollectionViewModel
import com.hxzk.network.model.DataX

/**
 *作者：created by zjt on 2021/4/6
 *描述:积分列表的Adapter
 *
 */
class CollectionAdapter(private val viewModel: CollectionViewModel) :
        ListAdapter<DataX, RecyclerView.ViewHolder>(IntegralDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder.from(parent)
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        if (holder is ItemViewHolder) {
            holder.bind(viewModel, item,position)
        }
    }

    override fun getItemCount(): Int {
        return viewModel.colItems.value?.size ?: 0
    }


    override fun submitList(list: List<DataX>?) {
        super.submitList(list)
    }


    class ItemViewHolder private constructor(private val binding: AdapterCollectionitemBinding) :
            RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: CollectionViewModel, item: DataX,pos : Int) {
            //此处将布局中的data赋值
            binding.viewModel = viewModel
            binding.item = item
            binding.pos = pos
            //executePendingBindings它使数据绑定刷新所有挂起的更改
            binding.executePendingBindings()
        }


        //companion object中声明的变量类似于Java中的静态变量,只在类里面能定义一次
        //使用 @JvmStatic 使Companion object 的成员真正成为静态成员
        //companion object是当包含它的类被加载时就初始化了的，这一点和Java的static还是一样的
        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterCollectionitemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }
}


