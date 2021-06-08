package com.hxzk.main.ui.system.child_sys

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.actionBundle
import com.hxzk.main.callback.FlexItemClickListener
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.FragmentChildSystemBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.SystemItemAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.system.sysitem.SystemItemActivity
import com.hxzk.network.model.Children

class ChildSystemFragment : BaseFragment(),FlexItemClickListener {

    private  val sysViewModel by viewModels<ChildSystemViewModel> {getViewModelFactory()  }
    private lateinit var binding : FragmentChildSystemBinding
    private lateinit var listAdapter: SystemItemAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding=FragmentChildSystemBinding.inflate(inflater, container, false).apply {
            viewmodel  = sysViewModel
        }
        return super.onCreateView(binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        setupListAdapter()
        listAdapter.setFlexItemClickListener(this)
    }


    /**
     * 设置RecyclerView的Adapter
     */
    private fun setupListAdapter() {
        if (sysViewModel != null) {
            listAdapter = SystemItemAdapter(sysViewModel)
            binding.recycler.adapter = listAdapter
        }
    }

    override fun onItemClick(item: Children) {
        val bundle =Bundle().apply {
            putString(Const.SystemItem.KEY_TITLE, item.name)
            putInt(Const.SystemItem.KEY_ID, item.id)
        }
        (activity as MainActivity).actionBundle<SystemItemActivity>(activity as MainActivity,bundle)
    }

}