package com.hxzk.main.data.source

import androidx.lifecycle.liveData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 *作者：created by zjt on 2021/3/11
 *描述:统一调用入口
 *
 */
class DefaultRepository(private  val  localSource : DataSource,
                        private  val  romtat : DataSource,
                        private  val  ioDispatch: CoroutineDispatcher = Dispatchers.IO
) : Repository{

    override  fun login(account: String, pwd: String) = liveData(ioDispatch){
        val result = romtat.login(account,pwd)
        emit(result)
    }

}