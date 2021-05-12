package com.hxzk.main.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.callback.BannerItemListener
import com.hxzk.main.callback.FlexItemClickListener
import com.hxzk.main.databinding.AdapterHomeitemBinding
import com.hxzk.main.databinding.AdapterSysitemBinding
import com.hxzk.main.databinding.BannerFragmentBinding
import com.hxzk.main.ui.home.HomeViewModel
import com.hxzk.main.ui.system.child_sys.ChildSystemViewModel
import com.hxzk.network.model.DataX
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.model.SystemModel

/**
 *作者：created by zjt on 2021/4/6
 *描述:首页Banner的Adapter
 *
 */
class SystemItemAdapter(private val viewModel: ChildSystemViewModel) :
    ListAdapter<SystemModel, RecyclerView.ViewHolder>(SystemDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
          return  ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position )
            if (holder is ItemViewHolder) {
                holder.bind(viewModel, item,mListener)
            }
    }

    private lateinit var mListener : FlexItemClickListener
   fun  setFlexItemClickListener(listener : FlexItemClickListener){
        mListener= listener
    }
}

class ItemViewHolder private constructor(private val binding: AdapterSysitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: ChildSystemViewModel, item: SystemModel,mListener : FlexItemClickListener) {
        //此处将布局中的data赋值
        binding.viewModel = viewModel
        binding.item = item
        binding.listener= mListener
        //executePendingBindings它使数据绑定刷新所有挂起的更改
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AdapterSysitemBinding.inflate(layoutInflater, parent, false)
            return ItemViewHolder(binding)
        }
    }
}
// DiffUtil将自动为我们处理然后进行调用，其功能 就是比较两个数据集，用newList和oldList进行比较
class SystemDiffCallback : DiffUtil.ItemCallback<SystemModel>() {
    override fun areItemsTheSame(oldItem: SystemModel, newItem: SystemModel): Boolean {
        //判断这个两个对象是否是同一个对象。
        return oldItem.id == newItem.id
    }

    //判断两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载
    override fun areContentsTheSame(oldItem: SystemModel, newItem: SystemModel): Boolean {
        return oldItem == newItem
    }
}
