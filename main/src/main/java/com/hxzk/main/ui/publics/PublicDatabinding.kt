package com.hxzk.main.ui.publics

import android.widget.LinearLayout
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hxzk.main.R
import com.hxzk.main.ui.adapter.NavigationItemAdapter
import com.hxzk.main.ui.adapter.PublicLeftAdapter
import com.hxzk.main.ui.adapter.PublicRightAdapter
import com.hxzk.network.model.ArticleListModel
import com.hxzk.network.model.Children
import com.hxzk.network.model.NavigationModel

/**
 * @author: hxzk_zjt
 * @date: 2021/5/14
 * 描述:
 */

/**
 * 公众号左侧列表的展示数据源
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<Children>?) {
    items?.let {
        //submitList()更新现有列表
        (listView.adapter as PublicLeftAdapter).submitList(items)
    }
}


/**
 * 公众号左侧RV的Item点击后背景的改变
 */
@BindingAdapter("isSlecect")
fun setIsSlecect(linearLayout: LinearLayout, isSelect : Boolean) {
      if(isSelect){
          linearLayout.setBackgroundResource(R.color.public_leftrv_bg_sel)
      }else{
          linearLayout.setBackgroundResource(R.color.public_leftrv_bg_nor)
      }
}

