package com.hxzk.main.common

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hxzk.base.util.Common
import com.hxzk.main.data.source.Repository
import com.hxzk.main.data.source.ServiceLocator
import com.hxzk.network.NetWork
import com.hxzk.tencentx5.X5Application
import java.util.concurrent.Executors

/**
 *作者：created by zjt on 2020/12/10
 *描述:
 *
 */
class MainApplication : Application(){

    /**
     *创建仓库
     */
    val repository : Repository
    get() = ServiceLocator.provideRepository(this)



    override fun onCreate() {
        super.onCreate()
        Common.initialize(this)
        NetWork.initialize(this)
        //初始化三方服务
        threadInitService()
    }

    private fun threadInitService(){
        val cacheThredPool = Executors.newCachedThreadPool().execute {
            //配置腾讯浏览服务
            X5Application.initQbSdk(this)
        }
    }



    companion object{
        private const val TAG = "MainApplication"
        //双重校验锁式
        val loadingLiveData : LiveData<Boolean> by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            MutableLiveData<Boolean>()
        }

    }
}