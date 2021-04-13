package com.hxzk.main.ui.home

import androidx.lifecycle.*
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.ArticleListModel
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.succeeded
import kotlinx.coroutines.launch

/**
 *作者：created by zjt on 2021/4/2
 *描述:
 *
 */
class HomeViewModel(private val repository: Repository) : ViewModel() {

    /**
     * 是否进行刷新的标识
     */
    private val _forceUpdate = MutableLiveData<Any?>()

    /**
     * 是否正在刷新
     */
     val isRefreshing =MutableLiveData(false)
    /**
     * 是否正在加载更多
     */
     val isLoadMoreing =MutableLiveData(false)

    /**
     * 首页banner数据源(用_forceUpdate来控制，主要是因为livedata感知，只会在onResume和onPause)
     */
    private val _banners: LiveData<List<HomeBanner>> = _forceUpdate.switchMap {
        repository.banner().switchMap {
            transitionBannerItem(it)
        }
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
     * 获取item的条目,adapter中的getItemCount要用
     */
    val _topArticleSize = MutableLiveData<Int>()


    fun refresh() {
        _forceUpdate.value = _forceUpdate.value
    }


    val _openItem = MutableLiveData<Int>()
    val openItem = _openItem

    /**
     * 点击Item
     */
    fun clickItem(url: Int) {
        _openItem.value = url
    }

    /**
     * 请求首页文章列表(包括置顶文章和一般文章列表)
     */
     var curPage : Int = 0
    var pageCount : Int =0

    private val _itemList :  LiveData<ArticleListModel> = _forceUpdate.switchMap {
        itemList()
    }
    private fun itemList() :  LiveData<ArticleListModel> {
        isRefreshing.value = true
        val result =  MutableLiveData<ArticleListModel>()
        viewModelScope.launch {
           val data =  repository.articleList(curPage)
            curPage =data.curPage
            pageCount =data.pageCount
            result.value = data
            _topArticleSize.value = data.datas.size
        }
        isRefreshing.value = false
       return  result
    }
    val itemList :  LiveData<ArticleListModel>  = _itemList

}