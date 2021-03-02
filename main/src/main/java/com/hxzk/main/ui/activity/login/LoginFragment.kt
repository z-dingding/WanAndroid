package com.hxzk.main.ui.activity.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hxzk.main.R
import com.hxzk.main.ui.activity.base.BaseFragment
import com.hxzk.main.ui.activity.main.MainActivity
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

          loginButton.setOnClickListener {
           MainActivity.actionStart(activity as LoginActivity)
           activity?.finish()
        }

        registerAccaount.setOnClickListener {
            (activity as LoginActivity).fakeDragBy(-1f)
        }

    }



}