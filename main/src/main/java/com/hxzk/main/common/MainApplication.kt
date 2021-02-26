package com.hxzk.main.common

import android.app.Application
import com.hxzk.base.extension.logDebug
import com.tencent.smtt.export.external.TbsCoreSettings
import com.tencent.smtt.sdk.QbSdk
import java.util.concurrent.Executors

/**
 *作者：created by zjt on 2020/12/10
 *描述:
 *
 */
class MainApplication : Application(){

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

    private fun initQbSdk() {
        // 在调用TBS初始化、创建WebView之前进行如下配置
        val map = HashMap<String, Any>()
        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
        map[TbsCoreSettings.TBS_SETTINGS_USE_DEXLOADER_SERVICE] = true
        QbSdk.initTbsSettings(map)
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        val cb: QbSdk.PreInitCallback = object : QbSdk.PreInitCallback {
            override fun onViewInitFinished(arg0: Boolean) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                logDebug("內核初始化完成的回调:$arg0")
            }

            override fun onCoreInitFinished() {}
        }
        //x5内核初始化接口,可异步初始化
        QbSdk.initX5Environment(this, cb)
    }

    companion object{
        private const val TAG = "MainApplication"
    }
}