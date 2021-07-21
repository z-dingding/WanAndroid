package com.hxzk.main.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.hxzk.base.extension.logDebug
import com.hxzk.base.extension.sToast
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.*
import com.hxzk.network.succeeded
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import retrofit2.Call

/**
 *作者：created by zjt on 2021/3/11
 *描述:统一调用入口
 *
 */
class DefaultRepository(
    private val localSource: DataSource,
    private val romtat: DataSource,
    private val ioDispatch: CoroutineDispatcher = Dispatchers.IO
) : Repository {

    //liveData是livedata-ktx的新技巧，自动构建并返回一个livedata对象
    //在代码块中提供一个挂起函数的上下文，这样我们在代码块中可以调用任意的挂起函数
    override fun login(account: String, pwd: String) = liveData(ioDispatch) {
        val result = romtat.login(account, pwd)
        emit(result)
    }

    override fun registerRequest(username: String, password: String, repassword: String) =
        liveData(ioDispatch) {
            val result = romtat.register(username, password, repassword)
            emit(result)
        }

    override fun banner() = liveData {
        val result = romtat.banner()
        emit(result)
    }

    override fun articleList(pageIndex: Int, origin: ArticleListModel?): LiveData<ArticleListModel> = liveData {
        emit( withContext(ioDispatch) {
        lateinit var articleListModel: ArticleListModel
        //刷新
        if (pageIndex == 0) {
            //首页列表
            val result = async { romtat.articleList(pageIndex) }
            //首页置顶
            val result2 = async { romtat.topArticle() }
            val res2 = result2.await()
            if (res2.succeeded) {
                val bean = (res2 as Result.Success<*>).res as ApiResponse<*>
                if (bean.errorCode == 0) {
                    val list = bean.data as ArrayList<DataX>
                        articleListModel = ArticleListModel(0, list, 0, false, 0, 0, 0)
                } else {
                    bean.errorMsg.sToast()
                }
            } else {
                ResponseHandler.handleFailure((res2 as Result.Error).e)
            }
            //列表内容
            val res1 = result.await()
            if (res1.succeeded) {
                val bean = (res1 as Result.Success<*>).res as ApiResponse<*>
                if (bean.errorCode == 0) {
                    val model = bean.data as ArticleListModel
                    articleListModel.curPage = model.curPage
                    articleListModel.offset = model.offset
                    articleListModel.over = model.over
                    articleListModel.pageCount = model.pageCount
                    articleListModel.size = model.size
                    articleListModel.total = model.total
                    model.datas.forEach {
                        articleListModel.datas.add(it)
                    }
                } else {
                    bean.errorMsg.sToast()
                }
            } else {
                ResponseHandler.handleFailure((res1 as Result.Error).e)
            }
        } else {
            //加载更多
            val deferred = async { romtat.articleList(pageIndex)}
            val  it = deferred.await()
            if (it.succeeded) {
                val bean = (it as Result.Success<*>).res as ApiResponse<*>
                if (bean.errorCode == 0) {
                    val model = (bean.data as ArticleListModel)
                    //将请求到的数据，放到到之前的livedata中
                    articleListModel = origin!!
                    articleListModel.curPage = model.curPage
                    articleListModel.offset = model.offset
                    articleListModel.over = model.over
                    articleListModel.pageCount = model.pageCount
                    articleListModel.size = model.size
                    articleListModel.total = model.total
                    model.datas.forEach {
                        articleListModel.datas.add(it)
                    }
                } else {
                    bean.errorMsg.sToast()
                }
            } else {
                val res = it as Result.Error
                ResponseHandler.handleFailure(res.e)
            }
        }
        articleListModel
    })
    }


    override fun integral()= liveData {
        val result = romtat.integral()
        emit(result)
    }

    override fun integralList(pageIndex : Int): LiveData<Result<*>> = liveData {
        val result = romtat.integralList(pageIndex)
        emit(result)
    }

    override  fun answerList(pageIndex: Int): LiveData<Result<*>> = liveData {
        val result = romtat.answerList(pageIndex)
        emit(result)
    }

    override fun treeList() : LiveData<Result<*>> = liveData {
        val result = romtat.treeList()
        emit(result)
    }

    override fun systemItemList(pageIndex : Int,cId:Int): LiveData<Result<*>> = liveData {
        val result = romtat.systemItemList(pageIndex,cId)
        emit(result)
    }

    override fun hotKeys(): LiveData<Result<*>> = liveData {
        val result = romtat.hotKeys()
        emit(result)
    }

    override fun navigaiontList(): LiveData<Result<*>>  = liveData {
        val result = romtat.navigaiontList()
        emit(result)
    }

    override fun wxPublic()= liveData {
        val result = romtat.wxPublic()
        emit(result)
    }


    lateinit var result:List<Any>
    override  fun wxPublicArticle(lsit:List<Int>)= liveData{
       result = ArrayList()
        lsit.forEach {
           //只请求第一页数据
            (result as ArrayList<Any>).add(romtat.wxPublicArticle(it,1))
        }
        emit(result)
    }

    override suspend fun insertItem(model: CommonItemModel) {
        localSource.insertItem(model)
    }

    override  fun queryBrowseItems(): LiveData<Result<List<CommonItemModel>>> {
        return localSource.queryBrowseItems()
    }

    override suspend fun delALLBrowsingHistory() {
        localSource.delALLBrowsingHistory()
    }

    override suspend fun insertHotword(item:HotKeyModel) {
        localSource.insertHotword(item)
    }

    override suspend fun delAllHotwords() {
        localSource.delAllHotwords()
    }

    override  fun queryAllHotwords(): Result<List<HotKeyModel>>{
       return localSource.queryAllHotwords()
    }

    override suspend fun insertSearchKeyWord(item: SearchKeyWord) {
       localSource.insertSearchKeyWord(item)
    }

    override  fun queryAllKeyWord(): LiveData<List<SearchKeyWord>> {
       return  localSource.queryAllKeyWord()
    }

    override fun searchByKey(keyWord: String, pageIndex: Int)= liveData {
        val result = romtat.searchByKey(keyWord,pageIndex)
        emit(result)
    }

    override suspend fun delAllSearchKeys() {
       localSource.delAllSearchKeys()
    }

    override suspend fun collecteArticle(id: Int): Result<*> {
       return romtat.collecteArticle(id)
    }

    override fun collectList(pageNum: Int): LiveData<Result<*>> {
        return liveData {
            val result = romtat.collectList(pageNum)
            emit(result)
        }
    }

    override suspend fun unCollection(id: Int, originId: Int): Result<*> {
       return  romtat.unCollection(id,originId)
    }

    override suspend fun unCollectionHomeList(id: Int): Result<*> {
       return  romtat.unCollectionHomeList(id)
    }

}