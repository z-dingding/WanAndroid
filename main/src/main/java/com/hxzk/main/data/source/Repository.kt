package com.hxzk.main.data.source

import androidx.lifecycle.LiveData
import com.hxzk.network.Result
import com.hxzk.network.model.*
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

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

    /**
    * 首页Banner接口
    */
    fun banner(): LiveData<Result<*>>
    /**
     * 首页文章列表
     */
     fun articleList(pageIndex: Int,origin: ArticleListModel?):  LiveData<ArticleListModel>
    /**
     * 获取个人积分(信息)接口
     */
   fun integral(): LiveData<Result<*>>

    /**
     * 获取个人积分列表接口
     */
    fun integralList(pageIndex : Int): LiveData<Result<*>>

    /**
     * 问答别表数据接口
     */
     fun answerList(pageIndex: Int) : LiveData<Result<*>>

    /**
     * 体系列表
     */
    fun treeList(): LiveData<Result<*>>

    /**
     * 体系下文章列表
     */
    fun systemItemList(pageIndex : Int,cId:Int):LiveData<Result<*>>
    /**
     * 搜索热词
     */
    fun hotKeys():LiveData<Result<*>>

    /**
     * 导航列表
     */
    fun navigaiontList():LiveData<Result<*>>

    /**
     * 微信公众号列表
     */
    @GET("wxarticle/chapters/json")
    fun  wxPublic():LiveData<Result<*>>
    /**
     * 查看某公众号列表
     */
     fun wxPublicArticle( lsit:List<Int>) : LiveData<List<Any>>
    /**
     * 插入单个阅读历史记录
     */
    suspend fun insertItem(model : CommonItemModel)

    /**
     * 查询浏览历史记录(查询不能用suspend,不然不能用Result)
     */
     fun queryBrowseItems():LiveData<Result<List<CommonItemModel>>>
    /**
     * 清空所有历史记录
     */
    suspend fun delALLBrowsingHistory()

    /**
     * 插入单个热词条目
     */
    suspend fun  insertHotword(item:HotKeyModel)
    /**
     * 删除所有的热词表所有内容
     */
    suspend fun  delAllHotwords()

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
    fun searchByKey(keyWord : String,pageIndex:Int):LiveData<Result<*>>
    /**
     * 清空本地所有的搜索记录
     */
    suspend fun delAllSearchKeys()

}