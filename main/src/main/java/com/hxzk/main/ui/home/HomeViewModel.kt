package com.hxzk.main.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.TopArticleModel
import com.hxzk.network.succeeded

/**
 *作者：created by zjt on 2021/4/2
 *描述:
 *
 */
class HomeViewModel(private val repository: Repository) : ViewModel() {

    /**
     * 首页banner数据源(可能只能观察一次)
     */
    private val _banners : LiveData<Result<*>> = repository.banner()
    val banners : LiveData<Result<*>> =_banners
    /**
     * 首页置顶文章数据源
     */
    private val _forceUpdate = MutableLiveData<Any?>()
    private val _topArticle : LiveData<List<TopArticleModel>> =_forceUpdate.switchMap{
        repository.topArticle().switchMap {
            transitionItem(it)
        }
    }
    private fun transitionItem(res : Result<*>) : LiveData<List<TopArticleModel>> {
        val result = MutableLiveData<List<TopArticleModel>>()
        if (res.succeeded) {
            val bean = (res as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value  = bean.data as List<TopArticleModel>
            } else {
                bean.errorMsg.sToast()
            }
        } else {
            result.value = emptyList()
            val res = res as Result.Error
            ResponseHandler.handleFailure(res.e)
        }
       return result
    }

    val topArticle : LiveData<List<TopArticleModel>> =_topArticle



    fun loadTopArticle(){
        _forceUpdate.value =  _forceUpdate.value
    }
}