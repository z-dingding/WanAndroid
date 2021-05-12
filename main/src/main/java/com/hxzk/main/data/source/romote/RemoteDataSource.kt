package com.hxzk.main.data.source.romote

import com.hxzk.main.data.source.DataSource
import com.hxzk.network.Result
import com.hxzk.network.WanApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
/**
 *作者：created by zjt on 2021/3/11
 *描述:网络请求的数据源
 *
 */
class RemoteDataSource : DataSource {

    override suspend fun login(account: String, pwd: String) = WanApi.get().login(account,pwd).await()
    override suspend fun register(username: String,password: String,repassword: String) = WanApi.get().register(username,password,repassword).await()
    override suspend fun banner() = WanApi.get().banner().await()
    override suspend fun topArticle() =  WanApi.get().topArticle().await()
    override suspend fun articleList(pageIndex : Int) =  WanApi.get().articleList(pageIndex).await()
    override suspend fun integral()=  WanApi.get().integralApi().await()
    override suspend fun integralList(pageIndex : Int)= WanApi.get().integralListApi(pageIndex).await()
    //需要转成Deferred，answerList()则不需要添加suspend关键字
    override suspend fun answerList(pageIndex: Int)=WanApi.get().answerList(pageIndex).await()
    override suspend fun treeList() = WanApi.get().treeList().await()
    override suspend fun hotKeys() =WanApi.get().hotKeys().await()

    /**
     * Call的扩展函数(默认持有该对象的引用)
     */
    private suspend fun <T> Call<T>.await(): Result<*> {
        return suspendCoroutine { continuation ->
            //enqueue异步,execute同步
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(Result.Success(body))
                    else continuation.resume(Result.Error(RuntimeException("response body is null")))
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    //多是由于外因所致
                    continuation.resume(Result.Error(t as Exception))
                }
            })
        }
    }

}


