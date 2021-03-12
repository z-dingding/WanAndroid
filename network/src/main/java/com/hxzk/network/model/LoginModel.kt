package com.hxzk.network.model

/**
 *作者：created by zjt on 2021/3/12
 *描述:
 *
 */
data class LoginModel(

    val admin: Boolean,
    val chapterTops: List<Any>,
    val coinCount: Int,
    val collectIds: List<Int>,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String
)