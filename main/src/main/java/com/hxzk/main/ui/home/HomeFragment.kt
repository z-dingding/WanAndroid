package com.hxzk.main.ui.home

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.hxzk.base.extension.actionBundle
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.AndroidVersion
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.callback.BannerItemListener
import com.hxzk.main.databinding.FragmentHomeBinding
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.adapter.HomeItemAdapter
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.main.MainActivity
import com.hxzk.main.ui.search.SearchActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity
import com.hxzk.main.ui.x5Webview.X5MainActivity.Companion.KEY_ITEMBEAN
import com.hxzk.network.model.CommonItemModel
import com.hxzk.network.model.HomeBanner
import kotlinx.android.synthetic.main.fragment_home.*
import me.devilsen.czxing.Scanner
import me.devilsen.czxing.util.BarCodeUtil
import me.devilsen.czxing.view.ScanActivityDelegate.OnClickAlbumDelegate
import me.devilsen.czxing.view.ScanActivityDelegate.OnScanDelegate
import me.devilsen.czxing.view.ScanView
import java.util.*



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
        //使fragmeng中的toolbar菜单生效
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
            R.id.menu_scanCode ->{
                // TODO: 2021/7/15  需要增加权限判断
              startScanCode()
            }
        }
        return true
    }

    /**
     * 二维码扫描
     */
    private fun startScanCode() {
        Scanner.with(requireContext())
                .setBorderSize(BarCodeUtil.dp2px(requireContext(), 200f)) // 设置扫码框大小
                .setScanMode(ScanView.SCAN_MODE_TINY) // 扫描区域 0：混合 1：只扫描框内 2：只扫描整个屏幕
                .setTitle("扫描") // 扫码界面标题
                .showAlbum(true) // 显示相册(默认为true)
                .setScanNoticeText("扫描二维码") // 设置扫码文字提示
                .setFlashLightOnText("打开闪光灯") // 打开闪光灯提示
                .setFlashLightOffText("关闭闪光灯") // 关闭闪光灯提示
                .continuousScan() // 连续扫码，不关闭扫码界面
                .enableOpenCVDetect(false) // 关闭OpenCV探测，避免没有发现二维码也放大的现象，但是这样可能降低扫码的成功率，请结合业务关闭（默认开启）
                .setOnClickAlbumDelegate(object : OnClickAlbumDelegate {
                    override fun onClickAlbum(activity: Activity) {       // 点击右上角的相册按钮
                        val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        activity.startActivityForResult(albumIntent, CODE_SELECT_IMAGE)
                    }

                    override fun onSelectData(requestCode: Int, data: Intent) { // 选择图片返回的数据
                        if (requestCode == CODE_SELECT_IMAGE) {
                          
                        }
                    }
                })
                .setOnScanResultDelegate(OnScanDelegate { activity, result, format ->
                    // 接管扫码成功的数据
                    result.sToast()
                })
                .start()

    }

    override fun onItemClick(data: HomeBanner, position: Int) {
        val model = CommonItemModel(data.id,data.url,data.url)
        val mBundle =Bundle()
        mBundle.putParcelable(KEY_ITEMBEAN,model)
        activity.actionBundle<X5MainActivity>(activity,mBundle)
    }
    
    companion object{
        const val CODE_SELECT_IMAGE = 0x1111;
    }
}