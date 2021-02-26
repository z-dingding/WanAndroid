package com.hxzk.base.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.hxzk.base.util.ActivityCollector

/**
 *作者：created by zjt on 2020/11/11
 *描述:Activity跳转的相关扩展函数
 */

/**
 * 普通页面跳转
 */
inline fun <reified T>Activity.action(context: Context){
    val intent =Intent(context, T::class.java)
    context.startActivity(intent)
}


/**
 * 普通页面跳转后finish()本页面
 */
inline fun <reified T>Activity.actionFinish(context: Context){
    val intent =Intent(context, T::class.java)
    context.startActivity(intent)
    (context as Activity).finish()
}

/**
 * 普通页面跳转带参
 */
inline fun <reified T> Activity.actionBundle(context: Context, mbundle: Bundle){
    val intent =Intent(context, T::class.java)
    intent.putExtras(mbundle)
    context.startActivity(intent)
}

/**
 * 普通页面跳转带参，finish()本页面
 */
inline fun <reified T> Activity.actionBundleFinish(context: Context, mbundle: Bundle){
    val intent =Intent(context, T::class.java)
    intent.putExtras(mbundle)
    context.startActivity(intent)
    (context as Activity).finish()
}

/**
 * 普通页面跳转不带参，有返回值
 */
inline fun <reified T> Activity.actionForResult(context: Context, requestCode: Int){
    val intent =Intent(context, T::class.java)
    (context as Activity).startActivityForResult(intent, requestCode)
}

/**
 * 普通页面跳转带参，有返回值
 */
inline fun <reified T> Activity.actionForResultBundle(
    context: Context,
    requestCode: Int,
    mBundle: Bundle
){
    val intent =Intent(context, T::class.java)
    intent.putExtras(mBundle)
    (context as Activity).startActivityForResult(intent, requestCode)
}


/**
 * 跳转到制定页面并删除两页面之间的所有页面(包括调用方的activity)
 */
inline fun <reified T> Activity.actionFinishBetween(context: Context, mClass: Class<T>){
    val from = ActivityCollector.getIndexByClass(context.javaClass)
    val to = ActivityCollector.getIndexByClass(mClass)
    for (i in from downTo to) {
        if(i != to) {
            val mActivity = ActivityCollector.get(i)?.get()
            ActivityCollector.remove(ActivityCollector.get(i))
            mActivity?.finish()
        }
    }
    val intent =Intent(context, T::class.java)
    context.startActivity(intent)
    (context as Activity).finish()
}
