package com.hxzk.main.ui.system.sysitem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.AnswerModel
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.DataX
import com.hxzk.network.succeeded

/**
 * @author: hxzk_zjt
 * @date: 2021/6/8
 * 描述:
 */
class SystemItemViewModel(val repository: Repository) : ViewModel() {

    /**
     * 当前页面展示数据列表的请求id
     */
    val  cIdLiveData = MutableLiveData<Int>()
    /**
     * 记录当前列表的数据源(可用来累加存储数据)
     */
    var datas = ArrayList<DataX>()


    private val _listData = cIdLiveData.switchMap {
        repository.systemItemList(0,it).switchMap {
            transition(it)
        }
    }
    val listData=_listData


    private fun transition(it: Result<*>): LiveData<MutableList<DataX>> {
        val result = MutableLiveData<MutableList<DataX>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                val model = bean.data as AnswerModel
                datas.addAll(model.datas as MutableList<DataX>)
                    result.value = datas
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