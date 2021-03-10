package com.hxzk.base.extension

import android.os.Looper
import android.widget.Toast
import com.hxzk.main.common.Common

/**
 *作者：created by zjt on 2020/11/11
 *描述:Toast的扩展类
 */


private var toast: Toast? = null


/**
 * 主线程的toast
 */
fun String.sToast(duration: Int =Toast.LENGTH_LONG){
    if(Looper.getMainLooper().equals(Looper.myLooper())){
        if(toast == null){
            toast =  Toast.makeText(Common.getContext(),this,duration)
        }else{
            toast?.setText(this)
        }
        toast?.show()
    }
}


/**
 * 工作线程的toast
 */
fun sMainToast(msg : String ,duration: Int =Toast.LENGTH_LONG){
    Common.getMainHandler().post{
        if(toast == null){
            toast =  Toast.makeText(Common.getContext(),msg,duration)
        }else{
            toast?.setText(msg)
        }
        toast?.show()
    }
}