package com.hxzk.main.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hxzk.base.extension.actionFinish
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.Preference
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.LoginFragBinding
import com.hxzk.main.event.MessageEvent
import com.hxzk.main.event.RegisterSuccessEvent
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class LoginFragment : BaseFragment() {
    /**
     * 初始化对应的ViewModel
     */
    private val logViewModel by viewModels<LoginViewModel> { getViewModelFactory() }

    private lateinit var viewDataBinding: LoginFragBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        viewDataBinding = LoginFragBinding.inflate(inflater, container, false).apply {
            loginmodel = logViewModel
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loginButton.setOnClickListener {
            ProgressDialogUtil.getInstance().showDialog(context)
            logViewModel.login()
            logViewModel.response.observe(viewLifecycleOwner, Observer {
                ProgressDialogUtil.getInstance().dismissDialog()
                if (it.errorCode == 0) {
                    //不管是首次注册登录还是直接登录都保存一次
                    (activity as LoginActivity).saveAuthData(logViewModel.accountText.value.toString(),logViewModel.pwdText.value.toString())
                    activity?.actionFinish<MainActivity>(context!!)
                } else {
                    it.errorMsg.sToast()
                }
            })
        }

        registerAccaount.setOnClickListener {
            (activity as LoginActivity).switchFrag(1)
        }

        forgetPwd.setOnClickListener {
            getString(R.string.toast_isDeveloping).sToast()
        }
    }


    override fun onResume() {
        super.onResume()
        var account  by Preference<String>(Const.Auth.KEY_ACCOUNT,"default")
        var pwd by Preference<String>(Const.Auth.KEY_PWD,"default")
        //如果本地有存储的账号密码信息则赋值
        if (!account.equals("default")) {
            accountEdit.setText(account)
            pwdEdit.setText(pwd)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(messageEvent: MessageEvent) {
        //接受注册页面注册成功的账号信息
        if (messageEvent is RegisterSuccessEvent) {
            accountEdit.setText(messageEvent.account)
            pwdEdit.setText(messageEvent.pwd)
        }
    }


}