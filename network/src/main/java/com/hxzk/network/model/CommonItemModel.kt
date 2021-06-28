package com.hxzk.network.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 *作者：created by zjt on 2021/4/14
 *描述:X5页面传参通用的item
 *
 */

@Parcelize
//创建表
@Entity(tableName = "items")
//JvmOverloads:方法的重载，类似于在java中对多个构造函数、多个方法的重载.
data class  CommonItemModel  @JvmOverloads constructor(
        //@ColumnInfo表示字段，name表示字段名称
        //@PrimaryKey主键,
        @PrimaryKey @ColumnInfo(name = "id")val id : Int,
        @ColumnInfo(name = "link") val link : String,
        @ColumnInfo(name = "title") val title : String,
        ) : Parcelable