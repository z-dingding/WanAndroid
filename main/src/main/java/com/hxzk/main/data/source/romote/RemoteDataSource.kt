package com.hxzk.main.data.source.romote

import androidx.lifecycle.LiveData
import com.hxzk.main.data.source.DataSource
import com.hxzk.network.WanApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 *作者：created by zjt on 2021/3/11
 *描述:网络请求的数据源
 *
 */
class RemoteDataSource : DataSource {
    private  val  ioDispatch: CoroutineDispatcher = Dispatchers.IO

    override suspend fun login(account: String, pwd: String) = WanApi.get().login(account,pwd).await()


    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}

