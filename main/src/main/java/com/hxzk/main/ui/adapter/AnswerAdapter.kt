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
import com.hxzk.main.ui.answer.AnswerViewModel
import com.hxzk.network.model.DataX
import com.hxzk.network.model.RankDataX
import kotlinx.android.synthetic.main.adapter_rankitem.view.*

/**
 * @author: hxzk_zjt
 * @date: 2021/5/7
 * 描述:积分排行适配器
 */
class AnswerAdapter constructor(val viewModel : AnswerViewModel) : ListAdapter<DataX,RecyclerView.ViewHolder>(IntegralDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = getItem(position)
        if (holder is ItemViewHolder) {
            holder.bind(itemData,viewModel)
        }
    }


    override fun submitList(list: List<DataX>?) {
   super.submitList(if (list != null) ArrayList(list) else null)
    }

class ItemViewHolder private constructor(private val binding : AdapterAnsweritemBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(data:DataX,viewModel:AnswerViewModel){
        binding.item = data
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup) : RecyclerView.ViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AdapterAnsweritemBinding.inflate(layoutInflater, parent, false)
          return ItemViewHolder(binding)
        }
    }
}

}


// DiffUtil将自动为我们处理然后进行调用，其功能 就是比较两个数据集，用newList和oldList进行比较
class IntegralDiffCallback : DiffUtil.ItemCallback<DataX>() {
    override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
        //判断这个两个对象是否是同一个对象。
        return oldItem.id == newItem.id
    }

    //判断两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载
    override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
        return oldItem == newItem
    }

}
