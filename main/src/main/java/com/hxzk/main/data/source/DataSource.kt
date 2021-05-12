package com.hxzk.main.data.source

import androidx.lifecycle.LiveData
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.SystemModel
import retrofit2.Call
import retrofit2.http.GET

/**
 *作者：created by zjt on 2021/3/11
 *描述:数据源接口(本地和网络公用)
 *
 */
interface DataSource {
    /**
     * 登录接口
     */
    suspend fun   login(account: String , pwd : String ): Result<*>
    /**
     * 登录接口
     */
    suspend fun register(username: String,password: String,repassword: String) : Result<*>
    /**
     * 首页Banner接口
     */
    suspend fun banner() : Result<*>
    /**
     * 首页置顶文章
     */
    suspend fun topArticle() : Result<*>
    /**
     * 首页文章列表
     */
    suspend fun articleList(pageIndex : Int) : Result<*>

    /**
     * 获取个人积分接口
     */
    suspend fun integral(): Result<*>

    /**
     * 获取个人积分列表接口
     */
    suspend fun integralList(pageIndex : Int): Result<*>
    /**
     * 问答别表数据接口
     */
    suspend  fun answerList(pageIndex: Int) :Result<*>

    /**
     * 体系列表
     */

    suspend fun treeList(): Result<*>
    /**
     * 搜索热词
     */
 suspend fun  hotKeys():Result<*>
}