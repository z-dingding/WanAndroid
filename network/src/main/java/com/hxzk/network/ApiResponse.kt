package com.hxzk.network

/**
 *作者：created by zjt on 2021/3/12
 *描述:通用的响应实体
 *
 */
class ApiResponse<T>(
    var data: T? ,
    var errorCode : Int,
    var errorMsg: String
)