package com.hxzk.network.model

/**
 * @author: hxzk_zjt
 * @date: 2021/5/8
 * 描述:
 */
data class AnswerModel(
    val curPage: Int,
    val datas: List<DataX>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int

)

