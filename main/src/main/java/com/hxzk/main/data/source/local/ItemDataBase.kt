package com.hxzk.main.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.HotKeyModel

/**
 * @author: hxzk_zjt
 * @date: 2021/6/25
 * 描述:构建Room使用的入口RoomDatabase
 */
@Database(entities = [CommonItemModel::class,HotKeyModel::class] , version = 2 , exportSchema = false)
abstract class ItemDataBase : RoomDatabase(){
    //必须提供获取DAO接口的抽象方法,Room将通过这个方法实例化DAO接口
    abstract fun taskDao(): ItemDao
}
