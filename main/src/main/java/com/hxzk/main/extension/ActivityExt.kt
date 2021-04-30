package com.hxzk.main.extension

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hxzk.main.common.MainApplication
import com.hxzk.main.common.ViewModelFactory
import java.security.acl.Owner

fun AppCompatActivity.getViewModelFactory() : ViewModelFactory {
    val repository = (applicationContext as MainApplication).repository
    //SavedStateRegistryOwner为AppCompatActivity所实现
    return  ViewModelFactory(repository,this)
}
