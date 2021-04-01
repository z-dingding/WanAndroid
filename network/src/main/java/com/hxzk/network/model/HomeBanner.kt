package com.hxzk.network.model

/**
 *作者：created by zjt on 2021/4/1
 *描述:
 *
 */
data class HomeBanner(
    val `data`: List<Data>,
)

data class Data(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)