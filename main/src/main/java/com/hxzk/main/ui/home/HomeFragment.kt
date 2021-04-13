package com.hxzk.main.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.sMainToast
import com.hxzk.base.util.AndroidVersion
import com.hxzk.main.R
import com.hxzk.main.callback.BannerItemListener
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.FragmentHomeBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.search.SearchActivity
import com.hxzk.network.model.HomeBanner
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * 首页Fragment
 */
class HomeFragment : BaseFragment(), BannerItemListener {

    val homeViewModel by viewModels<HomeViewModel> { getViewModelFactory() }
    lateinit var  homeFragDataBinding : FragmentHomeBinding
    lateinit var activity: MainActivity
    private lateinit var listAdapter: HomeItemAdapter


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
        //设置了lifecycleOwner后绑定了LiveData数据源的xml控件才会随着数据变化而改变
        homeFragDataBinding.lifecycleOwner = this.viewLifecycleOwner
        activity = getActivity() as MainActivity
        initToolbar()
        setupListAdapter()
        onItemClick()
        smartListener()
        homeViewModel.refresh()
    }

    private fun smartListener() {
        //刷新
        homeFragDataBinding.smartRefresh.setOnRefreshListener {
            homeViewModel.refresh()
        }
        //加载更多
        homeFragDataBinding.smartRefresh.setOnLoadMoreListener {

        }

        homeViewModel.isRefreshing.observe(viewLifecycleOwner,{
            if (!it){
                homeFragDataBinding.smartRefresh.finishRefresh()
            }
        })
        homeViewModel.isLoadMoreing.observe(viewLifecycleOwner,{
            if (!it){
                homeFragDataBinding.smartRefresh.finishLoadMore()
            }
        })
    }

    override fun onStart() {
        super.onStart()
        //刷新数据(如果断点,会影响)
        homeViewModel.banners.observe(viewLifecycleOwner,{})
        homeViewModel.itemList.observe(viewLifecycleOwner,{})

    }

    /**
     * 列表项的点击
     */
  private fun onItemClick(){
       homeViewModel.openItem.observe(viewLifecycleOwner,{
           it.toString().sMainToast()
       })
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
     * 设置toolbar,由于只有首页用到了boolbar,就不妨到baseFrag中了
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
                    startActivityForResult(
                        Intent(activity, SearchActivity::class.java),
                        Const.Search.REQUEST_SEARCH, options
                    )
                } else {
                    startActivityForResult(
                        Intent(activity, SearchActivity::class.java),
                        Const.Search.REQUEST_SEARCH
                    )
                }
            }
        }
        return true
    }

    override fun onItemClick(data: HomeBanner, position: Int) {
        position.toString().sMainToast()
    }
}