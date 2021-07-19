package com.hxzk.main.ui.browsehistroy

import androidx.lifecycle.*
import com.hxzk.network.Result
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.succeeded
import kotlinx.coroutines.launch

/**
 * @author: hxzk_zjt
 * @date: 2021/6/28
 * 描述:浏览历史
 */
class BrowseHistoryViewModel(private val repository: Repository) : ViewModel() {

    /**
     * 查询数据库浏览记录列表
     */
    private val _items: LiveData<List<CommonItemModel>> = repository.queryBrowseItems().switchMap {
        transitionBannerItem(it)
    }
    val items = _items

    private fun transitionBannerItem(it: Result<List<CommonItemModel>>): LiveData<List<CommonItemModel>> {
        val result = MutableLiveData<List<CommonItemModel>>()
        if (it.succeeded) {
            result.value = (it as Result.Success<List<CommonItemModel>>).res
        } else {
            result.value = null
            val res = it as Result.Error
            ResponseHandler.handleFailure(res.e)
        }
        return result
    }


    /**
     * 清空所有浏览记录的方法
     */
    fun clearAllHistory() {
        viewModelScope.launch {
            repository.delALLBrowsingHistory()
        }
    }

    private val _itemClick = MutableLiveData<CommonItemModel>()
    val itemClick: LiveData<CommonItemModel> = _itemClick
    /**
     * item点击事件
     */
    fun clickItem(item: CommonItemModel) {
        _itemClick.value = item
    }
}