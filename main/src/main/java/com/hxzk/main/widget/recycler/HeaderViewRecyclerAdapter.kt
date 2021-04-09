package com.hxzk.main.widget.recycler

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 *作者：created by zjt on 2021/4/8
 *描述:https://blog.csdn.net/tiger_gy/article/details/90261832
 *
 */
class HeaderViewRecyclerAdapter(
    val headerViewInfos: ArrayList<View>?,
    val footerViewInfos: ArrayList<View>?,
    val adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        //也要划分三个区域
        val numHeaders: Int = getHeadersCount()
        if (position < numHeaders) {
            //是头部
            return
        }
        //adapter body
        val adjPosition = position - numHeaders
        var adapterCount = 0
        adapterCount = adapter!!.getItemCount()
        if (adjPosition < adapterCount) {
            adapter!!.onBindViewHolder(holder, adjPosition)
            return
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == RecyclerView.INVALID_TYPE) {
            //header
            return HeaderViewHolder(headerViewInfos!!.get(0))
        } else if (viewType == RecyclerView.INVALID_TYPE - 1) {
            //footer
            return HeaderViewHolder(footerViewInfos!!.get(0))
        }
            return adapter!!.onCreateViewHolder(parent, viewType)
    }

    override fun getItemCount(): Int {
        val footNum = footerViewInfos?.size ?: 0
        val headerNum = headerViewInfos?.size ?: 0

        if (adapter != null) {
            return footNum + headerNum + adapter.itemCount
        } else {
            return footNum + headerNum
        }


    }

    override fun getItemViewType(position: Int): Int {
        //判断当前条目是什么类型的---决定渲染什么视图给什么数据
        val numHeaders: Int = getHeadersCount()
        //是头部
        if (position < numHeaders) {
            return RecyclerView.INVALID_TYPE
        }
        //正常条目部分
        val adjPosition = position - numHeaders
        var adapterCount = adapter?.itemCount ?: 0
        if (adjPosition < adapterCount) {
            if (adapter != null) {
                return adapter.getItemViewType(adjPosition)
            }
        }
        //footer部分
        return RecyclerView.INVALID_TYPE - 1

    }

    fun getHeadersCount(): Int {
        return headerViewInfos?.size ?: 0
    }

    fun getFootersCount(): Int {
        return footerViewInfos?.size ?: 0
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }
}