package com.hxzk.main.ui.x5Webview

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.succeeded
import kotlinx.android.synthetic.main.fragment_x5.*
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * @author: hxzk_zjt
 * @date: 2021/6/25
 * 描述:
 */
class X5FragViewModel(val repository: Repository) : ViewModel() {


    /**
     * 将浏览历史数据插入本地数据库
     */
    fun insertItem(model : CommonItemModel)=viewModelScope.launch {
        repository.insertItem(model)
    }


    private val _isCollectionSuccess = MutableLiveData<Boolean>()
    val isCollectionSuccess : LiveData<Boolean> = _isCollectionSuccess

    /**
     * 收藏该文章
     */
    fun collecteArticle(id : Int) =viewModelScope.launch {
        transitionItem(repository.collecteArticle(id))
    }


    private fun transitionItem(it: Result<*>){
        if (it.succeeded) {
            val responseBody = ((it as Result.Success<*>).res as ResponseBody).string()
            val obj  =JSONObject(responseBody)
            if (obj.getInt("errorCode") == 0) {
                _isCollectionSuccess.postValue(true)
            } else {
                obj.getString("errorMsg").sToast()
            }
        } else {
            val res = it as Result.Error
            ResponseHandler.handleFailure(res.e)
        }

    }
}