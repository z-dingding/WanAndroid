package com.hxzk.network.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * @author: hxzk_zjt
 * @date: 2021/5/12
 * 描述:
 */
@Parcelize
//热门搜索表
@Entity(tableName = "hotwords")
data class HotKeyModel @JvmOverloads constructor(
        @PrimaryKey @ColumnInfo(name = "id") val id: Int,
        @ColumnInfo(name = "link")val link: String,
        @ColumnInfo(name = "name")val name: String,
        @ColumnInfo(name = "order") val order: Int,
        @ColumnInfo(name = "visible") val visible: Int
) : Parcelable


@Parcelize
//搜索历史表
@Entity(tableName = "searchkeys")
data class SearchKeyWord @JvmOverloads constructor(
@PrimaryKey @ColumnInfo(name = "searchkey") val searchKey:String =""
):Parcelable