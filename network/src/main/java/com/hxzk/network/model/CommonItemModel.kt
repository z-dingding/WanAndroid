package com.hxzk.network.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 *作者：created by zjt on 2021/4/14
 *描述:通用的item
 *
 */

@Parcelize
data class CommonItemModel (val id : Int , val link : String ,val title : String) : Parcelable