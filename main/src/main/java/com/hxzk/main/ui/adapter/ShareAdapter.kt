package com.hxzk.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.callback.BannerItemListener
import com.hxzk.main.databinding.AdapterHomeitemBinding
import com.hxzk.main.databinding.AdapterShareItemBinding
import com.hxzk.main.ui.home.HomeViewModel
import com.hxzk.network.model.DataX
import com.hxzk.network.model.RankDataX

/**
 * @author: hxzk_zjt
 * @date: 2021/7/27
 * 描述:
 */
class ShareAdapter : androidx.recyclerview.widget.ListAdapter<DataX,RecyclerView.ViewHolder>(IntegralDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position )
        if (holder is ItemViewHolder) {
            holder.bind(item,position,mListener)
        }
    }


    class ItemViewHolder(private val binding: AdapterShareItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DataX, pos : Int,mListener: ItemClickListener) {
            //此处将布局中的data赋值
            binding.item = item
            binding.pos = pos
            binding.rootView.setOnClickListener {
                mListener.itemClick(pos,item)
            }
            //executePendingBindings它使数据绑定刷新所有挂起的更改
            binding.executePendingBindings()
        }


        //companion object中声明的变量类似于Java中的静态变量,只在类里面能定义一次
        //使用 @JvmStatic 使Companion object 的成员真正成为静态成员
        //companion object是当包含它的类被加载时就初始化了的，这一点和Java的static还是一样的
        companion object {
            fun from(parent: ViewGroup): RecyclerView.ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterShareItemBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }


    interface ItemClickListener{
        fun  itemClick(pos : Int ,item : DataX)
    }
    lateinit var mListener: ItemClickListener
    fun setItemClickListener(listener: ItemClickListener) {
        mListener = listener
    }



}