package com.hxzk.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.callback.BannerItemListener
import com.hxzk.main.databinding.AdapterHomeitemBinding
import com.hxzk.main.databinding.BannerFragmentBinding
import com.hxzk.main.ui.home.HomeViewModel
import com.hxzk.network.model.DataX
import com.hxzk.network.model.HomeBanner

/**
 *作者：created by zjt on 2021/4/6
 *描述:首页Banner的Adapter
 *
 */
class HomeItemAdapter(private val homeViewModel: HomeViewModel) :
    ListAdapter<DataX, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    val ITEM_TYEP_BANNER = 0
    val ITEM_TYEP_NORMAL = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYEP_BANNER){
            BannerViewHolder.from(parent,mBannerItemListener)
        }
        else {
            ItemViewHolder.from(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == ITEM_TYEP_BANNER) {
            (holder as BannerViewHolder).bind(homeViewModel)
            return
        } else {
            val item = getItem(position - 1)
            if (holder is ItemViewHolder) {
                holder.bind(homeViewModel, item)
            }
        }
    }

    override fun getItemCount(): Int {
        return homeViewModel._topArticleSize.value?.plus(1) ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ITEM_TYEP_BANNER else ITEM_TYEP_NORMAL
    }

    lateinit var mBannerItemListener: BannerItemListener
    fun setBannerItemListener(listener: BannerItemListener) {
        mBannerItemListener = listener
    }




class ItemViewHolder private constructor(private val binding: AdapterHomeitemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(viewModel: HomeViewModel, item: DataX) {
        //此处将布局中的data赋值
        binding.viewModel = viewModel
        binding.topArticle = item
        //executePendingBindings它使数据绑定刷新所有挂起的更改
        binding.executePendingBindings()
    }


    //companion object中声明的变量类似于Java中的静态变量,只在类里面能定义一次
    //使用 @JvmStatic 使Companion object 的成员真正成为静态成员
    //companion object是当包含它的类被加载时就初始化了的，这一点和Java的static还是一样的
    companion object {
        fun from(parent: ViewGroup): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AdapterHomeitemBinding.inflate(layoutInflater, parent, false)
            return ItemViewHolder(binding)
        }
    }
}




  class BannerViewHolder private constructor(private val binding: BannerFragmentBinding,val listener :BannerItemListener) :
    RecyclerView.ViewHolder(binding.root) {
    fun bind(viewModel: HomeViewModel) {
        binding.viewModel = viewModel
        binding.executePendingBindings()
        binding.mBanner.setOnBannerListener { data, position ->
            listener.onItemClick((data as  HomeBanner),position)
        }
    }

     companion object {
        fun from(parent: ViewGroup, listener :BannerItemListener): BannerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = BannerFragmentBinding.inflate(layoutInflater, parent, false)
            return BannerViewHolder(binding,listener)
        }
    }
}

}

// DiffUtil将自动为我们处理然后进行调用，其功能 就是比较两个数据集，用newList和oldList进行比较
class TaskDiffCallback : DiffUtil.ItemCallback<DataX>() {
    override fun areItemsTheSame(oldItem: DataX, newItem: DataX): Boolean {
        //判断这个两个对象是否是同一个对象。
        return oldItem.id == newItem.id
    }

    //判断两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载
    override fun areContentsTheSame(oldItem: DataX, newItem: DataX): Boolean {
        return oldItem == newItem
    }

}
