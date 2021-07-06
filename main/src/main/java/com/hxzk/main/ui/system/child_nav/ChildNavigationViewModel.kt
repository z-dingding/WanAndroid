package com.hxzk.main.ui.system.child_nav

import androidx.lifecycle.*
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.NavigationModel
import com.hxzk.network.model.SystemModel
import com.hxzk.network.succeeded

class ChildNavigationViewModel(private val repository: Repository) : ViewModel() {

    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading


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
        _dataLoading.value = false
        return result
    }


    lateinit var navItems  : LiveData<List<NavigationModel>>

   fun requestNavData(){
       _dataLoading.value = true
       navItems = repository.navigaiontList().switchMap {
           transitionBannerItem(it)
       }
   }
}