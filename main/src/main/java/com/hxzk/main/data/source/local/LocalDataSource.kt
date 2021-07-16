package com.hxzk.main.data.source.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.hxzk.main.data.source.DataSource
import com.hxzk.network.Result
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.HotKeyModel
import com.hxzk.network.model.SearchKeyWord
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

/**
 *作者：created by zjt on 2021/3/11
 *描述:本地的数据源
 *
 */
class LocalDataSource(
        private val  dao : ItemDao,
        private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DataSource {

    override suspend fun login(account: String, pwd: String): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun register(
        username: String,
        password: String,
        repassword: String
    ): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun banner(): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun topArticle(): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun articleList(pageIndex : Int): Result<*> {
        TODO("Not yet implemented")
    }

    suspend override fun integral(): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun integralList(pageIndex : Int): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun answerList(pageIndex: Int): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun treeList(): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun systemItemList(pageIndex : Int,cId:Int): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun navigaiontList(): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun hotKeys(): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun wxPublic(): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun wxPublicArticle(publicId: Int, pageIndex: Int): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun insertItem(model: CommonItemModel) {
        dao.insertItem(model)
    }

    override  fun queryBrowseItems(): LiveData<Result<List<CommonItemModel>>> {
        return dao.queryItems().map {
            Result.Success(it)
        }

    }

    override  suspend fun delALLBrowsingHistory() {
        dao.deleteAllBrowsingHistory()
    }

    override suspend fun delAllHotwords() {
        dao.deleteAllHotwords()
    }

    override suspend fun insertHotword(item: HotKeyModel) {
        dao.insertHotword(item)
    }

    override  fun queryAllHotwords(): Result<List<HotKeyModel>> {
         return  Result.Success(dao.queryAllHotwords())
    }

    override suspend fun insertSearchKeyWord(item: SearchKeyWord) {
        dao.insertSearchKey(item)
    }

    override  fun queryAllKeyWord(): LiveData<List<SearchKeyWord>> {
    return   dao.queryAllKeyWord()
    }

    override suspend fun searchByKey(keyWord: String, pageIndex: Int): Result<*> {
        TODO("Not yet implemented")
    }

    override suspend fun delAllSearchKeys() {
         dao.delAllSearchKeys()

    }

    override suspend fun collecteArticle(id: Int) : Result<ResponseBody> {
        TODO("Not yet implemented")
    }


}