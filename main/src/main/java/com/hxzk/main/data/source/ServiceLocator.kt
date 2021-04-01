package com.hxzk.main.data.source

import android.content.Context
import com.hxzk.main.data.source.local.LocalDataSource
import com.hxzk.main.data.source.romote.RemoteDataSource

/**
 *作者：created by zjt on 2021/3/11
 *描述:对外提供仓库的初始化
 *
 */
 object  ServiceLocator {

    var tasksRepository: Repository? = null

    fun provideRepository(context: Context): Repository {
        synchronized(this) {
            return tasksRepository ?: createRepository(context)
        }
    }

    private fun createRepository(context: Context): Repository {
        //获取仓库分为远程仓库和本地仓库
        val newRepo = DefaultRepository(createLocalDataSource(context),RemoteDataSource())
        tasksRepository = newRepo
        return newRepo
    }

    /**
     * 创建本地数据库
     */
    private fun createLocalDataSource(context: Context): DataSource {
        //val database = database ?: createDataBase(context)
        return LocalDataSource()
    }

}