package com.hxzk.main.data.source

import androidx.lifecycle.LiveData
import com.hxzk.network.ApiResponse
import com.hxzk.network.model.LoginModel
import retrofit2.Call

/**
 *作者：created by zjt on 2021/3/11
 *描述:数据源接口(本地和网络公用)
 *
 */
interface DataSource {
    /**
     * 登录接口
     */
    suspend fun   login(account: String , pwd : String ): ApiResponse<LoginModel>
}