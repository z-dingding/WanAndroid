package com.hxzk.main.callback

import com.hxzk.main.ui.search.SearchViewModel
import com.hxzk.network.model.Children
import com.hxzk.network.model.HotKeyModel
import com.hxzk.network.model.SearchKeyWord

/**
 * @author: hxzk_zjt
 * @date: 2021/5/11
 * 描述:体系点击Itme项的回调
 */
interface HotFlexItemClickListener {
   fun onItemClick(item : HotKeyModel)
}


interface SearchFlexItemClickListener {
   fun onItemClick(item : SearchKeyWord)
}
