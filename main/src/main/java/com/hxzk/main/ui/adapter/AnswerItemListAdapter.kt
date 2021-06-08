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
import com.hxzk.main.databinding.AdapterAnsweritemlistBinding
import com.hxzk.main.databinding.AdapterHomeitemBinding
import com.hxzk.main.ui.answer.AnswerViewModel
import com.hxzk.main.ui.system.sysitem.SystemItemViewModel
import com.hxzk.network.model.DataX
import com.hxzk.network.model.RankDataX
import kotlinx.android.synthetic.main.adapter_rankitem.view.*

/**
 * @author: hxzk_zjt
 * @date: 2021/5/7
 * 描述:积分排行适配器
 */
class AnswerItemListAdapter constructor(val viewModel : SystemItemViewModel) : ListAdapter<DataX,RecyclerView.ViewHolder>(IntegralDiffCallback()) {


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

class ItemViewHolder private constructor(private val binding : AdapterAnsweritemlistBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(data:DataX,viewModel:SystemItemViewModel){
        binding.item = data
        binding.viewModel = viewModel
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup) : RecyclerView.ViewHolder{
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AdapterAnsweritemlistBinding.inflate(layoutInflater, parent, false)
          return ItemViewHolder(binding)
        }
    }
}

}
