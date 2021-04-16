package com.hxzk.main.ui.mine


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hxzk.main.databinding.FragmentMineBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment

class MineFragment : BaseFragment() {

   val viewModel by viewModels<MineViewModel> { getViewModelFactory() }
   lateinit var databindding : FragmentMineBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databindding =FragmentMineBinding.inflate(inflater,container,false).apply {
            this.viewmodel = viewModel
        }
        return databindding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        databindding.lifecycleOwner = this.viewLifecycleOwner
    }





}