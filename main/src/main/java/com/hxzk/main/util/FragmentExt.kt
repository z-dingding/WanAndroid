package com.hxzk.main.util

import androidx.fragment.app.Fragment
import com.hxzk.main.common.MainApplication
import com.hxzk.main.common.ViewModelFactory

/**
 *作者：created by zjt on 2021/3/12
 *描述:Fragment的扩展函数
 *
 */


fun Fragment.getViewModelFactory() : ViewModelFactory {
    val repository = (requireContext().applicationContext as MainApplication).repository
    return  ViewModelFactory(repository,this)
}