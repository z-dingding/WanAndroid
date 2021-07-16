package com.hxzk.main.ui.search

import androidx.lifecycle.*
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.ui.home.HomeViewModel
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.*
import com.hxzk.network.succeeded
import kotlinx.coroutines.launch

/**
 * @author: hxzk_zjt
 * @date: 2021/5/11
 * 描述:搜索Acticity
 */
class SearchViewModel(val repository: Repository) : ViewModel() {

    private val requstHotwords = MutableLiveData<Boolean>(false)

    var hotKeys = requstHotwords.switchMap {
        repository.hotKeys().switchMap {
            //只要有请求结果就刷新本地数据库内容
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            allNotifiTable(bean.data as List<HotKeyModel>)
            transitionBannerItem(it)
        }
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


    /**
     * 刷新热词表中内容,先清除所有在添加
     */
    fun allNotifiTable(it: List<HotKeyModel>) {
        //先删除在插入
        viewModelScope.launch {
            repository.delAllHotwords()
            it.forEach { item ->
                repository.insertHotword(item)
            }
        }
    }

    /**
     * 查询本地热词数据
     */
    fun queryKeywords(): List<HotKeyModel>? {
        var result: List<HotKeyModel>? = null
        var data = repository.queryAllHotwords()
        if (data != null) {
            result = transition(data)
        }
        return result
    }

    private fun transition(result: Result<List<HotKeyModel>>): List<HotKeyModel> {
        return if (result.succeeded) {
            (result as Result.Success<List<HotKeyModel>>).res
        } else {
            emptyList()
        }
    }

    /**
     * 插如搜索历史记录
     */
    fun insertSearchKey(item : SearchKeyWord){
        viewModelScope.launch {
            repository.insertSearchKeyWord(item)
        }
    }

    /**
     * 查询所有的搜索历史记录
     */
      val searchKeyword:LiveData<List<SearchKeyWord>> = repository.queryAllKeyWord()

    /**
     * 执行搜索的网络请求
     */
    fun searchDataBykey(keyWord : String,pageIndex:Int) =repository.searchByKey(keyWord,pageIndex).switchMap {
            transition(it)
    }
    private fun transition(it: Result<*>): LiveData<List<DataX>> {
        val result = MutableLiveData<List<DataX>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = (bean.data as AnswerModel).datas
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

    /**
     * 清空所有的搜索历史记录
     */
   fun clearAllSearchKeys(){
       viewModelScope.launch {
           repository.delAllSearchKeys()
       }
    }

    /**
     * 点击item的监听
     */
   private val _itemClick = MutableLiveData<CommonItemModel>()
    val itemClick: LiveData<CommonItemModel> = _itemClick
    /**
     * 点击Item
     */
    fun clickItem(item: DataX) {
        //将不同的数据bean,转化为公用的
        val model = CommonItemModel(item.id, item.link, item.title,item.collect)
        _itemClick.value = model
    }
}