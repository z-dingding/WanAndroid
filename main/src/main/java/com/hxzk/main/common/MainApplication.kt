package com.hxzk.main.common

import com.hxzk.tencentx5.X5Application
import java.util.concurrent.Executors

/**
 *作者：created by zjt on 2020/12/10
 *描述:
 *
 */
class MainApplication : X5Application(){

    override fun onCreate() {
        super.onCreate()
        Common.initialize(this)
        //初始化三方服务
        threadInitService()
    }

    private fun threadInitService(){
        val cacheThredPool = Executors.newCachedThreadPool().execute {
            //配置腾讯浏览服务
            initQbSdk()
        }
    }



    companion object{
        private const val TAG = "MainApplication"
    }
}