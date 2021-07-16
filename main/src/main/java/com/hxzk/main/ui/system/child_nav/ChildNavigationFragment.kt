package com.hxzk.main.ui.system.child_nav

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.callback.NavFlexItemClickListener
import com.hxzk.main.databinding.FragmentChildNavigationBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.NavigationItemAdapter
import com.hxzk.main.ui.adapter.SystemItemAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.network.model.Article
import com.hxzk.network.model.Children
import com.hxzk.network.model.CommonItemModel

class ChildNavigationFragment : BaseFragment() ,NavFlexItemClickListener {

    private  val navViewModel by viewModels<ChildNavigationViewModel> { getViewModelFactory() }
    lateinit var binding : FragmentChildNavigationBinding
    private lateinit var listAdapter: NavigationItemAdapter
    lateinit var activity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentChildNavigationBinding.inflate(inflater, container, false).apply {
            viewModel  = navViewModel
        }
        return super.onCreateView( binding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        binding.lifecycleOwner = viewLifecycleOwner
        setupListAdapter()
        //实现Item点击接口回调
        listAdapter.setNavFlexItemClickListener(this)
        navViewModel.dataLoading.observe(viewLifecycleOwner){
            if (it) ProgressDialogUtil.getInstance().showDialog(activity)    else ProgressDialogUtil.getInstance().dismissDialog()
        }
        navViewModel.requestNavData()
    }

    /**
     * 设置RecyclerView的Adapter
     */
    private fun setupListAdapter() {
        if (navViewModel != null) {
            listAdapter = NavigationItemAdapter(navViewModel)
            binding.recycler.adapter = listAdapter
        }
    }

    override fun onItemClick(item: Article) {
        val bean= CommonItemModel(item.id,item.link,item.title,item.collect)
        val mBundle =Bundle()
        mBundle.putParcelable(X5MainActivity.KEY_ITEMBEAN,bean)
        activity.actionBundle<X5MainActivity>(activity,mBundle)
    }



}