package com.hxzk.main.ui.login

import com.hxzk.base.util.Preference
import com.hxzk.main.common.Const
import com.hxzk.main.ui.base.BaseActivity

abstract class AuthActivity : BaseActivity() {


    /**
     * 存储登录用户的账号密码
     */
    fun saveAuthData(account: String, pwd: String) {
        var sAcount by Preference(Const.Auth.KEY_ACCOUNT,"default")
        sAcount = account
        var sPwd by Preference(Const.Auth.KEY_PWD,"default")
        sPwd = pwd
    }

}