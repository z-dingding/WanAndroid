package com.hxzk.base.util

import android.content.Context
import android.content.SharedPreferences
import kotlin.reflect.KProperty

/**
者：created by zjt on 2021/3/30
 *描述:SharePreference的委托类
 *
 */
class  Preference<T>(val name: String, private val default: T) {
  //share_prefer
    private val prefs: SharedPreferences by lazy {Common.getContext().getSharedPreferences("share_prefer", Context.MODE_PRIVATE) }

    /**
     * Delegate类必须有getValue和setValue方法
     */
    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getSharePreferences(name, default)
    }

    /**
     * thisRef就是拥有这个属性的对象
     * KProperty类的示例代表被委托的属性
     * value是委托变量变化后的值
     */
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value:
    T) {
        putSharePreferences(name, value)
    }


    private fun putSharePreferences(name: String, value: T) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(name, value)
            is String -> putString(name, value)
            is Int -> putInt(name, value)
            is Boolean -> putBoolean(name, value)
            is Float -> putFloat(name, value)
            else -> throw IllegalArgumentException("This type of data cannot be saved!")
        }.apply()
    }


    private fun getSharePreferences(name: String, default: T): T = with(prefs) {
        val res: Any? = when (default) {
            is Long -> getLong(name, default)
            is String -> getString(name, default)
            is Int -> getInt(name, default)
            is Boolean -> getBoolean(name, default)
            is Float -> getFloat(name, default)
            else -> throw IllegalArgumentException("This type of data cannot be saved!")
        }
        return res as T
    }
}