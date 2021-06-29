package com.hxzk.main.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hxzk.network.model.CommonItemModel

/**
 * @author: hxzk_zjt
 * @date: 2021/6/25
 * 描述:构建一个访问Items表的DAO接口
 */
@Dao
interface ItemDao {
    /**
     * 插入单条数据
     */
    //OnConflictStrategy.REPLACE表如已有数据，就覆盖掉。数据的判断通过主键进行匹配，也就是id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(model: CommonItemModel)

    /**
     * 查询整个表
     */
    @Query("Select * From items")
     fun  queryItems() : LiveData<List<CommonItemModel>>



    /**
     * 删除表中所有数据(注意用的注解是Query)
     * 删除需要在io线程中,报错:Cannot access database on the main thread
     */
    @Query("DELETE FROM items")
     suspend fun  deleteAllBrowsingHistory()
}