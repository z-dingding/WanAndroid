package com.hxzk.main.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.util.AndroidVersion
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.callback.BannerItemListener
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.FragmentHomeBinding
import com.hxzk.main.event.TransparentStatusBarEvent
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.search.SearchActivity
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.HomeBanner
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity.Companion.KEY_ITEMBEAN
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus


/**
 * 首页Fragment
 */
class HomeFragment : BaseFragment(), BannerItemListener {

    val homeViewModel by viewModels<HomeViewModel> { getViewModelFactory() }
    lateinit var  homeFragDataBinding : FragmentHomeBinding

    private lateinit var listAdapter: HomeItemAdapter

    lateinit var activity: MainActivity
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeFragDataBinding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            this.viewModel = homeViewModel
        }
        setHasOptionsMenu(true)
        return super.onCreateView(homeFragDataBinding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        //设置了lifecycleOwner后绑定了LiveData数据源的xml控件才会随着数据变化而改变
        homeFragDataBinding.lifecycleOwner = this.viewLifecycleOwner
        initToolbar()
        setupListAdapter()
        onItemClick()
        smartListener()

        //刷新数据(如果断点,会影响)
        homeViewModel.banners.observe(viewLifecycleOwner) {}
        homeViewModel.itemList.observe(viewLifecycleOwner) {}
        homeViewModel.forceUpdate(true,true)
    }
    private fun smartListener() {
        //刷新
        homeFragDataBinding.smartRefresh.setOnRefreshListener {
            homeViewModel.forceUpdate(true)
        }
        //加载更多
        homeFragDataBinding.smartRefresh.setOnLoadMoreListener {
            homeViewModel.forceUpdate(false)
        }

        homeViewModel.isRefreshing.observe(viewLifecycleOwner) {
            if (!it){
                homeFragDataBinding.smartRefresh.finishRefresh()
            }
        }
        homeViewModel.isLoadMoreing.observe(viewLifecycleOwner){
            if (!it){
                homeFragDataBinding.smartRefresh.finishLoadMore()
            }
        }
        homeViewModel.dataLoading.observe(viewLifecycleOwner){
            if (it) ProgressDialogUtil.getInstance().showDialog(activity)    else ProgressDialogUtil.getInstance().dismissDialog()
        }
    }


    override fun onResume() {
        super.onResume()
        val message = TransparentStatusBarEvent(false)
        EventBus.getDefault().post(message)
    }
    /**
     * 列表项的点击
     */
  private fun onItemClick(){
       homeViewModel.openItem.observe(viewLifecycleOwner) {
           val mBundle =Bundle()
           mBundle.putParcelable(KEY_ITEMBEAN,it)
           activity.actionBundle<X5MainActivity>(activity,mBundle)
       }
    }

    /**
     * 设置RecyclerView的Adapter
     */
    private fun setupListAdapter() {
        val viewModel = homeFragDataBinding.viewModel
        if (viewModel != null) {
            listAdapter = HomeItemAdapter(viewModel)
            listAdapter.setBannerItemListener(this)
            homeFragDataBinding.recycler.adapter = listAdapter
        }
    }


    /**
     * 设置toolbar
     */
    private fun initToolbar() {
        activity.setSupportActionBar(toolbar)
        val actionBar = activity.supportActionBar
        //隐藏左侧图标
        actionBar?.setDisplayHomeAsUpEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                // Android 5.0版本启用transition动画会存在一些效果上的异常，因此这里只在Android 5.1以上启用此动画
                if (AndroidVersion.hasLollipopMR1()) {
                    val searchMenuView: View? = toolbar?.findViewById(R.id.menu_search)
                    val options = ActivityOptions.makeSceneTransitionAnimation(
                        activity, searchMenuView,
                        getString(R.string.transition_search_back)
                    ).toBundle()
                    startActivity(
                        Intent(activity, SearchActivity::class.java),
                         options
                    )
                } else {
                    startActivity(
                        Intent(activity, SearchActivity::class.java)
                    )
                }
            }
        }
        return true
    }

    override fun onItemClick(data: HomeBanner, position: Int) {
        val model = CommonItemModel(data.id,data.url,data.url)
        val mBundle =Bundle()
        mBundle.putParcelable(KEY_ITEMBEAN,model)
        activity.actionBundle<X5MainActivity>(activity,mBundle)
    }
}