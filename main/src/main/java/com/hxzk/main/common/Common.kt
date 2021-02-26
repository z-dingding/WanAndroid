package com.hxzk.main.common

import android.content.Context
import android.os.Handler
import android.os.Looper


/**
 *作者：created by zjt on 2020/12/10
 *描述:
 *
 */
object Common  {

    private lateinit var mContext: Context

    private lateinit var mHandler : Handler

    const val isDebug = true

     fun initialize(context : Context){
        this.mContext = context
        mHandler = Handler(Looper.getMainLooper())
    }

    /**
     * 获取应用程序的上下文
     */
    fun getContext():Context{
        return mContext
    }


    /**
     * 获取主线程的Handler对象，应用于Toast
     */
    fun getMainHandler():Handler{
        return mHandler
    }


}