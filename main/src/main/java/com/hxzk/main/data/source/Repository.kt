package com.hxzk.main.data.source

import androidx.lifecycle.LiveData
import com.hxzk.network.ApiResponse
import com.hxzk.network.model.LoginModel

/**
 *作者：created by zjt on 2021/3/11
 *描述:
 *
 */
interface Repository {

    /**
     * 登录接口
     */
    suspend fun  login(account: String , pwd : String ) : LiveData <ApiResponse<LoginModel>>
}