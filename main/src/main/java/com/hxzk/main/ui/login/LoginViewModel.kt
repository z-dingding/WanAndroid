package com.hxzk.main.ui.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hxzk.main.data.source.Repository
import com.hxzk.network.ApiResponse
import com.hxzk.network.model.LoginModel
import kotlinx.coroutines.launch

/**
 *作者：created by zjt on 2021/3/11
 *描述:登录ViewModel
 *
 */
class LoginViewModel(
    private val repository: Repository
) : ViewModel() {
    /**
     * 输入账号
     */
    val accountText = MutableLiveData<String>()

    /**
     * 输入的密码
     */
    val pwdText = MutableLiveData<String>()

    /**
     * 登录返回结果
     */

    var response : LiveData<ApiResponse<LoginModel>>? = null

    /**
     * 执行登录的方法
     */
    fun login() {
        if (!TextUtils.isEmpty(accountText.value) && !TextUtils.isEmpty(pwdText.value)) {
            //启动协程作用域，开启协程
            viewModelScope.launch {
                response = repository.login(accountText.value.toString(), pwdText.value.toString())
            }
        }
    }

}