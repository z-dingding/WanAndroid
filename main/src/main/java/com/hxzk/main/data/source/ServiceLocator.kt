package com.hxzk.main.data.source

import android.content.Context
import android.provider.Contacts.SettingsColumns.KEY
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
        ).fallbackToDestructiveMigration()
        .build()
        database = result
        return result
    }


    //.addMigrations(MIGRATION_1_2)
    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE 'hotwords' ('id' INTEGER NOT NULL, 'link' TEXT, 'name' TEXT ,'order' INTEGER , 'visible' INTEGER ,PRIMARY KEY('id'))")
        }
    }
}