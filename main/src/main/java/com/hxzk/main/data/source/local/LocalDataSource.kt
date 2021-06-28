package com.hxzk.main.data.source.local

import com.hxzk.main.data.source.DataSource
import com.hxzk.network.Result
import com.hxzk.network.model.CommonItemModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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

    override suspend fun queryItems(): Result<List<CommonItemModel>> = withContext(ioDispatcher) {
//        return@withContext try {
//            Result.Success(dao.queryItems())
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
         Result.Success(dao.queryItems())
    }


}