package com.hxzk.main.data.source.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hxzk.network.Result
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.HotKeyModel
import com.hxzk.network.model.SearchKeyWord

/**
 * @author: hxzk_zjt
 * @date: 2021/6/25
 * 描述:构建一个访问Items表的DAO接口
 */
@Dao
interface ItemDao {
    /**
     * 插入浏览记录单条数据
     */
    //OnConflictStrategy.REPLACE表如已有数据，就覆盖掉。数据的判断通过主键进行匹配，也就是id
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(model: CommonItemModel)

    /**
     * 查询浏览记录整个表
     */
    @Query("Select * From items")
    fun queryItems(): LiveData<List<CommonItemModel>>


    /**
     * 删除浏览记录表中所有数据(注意用的注解是Query)
     * 删除需要在io线程中,报错:Cannot access database on the main thread
     */
    @Query("DELETE FROM items")
    suspend fun deleteAllBrowsingHistory()

    /**
     * 插入单个热词条目
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotword(item: HotKeyModel)

    /**
     * 删除热词表中的所有数据
     */
    @Query("DELETE FROM hotwords")
    suspend fun deleteAllHotwords()

    /**
     * 查询热词表所有的条目
     * 实践发现room函数不能同事即使suspend和livedata
     */
    @Query("Select * From hotwords")
     fun queryAllHotwords(): List<HotKeyModel>

    /**
     * 插入搜索关键字
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchKey(item: SearchKeyWord)
    /**
     * 查询历史搜索记录数据
     */
    @Query("Select * From searchkeys")
     fun queryAllKeyWord():LiveData<List<SearchKeyWord>>

    /**
     * 清空本地所有的搜索记录
     */
    @Query("Delete From searchkeys")
    suspend fun delAllSearchKeys()
}