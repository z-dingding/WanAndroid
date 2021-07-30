package com.hxzk.main.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.network.model.DataX

/**
 * @author: hxzk_zjt
 * @date: 2021/7/27
 * 描述:
 */
class ShareAdapter constructor( val context: Context,val datas : ArrayList<DataX>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    val  mContext: Context = context
     val   itemDatas = datas

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShareViewHolder.from(parent,mContext)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder , position: Int) {
        val item = itemDatas.get(position)
        if (holder is ShareViewHolder) {
            holder.bind(item, position, mListener)
        }
    }

    override fun getItemCount(): Int {
        return if(itemDatas.size > 0)  itemDatas.size else  0
    }


    /**
     * Item点击事件
     */
    interface ItemClickListener {
        fun itemClick(pos: Int, item: DataX)
        fun itemDelClick(pos: Int, isCheck: Boolean)
    }
    lateinit var mListener: ItemClickListener
    fun setItemClickListener(listener: ItemClickListener) {
        mListener = listener
    }


}

 class ShareViewHolder(val binding: View) : RecyclerView.ViewHolder(binding) {
    val rootView: ConstraintLayout = binding.findViewById(R.id.rootView)
    val cbDel: AppCompatCheckBox = binding.findViewById(R.id.cb_delitem)
    val author: TextView = binding.findViewById(R.id.author)
    val tags: TextView = binding.findViewById(R.id.tags)
    val niceDate: TextView = binding.findViewById(R.id.niceDate)
    val title: TextView = binding.findViewById(R.id.title)
    val capterName: TextView = binding.findViewById(R.id.capterName)
    val collection: ImageView = binding.findViewById(R.id.iv_collection)

    fun bind(item: DataX, pos: Int, mListener: ShareAdapter.ItemClickListener) {
        //此处将布局中的data赋值
        author.text = item.shareUser
        val tag = if (item.tags.isNotEmpty()) item.tags.get(0).name else GlobalUtil.getString(R.string.home_tag_new)
        tags.text = tag
        niceDate.text = item.niceDate
        title.text = item.title
        capterName.text = item.chapterName
        if (item.collect) {
            collection.visibility = View.VISIBLE
            Glide.with(binding.context).load(R.drawable.icon_collection).into(collection)
        } else {
            collection.visibility = View.GONE
        }
        cbDel.isChecked = item.isDelShare
        rootView.setOnClickListener {
            mListener.itemClick(pos, item)
        }
        cbDel.setOnCheckedChangeListener { _, isChecked ->
            //如果选中就回调
                mListener.itemDelClick(pos, isChecked)
        }
    }


    //companion object中声明的变量类似于Java中的静态变量,只在类里面能定义一次
    //使用 @JvmStatic 使Companion object 的成员真正成为静态成员
    //companion object是当包含它的类被加载时就初始化了的，这一点和Java的static还是一样的
    companion object {
        fun from(parent: ViewGroup,context: Context): RecyclerView.ViewHolder {
            val layoutInflater = LayoutInflater.from(context)
            val binding = layoutInflater.inflate(R.layout.adapter_share_item, parent, false)
            return ShareViewHolder(binding)
        }
    }




}