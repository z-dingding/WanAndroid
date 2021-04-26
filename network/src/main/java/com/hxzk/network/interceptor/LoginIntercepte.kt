package com.hxzk.network.interceptor

import android.content.Context
import android.content.Intent
import com.hxzk.network.NetWork
import okhttp3.Interceptor
import okhttp3.Response
import org.json.JSONObject
import java.nio.charset.StandardCharsets

/**
 *作者：created by zjt on 2021/4/26
 *描述:登录信息失效跳转到登录页面
 *
 */
class LoginIntercepte : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed (request)

        val responseBody = response.body
        if (responseBody != null) {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE)
            // Buffer the entire body.
            val buffer = source.buffer()
            try {
                val result = buffer.clone().readString(StandardCharsets.UTF_8)
                val jsonObject = JSONObject(result)
                val code = jsonObject.getInt ("errorCode")
                //{"errorCode":-1001,"errorMsg":"请先登录！"}
                if (code == -1001) {
                  val preference = NetWork.context?.getSharedPreferences("share_prefer", Context.MODE_PRIVATE)
                  preference?.edit()?.apply {
                        putString("key_account","")
                       putString("key_pwd","")
                       putString(NetWork.KEY_COOKIES,"")
                       apply()
                    }
                    //隐士跳转到OpenSourceLoginAct  ivity
                     val ACTION_LOGIN_WITH_TRANSITION = "${NetWork.context?.getPackageName()}.ACTION_LOGIN_WITH_TRANSITION"
                     val mIntent = Intent(ACTION_LOGIN_WITH_TRANSITION).apply {
                         this.putExtra("key_isloginagain",true)
                         this.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                     }
                     NetWork.context?.startActivity(mIntent)
                }
            } catch (e : Exception) {
                e.printStackTrace()
            }
        }
        return response
    }
}