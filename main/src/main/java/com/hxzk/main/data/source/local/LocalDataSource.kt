package com.hxzk.main.data.source.local

import androidx.lifecycle.LiveData
import com.hxzk.main.data.source.DataSource
import com.hxzk.network.ApiResponse

import com.hxzk.network.model.LoginModel

/**
 *作者：created by zjt on 2021/3/11
 *描述:本地的数据源
 *
 */
class LocalDataSource : DataSource {

    override suspend fun login(account: String, pwd: String): LiveData<ApiResponse<LoginModel>> {
        TODO("Not yet implemented")
    }

}