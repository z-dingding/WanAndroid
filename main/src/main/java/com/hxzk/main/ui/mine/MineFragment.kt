package com.hxzk.main.ui.mine


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.action
import com.hxzk.base.extension.logDebug
import com.hxzk.main.R
import com.hxzk.main.databinding.FragmentMineBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.integral.IntegralActivity
import com.hxzk.main.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseFragment() , View.OnClickListener {

    val viewModel by viewModels<MineViewModel> { getViewModelFactory()}
    lateinit var databindding: FragmentMineBinding
    lateinit var activity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        databindding = FragmentMineBinding.inflate(inflater, container, false).apply {
            this.viewmodel = viewModel
        }
        databindding.lifecycleOwner = this.viewLifecycleOwner
        return super.onCreateView(databindding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = requireActivity() as MainActivity
        //改变状态栏颜色
        activity.window.statusBarColor = ContextCompat.getColor(activity, R.color.colorPrimary)

        stv_integral.setOnClickListener(this)
    }

    override fun onClick(v: View?) {

        when(v?.id){
            R.id.stv_integral -> activity.action<IntegralActivity>(activity)
        }
    }

}