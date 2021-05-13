package com.hxzk.main.callback

import com.hxzk.network.model.Article
import com.hxzk.network.model.Children

/**
 * @author: hxzk_zjt
 * @date: 2021/5/11
 * 描述:体系点击Itme项的回调
 */
interface NavFlexItemClickListener {
   fun onItemClick(item : Article)
}


