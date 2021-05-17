package com.hxzk.main.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.R
import com.hxzk.main.databinding.*
import com.hxzk.main.ui.answer.AnswerViewModel
import com.hxzk.main.ui.publics.PublicViewModel
import com.hxzk.network.model.Children
import com.hxzk.network.model.DataX
import com.hxzk.network.model.RankDataX
import kotlinx.android.synthetic.main.adapter_rankitem.view.*

/**
 * @author: hxzk_zjt
 * @date: 2021/5/7
 * 描述:公众号右侧适配器
 */
class PublicRightAdapter constructor(val viewModel: PublicViewModel) : ListAdapter<DataX, RecyclerView.ViewHolder>(PublicRightItemDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder : RecyclerView.ViewHolder
        if (viewType ==  1) {
            viewHolder =  ItemTitleViewHolder.from(parent)
        }else if(viewType ==  0){
            viewHolder =  ItemContentViewHolder.from(parent)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = getItem(position)
        if (holder is ItemTitleViewHolder) {
            holder.bind(itemData, viewModel)
        }else if(holder is ItemContentViewHolder){
            holder.bind(itemData, viewModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return  getItem(position).itemType
    }
    class ItemTitleViewHolder private constructor(private val binding: AdapterPublicrightTitleItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataX, viewModel: PublicViewModel) {
            binding.item = data
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterPublicrightTitleItemBinding.inflate(layoutInflater, parent, false)
                return ItemTitleViewHolder(binding)
            }
        }
    }
    class ItemContentViewHolder private constructor(private val binding: AdapterPublicrightContentItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DataX, viewModel: PublicViewModel) {
            binding.item = data
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterPublicrightContentItemBinding.inflate(layoutInflater, parent, false)
                return ItemContentViewHolder(binding)
            }
        }
    }
}



// DiffUtil将自动为我们处理然后进行调用，其功能 就是比较两个数据集，用newList和oldList进行比较
class PublicRightItemDiffCallback : DiffUtil.ItemCallback<DataX>() {
    override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
        //判断这个两个对象是否是同一个对象。
        return oldItem.id == newItem.id
    }

    //判断两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载
    override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
        return oldItem == newItem
    }

}