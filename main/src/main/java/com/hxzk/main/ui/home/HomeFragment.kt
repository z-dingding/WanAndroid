package com.hxzk.main.ui.home

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.AndroidVersion
import com.hxzk.main.R
import com.hxzk.main.common.Const
import com.hxzk.main.databinding.FragmentHomeBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.BannerImageAdapter
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.search.SearchActivity
import com.hxzk.main.util.ResponseHandler
import com.hxzk.network.Result
import com.hxzk.network.model.ApiResponse
import com.hxzk.network.model.HomeBanner
import com.hxzk.network.succeeded
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * 首页Fragment
 */
class HomeFragment : BaseFragment() {

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
        //Frament中显示Toolbar
        setHasOptionsMenu(true)
        return super.onCreateView(homeFragDataBinding.root)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = getActivity() as MainActivity
        initToolbar()
        setupListAdapter()
        bannerLiveData()
        topAriticla()
    }


    private fun setupListAdapter() {
        val viewModel = homeFragDataBinding.viewModel
        if (viewModel != null) {
            listAdapter = HomeItemAdapter(viewModel)
            homeFragDataBinding.recycler.adapter = listAdapter
        }
    }
    /**
     * 首页banner的监听
     */
    private fun bannerLiveData() {
        homeViewModel.banners.observe(viewLifecycleOwner, {
            if (it.succeeded) {
                val bean = (it as Result.Success<*>).res as ApiResponse<*>
                if (bean.errorCode == 0) {
                    initBanner(bean.data as List<HomeBanner>)
                } else {
                    bean.errorMsg.sToast()
                }
            } else {
                val res = it as Result.Error
                ResponseHandler.handleFailure(res.e)
            }
        })
    }

    /**
     * 首页置顶的请求
     */
    fun topAriticla(){
        homeViewModel.loadTopArticle()
        homeViewModel.topArticle.observe(viewLifecycleOwner,{
            if (it.isNotEmpty()) {
                (recycler.adapter as HomeItemAdapter).submitList(it)
            }
        })
    }

    private fun initBanner(mData: List<HomeBanner>) {
        // 暂时不支持kotlin,所以只能这样写
        mBanner.adapter = BannerImageAdapter(mData, activity)
        //添加生命周期观察者
        mBanner.addBannerLifecycleObserver(this)
        //banner设置方法全部调用完毕时最后调用
        mBanner.start()
    }


    private fun initToolbar() {
        //toolbar = rootView?.findViewById(R.id.toolbar)
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
}