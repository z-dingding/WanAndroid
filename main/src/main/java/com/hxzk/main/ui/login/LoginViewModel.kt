package com.hxzk.main.ui.login

import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.hxzk.base.extension.sMainToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.data.source.Repository
import com.hxzk.network.Result

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
     * 暴露不可变的livedata给外部
     */
    private val loginParams =  MutableLiveData<LoginBean>()
    val response : LiveData<Result<*>>  = Transformations.switchMap(loginParams){
      repository.login(it.account,it.pwd)
    }




    /**
     * 执行登录的方法
     */
    fun login() {
        if (!TextUtils.isEmpty(accountText.value) && !TextUtils.isEmpty(pwdText.value)) {
            val parame = LoginBean(accountText.value.toString(),pwdText.value.toString())
            loginParams.value =parame
        }else{
            GlobalUtil.getString(R.string.common_tips_notnull).sMainToast()
            ProgressDialogUtil.getInstance().dismissDialog()
        }
    }

    data class LoginBean(var account : String ,var pwd : String)

}