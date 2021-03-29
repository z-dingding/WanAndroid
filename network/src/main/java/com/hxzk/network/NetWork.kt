package com.hxzk.network

import android.content.Context

/**
 *作者：created by zjt on 2021/3/24
 *描述:
 *
 */
class NetWork {

    companion object {
         var context: Context? = null

        fun initialize(c: Context) {
            context = c
        }
    }
}