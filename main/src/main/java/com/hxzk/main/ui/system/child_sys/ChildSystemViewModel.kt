package com.hxzk.main.ui.system.child_sys

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.model.SystemModel
import com.hxzk.network.succeeded

class ChildSystemViewModel(private val repository: Repository) : ViewModel() {



    private val _sysItems = repository.treeList().switchMap {
        transitionBannerItem(it)
    }
    private fun transitionBannerItem(it: Result<*>): LiveData<List<SystemModel>> {
        val result = MutableLiveData<List<SystemModel>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = bean.data as List<SystemModel>
            } else {
                bean.errorMsg.sToast()
            }
        } else {
            result.value = emptyList()
            val res = it as Result.Error
            ResponseHandler.handleFailure(res.e)
        }
        return result
    }

    val sysItems = _sysItems
}