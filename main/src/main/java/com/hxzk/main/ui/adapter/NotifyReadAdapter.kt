package com.hxzk.main.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.network.model.DataX
import com.hxzk.network.model.DataX2

/**
 * @author: hxzk_zjt
 * @date: 2021/7/30
 * 描述:
 */
class NotifyReadAdapter( val context: Context,val datas : ArrayList<DataX2>) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    val  mContext: Context = context
    val   itemDatas = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NotifyViewHolder.from(parent,mContext)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemDatas.get(position)
        if (holder is NotifyViewHolder) {
            holder.bind(item)
        }
    }

    override fun getItemCount(): Int {
        return if(itemDatas.size > 0)  itemDatas.size else  0
    }
}

class NotifyViewHolder(val binding: View) : RecyclerView.ViewHolder(binding) {
    val author: TextView = binding.findViewById(R.id.author)
    val tags: TextView = binding.findViewById(R.id.tags)
    val niceDate: TextView = binding.findViewById(R.id.niceDate)
    val title: TextView = binding.findViewById(R.id.title)
    val message: TextView = binding.findViewById(R.id.message)


    fun bind(item: DataX2) {
        //此处将布局中的data赋值
        author.text = item.fromUser
        val tag = if (item.tag.isNotEmpty()) item.tag else GlobalUtil.getString(R.string.home_tag_new)
        tags.text = tag
        niceDate.text = item.niceDate
        title.text = item.title
        message.text = item.message
    }


    //companion object中声明的变量类似于Java中的静态变量,只在类里面能定义一次
    //使用 @JvmStatic 使Companion object 的成员真正成为静态成员
    //companion object是当包含它的类被加载时就初始化了的，这一点和Java的static还是一样的
    companion object {
        fun from(parent: ViewGroup, context: Context): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val binding = layoutInflater.inflate(R.layout.adapter_notifylist_item, parent, false)
            return NotifyViewHolder(binding)
        }
    }

}