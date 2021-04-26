package com.hxzk.network.interceptor

import android.content.Context
import android.text.TextUtils
import com.hxzk.network.NetWork
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 *作者：created by zjt on 2021/4/16
 *描述:Cookie拦截器
 *
 */
class CookieInterceptor : Interceptor {


    val sp = NetWork.context!!.getSharedPreferences("share_prefer", Context.MODE_PRIVATE)
    /**
     * 内存中存储的cookies,先看内存中的再看磁盘中的
     */
    var memoryCookie = ""

    override fun intercept(chain: Interceptor.Chain): Response {
        //获取请求链接
        val originalRequest: Request = chain.request()
        if (memoryCookie.isBlank()) {
            //获取磁盘里面的Cookie字符串
            val spCookie = sp.getString(NetWork.KEY_COOKIES, "")
            if (!TextUtils.isEmpty(spCookie)) {
                //获取spCookie放到内存中
                memoryCookie= spCookie.toString()
            }
        }

        //拦截网络请求数据
        val request: Request = originalRequest.newBuilder()
            //设置请求头Cookie值
            .addHeader("Cookie", memoryCookie)
            .build()

        //拦截返回数据
        val originalResponse: Response = chain.proceed(request)
        //判断请求头里面是否有Set-Cookie值,更新Cookie(通常只有登录时才有)
        if (originalResponse.headers("Set-Cookie").isNotEmpty()) {
            //字符串集
            val stringBuilder: StringBuilder = StringBuilder()
            //将集合转化为字符串
            for (header in originalResponse.headers("Set-Cookie")) {
                stringBuilder.append(header)
                stringBuilder.append(";")
            }
            //拼接Cookie成字符串
            val cookie = stringBuilder.toString()

            //更新内存中Cookies值
            memoryCookie =  cookie
            //存储到本地磁盘中
            sp.edit().apply {
                this.putString(NetWork.KEY_COOKIES, cookie)
                this.apply()
            }
        }
        return originalResponse
    }

}