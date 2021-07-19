package com.hxzk.main.data.source

import androidx.lifecycle.LiveData
import com.hxzk.network.Result
import com.hxzk.network.model.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

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
     * 体系下单个item的列表
     */
    suspend fun systemItemList(pageIndex : Int,cId:Int):Result<*>
    /**
     * 导航列表
     */
    suspend fun navigaiontList():Result<*>
    /**
     * 搜索热词
     */
    suspend fun  hotKeys():Result<*>
    /**
     * 微信公众号列表
     */
    @GET("wxarticle/chapters/json")
    suspend fun  wxPublic():Result<*>
    /**
     * 查看某公众号列表
     */
   suspend fun wxPublicArticle( publicId: Int,  pageIndex: Int):Result<*>

    /**
     * 插入单个阅读历史记录
     */
   suspend fun insertItem(model : CommonItemModel)

    /**
     * 查询浏览历史记录
     */
     fun queryBrowseItems():LiveData<Result<List<CommonItemModel>>>
    /**
     * 清空所有历史记录
     */
    suspend fun delALLBrowsingHistory()
    /**
     * 删除所有的热词表所有内容
     */
    suspend fun  delAllHotwords()
    /**
     * 插入单个热词条目
     */
    suspend fun  insertHotword(item: HotKeyModel)
    /**
     * 查询热词表所有的条目
     */
     fun queryAllHotwords():Result<List<HotKeyModel>>
    /**
     * 插入历史搜索单条数据
     */
    suspend fun  insertSearchKeyWord(item : SearchKeyWord)
    /**
     * 查询历史搜索记录数据
     */
     fun queryAllKeyWord():LiveData<List<SearchKeyWord>>
    /**
     * 根据关键字进行网络请求
     */
    suspend fun searchByKey(keyWord : String,pageIndex:Int):Result<*>
    /**
     * 清空本地所有的搜索记录
     */
    suspend fun delAllSearchKeys()
    /**
     * 收藏列表文章
     */
    suspend  fun collecteArticle(id : Int):Result<*>
    /**
     * 查询收藏列表
     */
     suspend  fun collectList(pageNum : Int) :Result<*>
    /**
     * 取消收藏
     */
    suspend fun unCollection(id : Int,originId : Int) : Result<*>

}