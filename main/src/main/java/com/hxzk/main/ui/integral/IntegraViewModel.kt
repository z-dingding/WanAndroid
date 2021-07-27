package com.hxzk.main.ui.integral

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.DataItem
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.model.IntegralItemModel
import com.hxzk.network.succeeded

class IntegraViewModel (repository: Repository) : ViewModel() {


    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

     val requstData =MutableLiveData<Boolean>()

    private val _items : LiveData<List<DataItem>> = requstData.switchMap {
        _dataLoading.value = true
        repository.integralList(1).switchMap {
            transitionItem(it)
        }
    }

    private fun transitionItem(it: Result<*>): LiveData<List<DataItem>> {
        val result = MutableLiveData<List<DataItem>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = (bean.data as IntegralItemModel).datas
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

    val items : LiveData<List<DataItem>> = _items
}