package com.hxzk.network


/**
 *作者：created by zjt on 2021/3/23
 *描述:封装处理请求结果
 *密封类都是抽象类
 */
 sealed class Result< out R> {

    data class Success<out T>(val res : T) : Result<T>()

    data class Error(val e : Exception):Result<Nothing>()

    object Loading : Result<Nothing>()


    //使用密封类的关键好处在于使用 when 表达式 的时候
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$res]"
            is Error -> "Error[exception=$e]"
            Loading -> "Loading"
        }
    }
}

val Result<*>.succeeded
    get() = this is Result.Success && res != null