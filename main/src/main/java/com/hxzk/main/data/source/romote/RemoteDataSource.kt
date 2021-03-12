package com.hxzk.main.data.source.romote

import com.hxzk.main.data.source.DataSource
import com.hxzk.network.WanApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *作者：created by zjt on 2021/3/11
 *描述:网络请求的数据源
 *
 */
class RemoteDataSource : DataSource {
    private  val  ioDispatch: CoroutineDispatcher = Dispatchers.IO

    override suspend fun login(account: String, pwd: String) =
        withContext(ioDispatch){
        return@withContext  WanApi.get().login(account,pwd)
    }

}

