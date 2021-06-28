package com.hxzk.main.data.source.local

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
     * 插入数据
     */
    //OnConflictStrategy.REPLACE表如已有数据，就覆盖掉。数据的判断通过主键进行匹配，也就是id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(model: CommonItemModel)

    /**
     * 查询整个表
     */
    @Query("Select * From items")
    suspend fun  queryItems() : List<CommonItemModel>

    /**
     * 删除单个数据
     */
    @Delete
    suspend fun deleteItem(model: CommonItemModel) : Int


    /**
     * 删除表中所有数据
     */
    @Delete
    suspend fun  deleteItems( lists: List<CommonItemModel>) : Int
}