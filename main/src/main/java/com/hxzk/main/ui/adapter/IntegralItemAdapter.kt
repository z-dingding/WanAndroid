package com.hxzk.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.callback.BannerItemListener
import com.hxzk.main.databinding.AdapterHomeitemBinding
import com.hxzk.main.databinding.AdapterIntegralitemBinding
import com.hxzk.main.databinding.BannerFragmentBinding
import com.hxzk.main.ui.home.HomeViewModel
import com.hxzk.main.ui.integral.IntegraViewModel
import com.hxzk.network.model.DataItem
import com.hxzk.network.model.DataX
import com.hxzk.network.model.HomeBanner

/**
 *作者：created by zjt on 2021/4/6
 *描述:积分列表的Adapter
 *
 */
class IntegralItemAdapter(private val viewModel: IntegraViewModel) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(TaskDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position)
            if (holder is ItemViewHolder) {
                holder.bind(viewModel, item)
        }
    }

    override fun getItemCount(): Int {
        return viewModel.items.value?.size ?: 0
    }


    override fun submitList(list: List<DataItem>?) {
        super.submitList(list?.let { ArrayList(it) })
    }


class ItemViewHolder private constructor(private val binding: AdapterIntegralitemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: IntegraViewModel, item: DataItem) {
        //此处将布局中的data赋值
        binding.veiwModel=viewModel
        binding.item = item
        //executePendingBindings它使数据绑定刷新所有挂起的更改
        binding.executePendingBindings()
    }


    //companion object中声明的变量类似于Java中的静态变量,只在类里面能定义一次
    //使用 @JvmStatic 使Companion object 的成员真正成为静态成员
    //companion object是当包含它的类被加载时就初始化了的，这一点和Java的static还是一样的
    companion object {
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AdapterIntegralitemBinding.inflate(layoutInflater, parent, false)
            return ItemViewHolder(binding)
        }
    }
}

}

// DiffUtil将自动为我们处理然后进行调用，其功能 就是比较两个数据集，用newList和oldList进行比较
class TaskDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        //判断这个两个对象是否是同一个对象。
        return oldItem.id == newItem.id
    }

    //判断两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载
    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }

}
