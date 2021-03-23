package com.hxzk.main.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.databinding.LoginFragBinding
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.util.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*


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
        viewDataBinding = LoginFragBinding.inflate(inflater, container, false).apply {
            loginmodel = logViewModel
        }
//        val view = inflater.inflate(R.layout.fragment_login, container, false)
//        viewDataBinding = LoginFragBinding.bind(view).apply {
//            loginmodel = logViewModel
//        }
//        viewDataBinding =  DataBindingUtil.inflate(
//            inflater,
//            R.layout.fragment_login,
//            container,
//            false
//        )
//        viewDataBinding.loginmodel = logViewModel


        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        loginButton.setOnClickListener {
            ProgressDialogUtil.getInstance().showDialog(context)
            logViewModel.login()
            logViewModel.response.observe(viewLifecycleOwner, Observer {
                ProgressDialogUtil.getInstance().dismissDialog()
                if (it.errorCode == 200) {

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


}