package com.hxzk.network.model

/**
 *作者：created by zjt on 2021/4/15
 *描述:
 */

data class UserInfoModel(
    val coinCount: Int,
    val rank: Int,
    val userId: Int,
    val username: String,
    val level: String
)