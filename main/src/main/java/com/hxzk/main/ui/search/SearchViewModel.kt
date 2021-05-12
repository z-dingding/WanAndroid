package com.hxzk.main.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.HotKeyModel
import com.hxzk.network.model.SystemModel
import com.hxzk.network.succeeded

/**
 * @author: hxzk_zjt
 * @date: 2021/5/11
 * 描述:搜索Acticity
 */
class SearchViewModel(val repository: Repository) : ViewModel() {

   private  val _hotKeys = repository.hotKeys().switchMap {
       transitionBannerItem(it)
   }
    private fun transitionBannerItem(it: Result<*>): LiveData<List<HotKeyModel>> {
        val result = MutableLiveData<List<HotKeyModel>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = bean.data as List<HotKeyModel>
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
    val hotKeys=_hotKeys
}