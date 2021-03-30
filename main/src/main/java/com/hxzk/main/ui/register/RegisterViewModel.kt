package com.hxzk.main.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hxzk.base.extension.sMainToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.main.data.source.Repository
import com.hxzk.network.ApiResponse
import com.hxzk.network.model.LoginModel

/**
 *作者：created by zjt on 2021/3/24
 *描述:
 *
 */
class RegisterViewModel(private val repository: Repository) : ViewModel() {
    /**
     * 注册的账号
     */
    val account =  MutableLiveData<String>()

    /**
     * 注册的密码
     */
    val pwd =  MutableLiveData<String>()

    /**
     * 确认注册的密码
     */
    val surePwd =  MutableLiveData<String>()

    /**
     * 监听网络请求,启动ui界面加载loading
     */
    val _isStartLoading = MutableLiveData<Boolean>()
    val isStartLoading : LiveData<Boolean> =_isStartLoading



    private val refershLiveData = MutableLiveData<Any?>()
    //switchMap将转化函数中返回的livedata对象转化为可以观察的livedata对象
    val register : LiveData<ApiResponse<LoginModel>> = Transformations.switchMap(refershLiveData){
        //执行网络请求每次返回的livedata都是new的，所以如果直接用var类型的livedata接受会出问题。
        repository.registerRequest(account.value!!, pwd.value!!, surePwd.value!!)
    }

    fun registerEvent() {
        if (account.value.isNullOrBlank() || pwd.value.isNullOrBlank() || surePwd.value.isNullOrBlank()) {
            GlobalUtil.getString(R.string.common_tips_notnull).sMainToast()
            return
        }

        if(!pwd.value.equals(surePwd.value)){
            GlobalUtil.getString(R.string.rigiter_pwdNotCommon).sMainToast()
            return
        }
        //将空数据取出后再重新放入，LiveData内部不会判断和原数据是否相同，只要调用就触发
        refershLiveData.value =refershLiveData.value
        _isStartLoading.value = true
    }









}

