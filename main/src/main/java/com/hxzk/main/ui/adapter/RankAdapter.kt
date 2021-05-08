package com.hxzk.main.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.R
import com.hxzk.network.model.DataX
import com.hxzk.network.model.RankDataX
import kotlinx.android.synthetic.main.adapter_rankitem.view.*

/**
 * @author: hxzk_zjt
 * @date: 2021/5/7
 * 描述:积分排行适配器
 */
class RankAdapter : ListAdapter<RankDataX,RecyclerView.ViewHolder>(DiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val itemData = getItem(position)
        if (holder is ItemViewHolder) {
            holder.bind(itemData)
        }
    }


    override fun submitList(list: List<RankDataX>?) {
            super.submitList(if (list != null) ArrayList(list) else null)
    }

class ItemViewHolder private constructor(private val mItemView : View): RecyclerView.ViewHolder(mItemView){

    fun bind(data:RankDataX){
        mItemView.stvItem.setLeftString(data.rank)
        mItemView.stvItem.setCenterString(data.username)
        mItemView.stvItem.setRightString(data.coinCount.toString())
    }

    companion object {
        fun from(parent: ViewGroup) : RecyclerView.ViewHolder{
            val view =LayoutInflater.from(parent.context).inflate(R.layout.adapter_rankitem,parent,false)
          return ItemViewHolder(view)
        }
    }
}

}

