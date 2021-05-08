package com.hxzk.main.ui.answer

import androidx.lifecycle.*
import com.hxzk.base.extension.logDebug
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.*
import com.hxzk.network.succeeded
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *作者：created by zjt on 2021/4/2
 *描述:
 *
 */
class AnswerViewModel(val repository: Repository) : ViewModel() {
    /**
     * 页码
     */
    var pageIndex = 1

    /**
     * 总页码(如果是最后一页返回的datas长度为0,所以不用管是否超出下标索引)
     */
    var total = 1

    /**
     * 记录当前列表的数据源(可用来累加存储数据)
     */
    var datas = mutableListOf<DataX>()

    /**
     *触发网络请求的LiveData，true是刷新请求，否则是加载更多的请求
     */
    val isRefreshRequest =MutableLiveData<Boolean>()

    val itemList  = isRefreshRequest.switchMap {
        if(it){
            //刷新
            pageIndex = 1
            datas.clear()
        }else{
           //加载更多
            pageIndex++
        }
        repository.answerList(pageIndex).switchMap {
            transition(it)
        }
    }
    /**
     * 请求数据
     */
    fun requestData(isRefresh : Boolean) {
        isRefreshRequest.value = isRefresh
    }

    private fun transition(it: Result<*>): LiveData<MutableList<DataX>> {
        val result = MutableLiveData<MutableList<DataX>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                val model = bean.data as AnswerModel
                total = model.total
                datas.addAll(model.datas as MutableList<DataX>)
                if(isRefreshRequest.value == true){
                    //刷新
                    result.value = model.datas as MutableList<DataX>
                }else{
                    //加载更多
                    result.value = datas
                }

            } else {
                bean.errorMsg.sToast()
            }
        } else {
            result.value = null
            val res = it as Result.Error
            ResponseHandler.handleFailure(res.e)
        }
        return result
    }

    /**
     * 点击item的监听
     */
    val _itemClick = MutableLiveData<CommonItemModel>()
    val itemClick: LiveData<CommonItemModel> = _itemClick

    /**
     * 点击Item
     */
    fun clickItem(item: DataX) {
        //将不同的数据bean,转化为公用的
        val model = CommonItemModel(item.id, item.link, item.title)
        _itemClick.value = model
    }


}