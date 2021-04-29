package com.hxzk.network

import com.hxzk.network.interceptor.CookieInterceptor
import com.hxzk.network.interceptor.LoginIntercepte
import com.hxzk.network.model.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


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
                .addInterceptor(CookieInterceptor())
                .addNetworkInterceptor(LoginIntercepte())
            if (isDebug) {
                val loggingInterceptor = HttpLoggingInterceptor()
                loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                clientBuilder.addInterceptor(loggingInterceptor)
            }
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
    fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): Call<ApiResponse<LoginModel>>

    /**
     * 首页Banner
     */
    @GET("banner/json")
    fun banner(): Call<ApiResponse<List<HomeBanner>>>

    /**
     * 首页置顶文章
     */
    @GET("article/top/json")
    fun topArticle(): Call<ApiResponse<List<DataX>>>
    /**
     * 首页普通文章
     */
    @GET("article/list/{pageIndex}/json")
    fun articleList(@Path("pageIndex") pageIndex: Int): Call<ApiResponse<ArticleListModel>>


    /**
     * 我的(信息)积分接口
     */
    @GET("lg/coin/userinfo/json")
    fun integralApi():Call<ApiResponse<UserInfoModel>>
    /**
     * 我的积分接口(索引从1开始)
     */
    @GET("lg/coin/list/{index}/json")
    fun integralListApi(@Path("index") index: Int):Call<ApiResponse<IntegralItemModel>>












}