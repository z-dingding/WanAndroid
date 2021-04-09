package com.hxzk.main.callback

import com.hxzk.network.model.HomeBanner

/**
 *作者：created by zjt on 2021/4/9
 *描述:
 *
 */
interface BannerItemListener {
    fun onItemClick(data : HomeBanner ,position : Int)
}