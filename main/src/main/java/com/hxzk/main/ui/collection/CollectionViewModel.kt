package com.hxzk.main.ui.collection

import android.view.ViewConfiguration
import androidx.lifecycle.*
import com.hxzk.base.extension.sMainToast
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.main.data.source.Repository
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.DataX
import com.hxzk.network.succeeded
import  com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.ArticleListModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.json.JSONObject

/**
 * @author: hxzk_zjt
 * @date: 2021/7/16
 * 描述:
 */
class CollectionViewModel(private val repository: Repository) : ViewModel() {
    /**
     * 页码,从0开始
     */
    private var pageNum = 0

    /**
     * 总页数
     */
    private var pageCount = 0

    /**
     * 数据源
     */
    private  val   itemDatas : ArrayList<DataX> = ArrayList()


    /**
     * 是否正在刷新
     */
    val isRefreshing = MutableLiveData(false)

    /**
     * 是否正在加载更多
     */
    val isLoadMoreing = MutableLiveData(false)



    private val isRefreshLv=MutableLiveData<Boolean>()
    /**
     * 请求数据
     */
    fun requestData(isRefresh : Boolean){
        if (isRefresh) {
            pageNum = 0
            isRefreshLv.value = isRefresh
        } else {
            pageNum++
            if(pageNum < pageCount ){
                isRefreshLv.value = isRefresh
            }else{
                pageNum--
                isLoadMoreing.value = false
                GlobalUtil.getString(R.string.collection_finalPage).sToast()
            }
        }

    }

    private val _colItems : LiveData<ArrayList<DataX>> =isRefreshLv.switchMap {
        //如果是刷新,页码为0,否则页码+1
        if (it) {
            isRefreshing.value = true
        } else {
            isLoadMoreing.value = true
        }
        repository.collectList(pageNum).switchMap {
            transForm(it)
        }
    }

    private fun transForm(it : Result<*>): MutableLiveData<ArrayList<DataX>> {
        val result = MutableLiveData<ArrayList<DataX>>()
        if (it.succeeded) {
            if (it.succeeded) {
                if (isRefreshLv.value!!) {
                    //先清空原数据源
                    itemDatas.clear()
                    //刷新
                    (it as Result.Success<ApiResponse<ArticleListModel>>).res.data?.let {
                        pageCount = it.pageCount
                        itemDatas.addAll(it.datas)
                    }
                    result.value = ((it).res.data?.datas)
                    isRefreshing.value = false
                } else {
                    //加载更多
                    (it as Result.Success<ApiResponse<ArticleListModel>>).res.data?.datas?.let {
                        it1 ->
                        itemDatas.addAll(it1)
                    }
                    result.value = itemDatas
                    isLoadMoreing.value = false
                }
            } else {
                result.value = null
                val res = it as Result.Error
                ResponseHandler.handleFailure(res.e)
            }
        }
        return result
    }

    val colItems : LiveData<ArrayList<DataX>> = _colItems


    private val _itemClick = MutableLiveData<DataX>()
    val itemClick: LiveData<DataX> = _itemClick
    /**
     * item点击事件
     */
    fun clickItem(item: DataX) {
        _itemClick.value = item
    }

    private  val _unCollectionPos =  MutableLiveData<Int>()
    val unCollectionPos :LiveData<Int> = _unCollectionPos

    /**
     * 取消收藏
     */
    fun cancelCollection(item: DataX,pos : Int){
       val originId : Int = if (item.originId != 0)   item.originId else -1
        viewModelScope.launch {
            transform(repository.unCollection(item.id,originId),pos)
        }
    }


    private fun transform(it: Result<*>,pos : Int){
        if (it.succeeded) {
            val responseBody = ((it as Result.Success<ResponseBody>).res).string()
            val obj  = JSONObject(responseBody)
            if (obj.getInt("errorCode") == 0) {
                _unCollectionPos.value = pos
            } else {
                obj.getString("errorMsg").sMainToast()
            }
        } else {
            val res = it as Result.Error
            ResponseHandler.handleFailure(res.e)
        }

    }

}