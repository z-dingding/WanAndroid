package com.hxzk.main.data.source

import androidx.lifecycle.LiveData
import com.hxzk.network.Result
import com.hxzk.network.model.ArticleListModel

/**
 *作者：created by zjt on 2021/3/11
 *描述:
 *
 */
interface Repository {

    /**
     * 登录接口
     */
    fun login(account: String, pwd: String): LiveData<Result<*>>

    /**
     * 注册接口
     */
    fun registerRequest(username: String, password: String, repassword: String): LiveData<Result<*>>

    /*
    * 首页Banner接口
    */
    fun banner(): LiveData<Result<*>>
    /**
     * 首页文章列表
     */
    suspend fun articleList(pageIndex: Int,origin: ArticleListModel?): ArticleListModel
    /**
     * 获取个人积分接口
     */
   fun integral(): LiveData<Result<*>>
}