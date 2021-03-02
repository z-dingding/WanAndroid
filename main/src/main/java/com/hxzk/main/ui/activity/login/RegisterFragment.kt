package com.hxzk.main.ui.activity.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hxzk.main.R
import com.hxzk.main.ui.activity.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_rigister.*


class RegisterFragment : BaseFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rigister, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        accaountLogin.setOnClickListener {
            (activity as LoginActivity).fakeDragBy(1f)
        }


    }




}