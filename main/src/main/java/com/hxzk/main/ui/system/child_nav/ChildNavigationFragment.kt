package com.hxzk.main.ui.system.child_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hxzk.main.R
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment

class ChildNavigationFragment : BaseFragment() {


    private  val viewModel by viewModels<ChildNavigationViewModel> { getViewModelFactory() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_child_navigation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

}