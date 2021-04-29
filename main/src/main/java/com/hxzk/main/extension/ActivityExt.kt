package com.hxzk.main.extension

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.hxzk.main.common.MainApplication
import com.hxzk.main.common.ViewModelFactory
import java.security.acl.Owner

fun AppCompatActivity.getViewModelFactory() : ViewModelFactory {//SavedStateRegistryOwner
    val repository = (applicationContext as MainApplication).repository
    return  ViewModelFactory(repository,this)
}
