package com.hxzk.main.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.R
import com.hxzk.main.databinding.AdapterAnsweritemBinding
import com.hxzk.main.databinding.AdapterHomeitemBinding
import com.hxzk.main.databinding.AdapterPublicleftitemBinding
import com.hxzk.main.ui.answer.AnswerViewModel
import com.hxzk.main.ui.publics.PublicViewModel
import com.hxzk.network.model.Children
import com.hxzk.network.model.DataX
import com.hxzk.network.model.RankDataX
import kotlinx.android.synthetic.main.adapter_rankitem.view.*

/**
 * @author: hxzk_zjt
 * @date: 2021/5/7
 * 描述:公众号左侧适配器
 */
class PublicLeftAdapter constructor(val viewModel: PublicViewModel) : ListAdapter<Children, RecyclerView.ViewHolder>(PublicItemDiffCallback()) {

    /**
     * 左侧选中的item下标
     */
    private var mSelectedPosition = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = getItem(position)
        if (holder is ItemViewHolder) {
            holder.bind(itemData, viewModel)
        }
    }

    class ItemViewHolder private constructor(private val binding: AdapterPublicleftitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Children, viewModel: PublicViewModel) {
            binding.item = data
            binding.viewModel = viewModel
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterPublicleftitemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    /**
     * 改变item的选中状态
     */
    fun itemSelectSytle(selChild: Children) {
        val curIndex: Int? = viewModel.leftItems.value?.indexOf(selChild)
        viewModel.leftItems.value!!.get(mSelectedPosition).isSelect = false
        notifyItemChanged(mSelectedPosition)
        if (curIndex != null) {
            viewModel.leftItems.value!!.get(curIndex).isSelect = true
            notifyItemChanged(curIndex)
            mSelectedPosition = curIndex
        }

    }
}



// DiffUtil将自动为我们处理然后进行调用，其功能 就是比较两个数据集，用newList和oldList进行比较
class PublicItemDiffCallback : DiffUtil.ItemCallback<Children>() {
    override fun areItemsTheSame(oldItem: Children, newItem: Children): Boolean {
        //判断这个两个对象是否是同一个对象。
        return oldItem.id == newItem.id
    }

    //判断两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载
    override fun areContentsTheSame(oldItem: Children, newItem: Children): Boolean {
        return oldItem == newItem
    }

}