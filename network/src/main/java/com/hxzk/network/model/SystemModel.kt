package com.hxzk.network.model

/**
 * @author: hxzk_zjt
 * @date: 2021/5/11
 * 描述:
 */
data class SystemModel(
    val children: List<Children>,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)

data class Children(
        var children: List<DataX>,
        val courseId: Int,
        val id: Int,
        val name: String,
        val order: Int,
        val parentChapterId: Int,
        val userControlSetTop: Boolean,
        val visible: Int,
        //人为增加一个isSelect属性
        var isSelect :Boolean
)