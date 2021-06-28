package com.hxzk.main.ui.x5Webview

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxzk.main.data.source.Repository
import com.hxzk.network.model.CommonItemModel
import kotlinx.coroutines.launch

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

}