package com.hxzk.network.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 *作者：created by zjt on 2021/4/1
 *描述:应用拦截器（执行一次）
 *
 */
internal class HeaderInterceptor : Interceptor{

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        if (NetCacheInterceptor.cookies.isNotEmpty()) {
            builder.addHeader("Cookie", encodeCookie(NetCacheInterceptor.cookies)!!)
        }
        //builder.addHeader("Referer", url)
        builder.addHeader("Connection", "close")
        builder.addHeader("Accept-Language", "zh-CN")
        return chain.proceed(builder.build())
    }

    fun encodeCookie(cookies: List<String>): String? {
        val sb = StringBuilder()
        val set: MutableSet<String> = HashSet()
        for (cookie in cookies) {
            val arr = cookie.split(";".toRegex()).toTypedArray()
            for (s in arr) {
                if (s.contains("SESSION") || s.contains("ck_company_code") || s.contains("ck_username")) {
                    set.add(s)
                }
            }
        }
        for (cookie in set) {
            sb.append(cookie).append(";")
        }
        val last = sb.lastIndexOf(";")
        if (sb.length - 1 == last) {
            sb.deleteCharAt(last)
        }
        Log.e("cookies", sb.toString())
        return sb.toString()
    }
}