package com.hxzk.network.interceptor

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 *作者：created by zjt on 2021/4/1
 *描述:网络拦截器(执行两次)
 *
 */
internal  object  NetCacheInterceptor : Interceptor {

    var cookies: List<String> = ArrayList()
    var response: Response? = null


    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        //调用chain.proceed(request)。这个方法是所有HTTP工作发生的地方，以满足请求和响应的需求。
        response = chain.proceed(request)
        val onlineCacheTime = 60
        cookies = response!!.headers("Set-Cookie")
        return response!!.newBuilder()
            .header("Cache-Control", "public, max-age=$onlineCacheTime")
            .removeHeader("Pragma")
            .build()
    }
}