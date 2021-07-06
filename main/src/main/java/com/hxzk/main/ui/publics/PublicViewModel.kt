package com.hxzk.main.ui.publics

import androidx.lifecycle.*
import androidx.lifecycle.Transformations.switchMap
import com.hxzk.base.extension.sToast
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.*
import com.hxzk.network.succeeded
import kotlinx.coroutines.launch

class PublicViewModel(private val repository: Repository) : ViewModel() {


    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    private val _leftItems = repository.wxPublic().switchMap {
        transitionLeftItem(it)
    }

    private fun transitionLeftItem(it: Result<*>): LiveData<List<Children>> {
        val result = MutableLiveData<List<Children>>()
        if (it.succeeded) {
            val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                result.value = bean.data as List<Children>
                //将第一个item设为选中状态
                result.value!![0].isSelect = true
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
     * 左边RV数据源
     */
    val leftItems = _leftItems

    /**
     * 左边RV的Item的监听
     */
    val _leftItemClick = MutableLiveData<Children>()
    val leftItemClick = _leftItemClick
    fun leftItemClick(children: Children) {
        _leftItemClick.value = children
    }




    private val parentId =  MutableLiveData<List<Int>>()
    private val _rightItems =parentId.switchMap {
        repository.wxPublicArticle(it).switchMap {
            transitionRightItem(it)
        }
    }

    /**
     * 左边RV数据源获取后手动触发右侧数据请求
     */
    fun rightData(list:List<Int>){
        _dataLoading.value = true
        parentId.value= list
    }

    private fun transitionRightItem(it: List<Any>): LiveData<List<ArticleListModel>> {
        val result = MutableLiveData<List<ArticleListModel>>()
        val list = ArrayList<ArticleListModel>()
        it.forEach {
            val item = it as Result<*>
            if (item.succeeded) {
                val bean = (it as Result.Success<*>).res as ApiResponse<*>
                if (bean.errorCode == 0) {
                    val model  = bean.data as ArticleListModel
                    list.add(model)
                } else {
                    bean.errorMsg.sToast()
                }
            } else {
                result.value = emptyList()
                val res = it as Result.Error
                ResponseHandler.handleFailure(res.e)
            }
        }
        result.value = list
        _dataLoading.value = false
        return result
    }
    /**
     * 右边RV数据源
     */
    val rightItems = _rightItems

    /**
     * 左边RV的Item的监听
     */
    val _rightItemClick = MutableLiveData<DataX>()
    val rightItemClick = _rightItemClick
    fun rightItemClick(dataX: DataX) {
        _rightItemClick.value = dataX
    }
}