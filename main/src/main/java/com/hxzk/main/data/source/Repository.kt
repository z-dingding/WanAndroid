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
       fun  login(account: String , pwd : String ) : LiveData<ApiResponse<LoginModel>>

    /**
     * 注册接口
     */
    fun registerRequest( username: String,password: String,repassword: String): LiveData<ApiResponse<LoginModel>>
}