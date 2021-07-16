package com.hxzk.main.ui.x5Webview

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.main.ui.publics.PublicViewModel
import com.hxzk.network.model.CommonItemModel
import com.hxzk.tencentx5.callback.WebViewProgress
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.fragment_x5.*


class X5Fragment : BaseFragment(), WebViewProgress {

    private val viewModel by viewModels<X5FragViewModel> { getViewModelFactory()}

    lateinit var model: CommonItemModel
    lateinit var activity: X5MainActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_x5, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity = requireActivity() as X5MainActivity
        model = activity.intent.getParcelableExtra(X5MainActivity.KEY_ITEMBEAN)
        //设置加载进度的回调
        x5WebView.x5WebChromeClient.setOnProgressChanged(this)
        x5WebView.loadWebUrl(model.link)
        webProgressView.setOnClickListener {
            activity.finish()
        }
        //如果已经收藏设置返回按钮背景为红色
        if(model.collect) webProgressView.setmBgColor(Color.RED)
    }



    // 上次点击时间
    private var lastClickTime = 0L

    // 两次点击间隔时间（毫秒）
    private val FAST_CLICK_DELAY_TIME = 500

    @SuppressLint("ClickableViewAccessibility")
    override fun onStart() {
        super.onStart()
        x5WebView.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_DOWN == event?.action) {
                if (System.currentTimeMillis() - lastClickTime >= FAST_CLICK_DELAY_TIME){
                    lastClickTime = System.currentTimeMillis()
                }else{
                    //执行收藏逻辑
              viewModel.collecteArticle(model.id)
                }
            }
            false
        }
        //存储历史记录
        model.browseTime =GlobalUtil.currentDateString
        model.let { viewModel.insertItem(it) }
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        if(webProgressView != null){
            webProgressView.setmCurrent(newProgress)
        }
    }

}