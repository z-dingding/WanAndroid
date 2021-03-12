package com.hxzk.main.data.source

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *作者：created by zjt on 2021/3/11
 *描述:统一调用入口
 *
 */
class DefaultRepository(private  val  localSource : DataSource,
                        private  val  romtat : DataSource,
                        private  val  ioDispatch: CoroutineDispatcher = Dispatchers.IO
) : Repository{

    override suspend fun login(account: String, pwd: String) = withContext(ioDispatch){
      return@withContext romtat.login(account,pwd)
    }

}