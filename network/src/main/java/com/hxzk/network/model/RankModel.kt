package com.hxzk.network.model

/**
 * @author: hxzk_zjt
 * @date: 2021/5/7
 * 描述:排行榜
 */
data class RankModel(
    val curPage: Int,
    val datas: List<RankDataX>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class RankDataX(
    val coinCount: Int,
    val level: Int,
    val nickname: String,
    val rank: String,
    val userId: Int,
    val username: String
)