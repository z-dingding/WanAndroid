package com.hxzk.main.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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


    var register : LiveData<ApiResponse<LoginModel>>? = null


    fun registerEvent() {
        if (account.value.isNullOrBlank() || pwd.value.isNullOrBlank() || surePwd.value.isNullOrBlank()) {
            GlobalUtil.getString(R.string.common_tips_notnull).sMainToast()
            return
        }

        if(!pwd.value.equals(surePwd.value)){
            GlobalUtil.getString(R.string.rigiter_pwdNotCommon).sMainToast()
            return
        }
        register = repository.registerRequest(account.value!!, pwd.value!!, surePwd.value!!)
    }









}

