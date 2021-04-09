package com.hxzk.main.ui.home

import androidx.lifecycle.*
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.model.TopArticleModel
import com.hxzk.network.succeeded

/**
 *作者：created by zjt on 2021/4/2
 *描述:
 *
 */
class HomeViewModel(private val repository: Repository) : ViewModel() {

    /**
     * 是否进行刷新的标识(默认不刷新)
     */
    private val _forceUpdate = MutableLiveData(false)

    /**
     * 是否正在刷新
     */
     val isRefreshing =MutableLiveData(false)
    /**
     * 是否正在加载更多
     */
     val isLoadMoreing =MutableLiveData(false)

    /**
     * 首页banner数据源
     */
    private val _banners: LiveData<List<HomeBanner>> =
        repository.banner().distinctUntilChanged().switchMap {
            transitionBannerItem(it)
        }

    private fun transitionBannerItem(it: Result<*>): LiveData<List<HomeBanner>> {
        val result = MutableLiveData<List<HomeBanner>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = bean.data as List<HomeBanner>
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

    val banners: LiveData<List<HomeBanner>> = _banners


    /**
     * 首页置顶文章数据源
     */
    private val _topArticle: LiveData<List<TopArticleModel>> = _forceUpdate.switchMap {
             isRefreshing.value = true
            //distinctUntilChanged()只有数据内容变化，才会执行
            repository.topArticle().distinctUntilChanged().switchMap {
                isRefreshing.value = false
                transitionItem(it)
            }

    }

    private fun transitionItem(res: Result<*>): LiveData<List<TopArticleModel>> {
        val result = MutableLiveData<List<TopArticleModel>>()
        if (res.succeeded) {
            val bean = (res as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = bean.data as List<TopArticleModel>
                _topArticleSize.value = result.value!!.size
            } else {
                bean.errorMsg.sToast()
            }
        } else {
            result.value = emptyList()
            ResponseHandler.handleFailure((res as Result.Error).e)
        }
        return result
    }

    val topArticle: LiveData<List<TopArticleModel>> = _topArticle

    /**
     * 获取item的条目,adapter中的getItemCount要用
     */
    val _topArticleSize = MutableLiveData<Int>()


    fun refresh(refresh: Boolean) {
        _forceUpdate.value = refresh
    }


    val _openItem = MutableLiveData<Int>()
    val openItem = _openItem

    /**
     * 点击Item
     */
    fun clickItem(url: Int) {
        _openItem.value = url
    }
}