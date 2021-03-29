package com.hxzk.main.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.sToast
import com.hxzk.main.R
import com.hxzk.main.databinding.RegisterFragBinding
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.login.LoginActivity
import com.hxzk.main.util.getViewModelFactory
import kotlinx.android.synthetic.main.fragment_rigister.*


class RegisterFragment : BaseFragment() {

    lateinit var registerViewBind : RegisterFragBinding

    val registerViewModel by viewModels<RegisterViewModel> { getViewModelFactory() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

        registerViewModel.register?.observe(viewLifecycleOwner,{
            if (it.errorCode == 0) {
                (activity as LoginActivity).switchFrag(0)
            } else {
                it.errorMsg.sToast()
            }
        })
    }




}