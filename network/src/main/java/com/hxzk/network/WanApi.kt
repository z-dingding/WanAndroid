package com.hxzk.network

import com.hxzk.network.interceptor.HeaderInterceptor
import com.hxzk.network.interceptor.NetCacheInterceptor
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.model.LoginModel
import com.hxzk.network.model.TopArticleModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST


/**
 *作者：created by zjt on 2021/3/12
 *描述:访问网络的Retrofit的Api接口
 */
 interface WanApi {

    companion object {
        /**
         * 是否打印日志
         */
        const val isDebug = true

        /**
         * basrUrl
         */
        const val baseUrl ="https://www.wanandroid.com/"

        fun get(): WanApi {
            val clientBuilder = OkHttpClient.Builder()
                .connectTimeout(60, java.util.concurrent.TimeUnit.SECONDS)
                .addInterceptor(HeaderInterceptor())
                .addNetworkInterceptor(NetCacheInterceptor)
            if (isDebug) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(loggingInterceptor)
            }
//            val cookieJar =
//                PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(NetWork.context))
//            clientBuilder.cookieJar(cookieJar)

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(clientBuilder.build())
                .addCallAdapterFactory(LiveDataCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WanApi::class.java)
        }
    }



    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Call<ApiResponse<LoginModel>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") username: String, @Field("password") password: String , @Field("repassword") repassword: String): Call<ApiResponse<LoginModel>>

    /**
     * 首页Banner
     */
    @GET("banner/json")
    fun banner(): Call<ApiResponse<List<HomeBanner>>>

    /**
     * 首页置顶文章
     */
    @GET("article/top/json")
    fun topArticle(): Call<ApiResponse<List<TopArticleModel>>>











}