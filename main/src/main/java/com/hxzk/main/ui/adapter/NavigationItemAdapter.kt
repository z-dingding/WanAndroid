package com.hxzk.main.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.callback.BannerItemListener
import com.hxzk.main.callback.FlexItemClickListener
import com.hxzk.main.callback.NavFlexItemClickListener
import com.hxzk.main.databinding.*
import com.hxzk.main.ui.home.HomeViewModel
import com.hxzk.main.ui.system.child_nav.ChildNavigationViewModel
import com.hxzk.main.ui.system.child_sys.ChildSystemViewModel
import com.hxzk.network.model.DataX
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.model.NavigationModel
import com.hxzk.network.model.SystemModel

/**
 *作者：created by zjt on 2021/4/6
 *描述:体系的Adapter
 *
 */
class NavigationItemAdapter(private val viewModel: ChildNavigationViewModel) :
    ListAdapter<NavigationModel, RecyclerView.ViewHolder>(NavDiffCallback()) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
          return  ItemViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val item = getItem(position )
            if (holder is ItemViewHolder) {
                holder.bind(viewModel, item,mListener)
            }
    }

    private lateinit var mListener : NavFlexItemClickListener
   fun  setNavFlexItemClickListener(listener : NavFlexItemClickListener){
        mListener= listener
    }
}

class ItemViewHolder(private val binding: AdapterNavitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: ChildNavigationViewModel, item: NavigationModel,mListener : NavFlexItemClickListener) {
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
            val binding = AdapterNavitemBinding.inflate(layoutInflater, parent, false)
            return ItemViewHolder(binding)
        }
    }
}
// DiffUtil将自动为我们处理然后进行调用，其功能 就是比较两个数据集，用newList和oldList进行比较
class NavDiffCallback : DiffUtil.ItemCallback<NavigationModel>() {
    override fun areItemsTheSame(oldItem: NavigationModel, newItem: NavigationModel): Boolean {
        //判断这个两个对象是否是同一个对象。
        return oldItem.cid == newItem.cid
    }

    //判断两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载
    override fun areContentsTheSame(oldItem: NavigationModel, newItem: NavigationModel): Boolean {
        return oldItem == newItem
    }
}
