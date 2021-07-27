package com.hxzk.network.model

/**
 * @author: hxzk_zjt
 * @date: 2021/7/27
 * 描述:
 */
data class ShareListItemModel(
    val coinInfo: CoinInfo,
    val shareArticles: ShareArticles
)

data class CoinInfo(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
)

data class ShareArticles(
    val curPage: Int,
    val datas: List<DataX>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

