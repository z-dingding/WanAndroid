package com.hxzk.network.model

/**
 * @author: hxzk_zjt
 * @date: 2021/7/30
 * 描述:
 */
data class NotifyModel(
    val curPage: Int,
    val datas: List<DataX2>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)

data class DataX2(
    val category: Int,
    val date: Long,
    val fromUser: String,
    val fromUserId: Int,
    val fullLink: String,
    val id: Int,
    val isRead: Int,
    val link: String,
    val message: String,
    val niceDate: String,
    val tag: String,
    val title: String,
    val userId: Int
)