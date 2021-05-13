package com.hxzk.main.ui.system.child_nav

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.NavigationModel
import com.hxzk.network.model.SystemModel
import com.hxzk.network.succeeded

class ChildNavigationViewModel(private val repository: Repository) : ViewModel() {

    private val _navItems = repository.navigaiontList().switchMap {
        transitionBannerItem(it)
    }
    private fun transitionBannerItem(it: Result<*>): LiveData<List<NavigationModel>> {
        val result = MutableLiveData<List<NavigationModel>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = bean.data as List<NavigationModel>
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

    val navItems = _navItems
}