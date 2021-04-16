package com.hxzk.main.ui.mine

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.IntegralModel
import com.hxzk.network.succeeded

class MineViewModel(private val repository: Repository) : ViewModel() {


    private  val _userInfo :LiveData<IntegralModel> = repository.integral().switchMap {
         transition(it)
    }
    private fun transition(it : Result<*>) : LiveData<IntegralModel> {
        val result =MutableLiveData<IntegralModel>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = bean.data as IntegralModel
            } else {
                bean.errorMsg.sToast()
            }
        } else {
            val res = it as Result.Error
            ResponseHandler.handleFailure(res.e)
        }
        return result
    }

    val userInfo = _userInfo
}