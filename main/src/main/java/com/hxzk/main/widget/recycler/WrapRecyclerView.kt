package com.hxzk.main.widget.recycler

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 *作者：created by zjt on 2021/4/8
 *描述:包装一个RecyclerView,包含头部和尾部
 *
 */
class WrapRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    private val mHeaderViewInfos: ArrayList<View> = ArrayList<View>()
    private val mFooterViewInfos = ArrayList<View>()
    private var mAdapter: Adapter<ViewHolder>? = null


    fun addHeaderView(view: View) {
        mHeaderViewInfos.add(view)
        // 如果适配器尚未包装，请将其包装
        if (mAdapter != null) {
            if (!(mAdapter is HeaderViewRecyclerAdapter)) {
                mAdapter =
                     HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, mAdapter!!)
            }
        }
    }

    fun addFooterView(view: View) {
        mFooterViewInfos.add(view)
        if (mAdapter!=null){
            if (!(mAdapter is HeaderViewRecyclerAdapter)){
                mAdapter= HeaderViewRecyclerAdapter(mHeaderViewInfos,mFooterViewInfos,mAdapter!!)
            }
        }
    }


    override fun setAdapter(adapter: Adapter<ViewHolder>?) {
        super.setAdapter(adapter)
        if (mHeaderViewInfos.size > 0|| mFooterViewInfos.size > 0) {
            mAdapter =  HeaderViewRecyclerAdapter(mHeaderViewInfos, mFooterViewInfos, adapter!! )
        } else {
            mAdapter = adapter
        }
        super.setAdapter(mAdapter)
    }

}