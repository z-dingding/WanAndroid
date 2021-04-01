package com.hxzk.main.data.source

import com.hxzk.network.Result

/**
 *作者：created by zjt on 2021/3/11
 *描述:数据源接口(本地和网络公用)
 *
 */
interface DataSource {
    /**
     * 登录接口
     */
    suspend fun   login(account: String , pwd : String ): Result<*>
    /**
     * 登录接口
     */
    suspend fun register(username: String,password: String,repassword: String) : Result<*>  /**
     * 首页Banner接口
     */
    suspend fun banner() : Result<*>
}