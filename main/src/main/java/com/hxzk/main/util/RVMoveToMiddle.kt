package com.hxzk.main.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * @author: hxzk_zjt
 * @date: 2021/5/17
 * 描述:RecycclerVeiw的指定item移动到页面中间位置
 */
object RVMoveToMiddle {

    fun moveToModdle(rv:RecyclerView, clickPos:Int){
        //先从RV中获取当前RV的可见的第一项和最后一项item的position
    val firstItemPos = (rv.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
    val lastItemPos = (rv.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        //算出中间位置Item的位置
        val middle = (firstItemPos + lastItemPos)/2
        //计算当前索引和中间位置的差,作为需要滑动的距离(中间隔了几个item)
        //>0点击的item在中间位置的后面,反之在前面
        val index :Int = if( (clickPos - middle) >= 0 ) (clickPos - middle)  else  -(clickPos - middle)
        if(clickPos < middle){
            //如果当前位置在中间位置的前面,往下移动
            rv.scrollBy(0,-rv.getChildAt(index).top)
        }else{
            //如果当前位置在中间位置的后面,往上移动
            rv.scrollBy(0,rv.getChildAt(index).top)
        }
    }

}