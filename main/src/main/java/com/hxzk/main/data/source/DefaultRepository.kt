package com.hxzk.main.data.source

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.hxzk.network.ApiResponse
import com.hxzk.network.model.LoginModel
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

    override  fun login(account: String, pwd: String)  = liveData<ApiResponse<LoginModel>>(ioDispatch){
        val result = romtat.login(account,pwd)
        // TODO: 2021/3/16  
        emit(result)
    }

}