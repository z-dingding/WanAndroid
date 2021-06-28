package com.hxzk.main.data.source

import android.content.Context
import androidx.room.Room
import com.hxzk.main.data.source.local.ItemDataBase
import com.hxzk.main.data.source.local.LocalDataSource
import com.hxzk.main.data.source.romote.RemoteDataSource

/**
 *作者：created by zjt on 2021/3/11
 *描述:对外提供仓库的初始化
 *
 */
 object  ServiceLocator {

    var tasksRepository: Repository? = null
    var database : ItemDataBase? = null

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
    
    private fun createLocalDataSource(context: Context): DataSource {
        val database = database ?: createDataBase(context)
        return LocalDataSource(database.taskDao())
    }

    private fun createDataBase(context: Context): ItemDataBase {
        val result = Room.databaseBuilder(
                context.applicationContext,
                ItemDataBase::class.java, "item.db"
        ).build()
        database = result
        return result
    }

}