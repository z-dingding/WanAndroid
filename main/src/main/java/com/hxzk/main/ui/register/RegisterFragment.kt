package com.hxzk.main.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.databinding.RegisterFragBinding
import com.hxzk.main.event.MessageEvent
import com.hxzk.main.event.RegisterSuccessEvent
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.login.LoginActivity
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.succeeded
import kotlinx.android.synthetic.main.fragment_rigister.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class RegisterFragment : BaseFragment() {

    lateinit var registerViewBind : RegisterFragBinding

    val registerViewModel by viewModels<RegisterViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
       val view = inflater.inflate(R.layout.fragment_rigister, container, false)
        registerViewBind = RegisterFragBinding.bind(view).apply {
            registerFrag = registerViewModel
        }
        return registerViewBind.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        accaountLogin.setOnClickListener {
            (activity as LoginActivity).switchFrag(0)
        }


        registerViewModel.isStartLoading.observe(viewLifecycleOwner,{
           if(it)  ProgressDialogUtil.getInstance().showDialog(context) else   ProgressDialogUtil.getInstance().dismissDialog()
        })

        registerViewModel.register?.observe(viewLifecycleOwner,{
            registerViewModel._isStartLoading.value = false
            if(it.succeeded){
                val bean = (it as Result.Success<*>).res as ApiResponse<*>
            if (bean.errorCode == 0) {
                (activity as LoginActivity).switchFrag(0)
                //将注册成功的账号密码发送给登录页面
                val registerEvent = RegisterSuccessEvent(registerViewModel.account.value.toString(),registerViewModel.pwd.value.toString())
                EventBus.getDefault().post(registerEvent)
            } else {
                bean.errorMsg.sToast()
            }
            }else{
                val res = it as Result.Error
                ResponseHandler.handleFailure(res.e)
            }
        })
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
     fun onMessageEvent(messageEvent: MessageEvent) {}


}