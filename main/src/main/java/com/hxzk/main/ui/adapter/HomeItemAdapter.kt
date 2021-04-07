package com.hxzk.main.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.databinding.AdapterHomeitemBinding
import com.hxzk.main.ui.home.HomeViewModel
import com.hxzk.network.model.TopArticleModel

/**
 *作者：created by zjt on 2021/4/6
 *描述:首页Banner的Adapter
 *
 */
class HomeItemAdapter(private  val homeViewModel: HomeViewModel) : ListAdapter<TopArticleModel, HomeItemAdapter.ViewHolder>(TaskDiffCallback()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return  ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(homeViewModel,item)
    }



    class ViewHolder private constructor(val binding: AdapterHomeitemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel:HomeViewModel , item: TopArticleModel) {
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
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterHomeitemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }


}

//DiffUtil在 support library 25.1.0 的时候就引入了，最主要的功能就是处理adapter的更新，其功能 就是比较两个数据集，用newList和oldList进行比较
// DiffUtil将自动为我们处理然后进行调用，拒绝无脑调用notifyDataSetChanged()
class TaskDiffCallback : DiffUtil.ItemCallback<TopArticleModel>() {
    override fun areItemsTheSame(oldItem: TopArticleModel, newItem: TopArticleModel): Boolean {
        //判断这个两个对象是否是同一个对象。
        return oldItem.id == newItem.id
    }

    //判断两个对象的内容是否一致，如果不一致，那么 它就将对列表进行重绘和动画加载
    override fun areContentsTheSame(oldItem: TopArticleModel, newItem: TopArticleModel): Boolean {
        return oldItem == newItem
    }
}
