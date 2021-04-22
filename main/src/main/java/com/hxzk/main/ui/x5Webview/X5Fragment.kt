package com.hxzk.main.ui.x5Webview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.hxzk.base.extension.sToast
import com.hxzk.main.R
import com.hxzk.main.ui.base.BaseFragment
import com.hxzk.network.model.CommonItemModel
import com.hxzk.tencentx5.callback.WebViewProgress
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.fragment_x5.*


class X5Fragment : BaseFragment(), WebViewProgress {

    var model: CommonItemModel? = null
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
        model = activity.intent.getParcelableExtra<CommonItemModel>(X5MainActivity.KEY_ITEMBEAN)
        //设置加载进度的回调
        x5WebView.x5WebChromeClient.setOnProgressChanged(this)
        x5WebView.loadWebUrl(model?.link)
        webProgressView.setOnClickListener {
            activity.finish()
        }
    }


    // 上次点击时间
    private var lastClickTime = 0L

    // 两次点击间隔时间（毫秒）
    private val FAST_CLICK_DELAY_TIME = 500

    override fun onStart() {
        super.onStart()
        x5WebView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (MotionEvent.ACTION_DOWN == event?.getAction()) {
                    if (System.currentTimeMillis() - lastClickTime >= FAST_CLICK_DELAY_TIME){
                        lastClickTime = System.currentTimeMillis()
                    }else{
                        "双击了webview".sToast()
                    }
                }
                return false
            }
        })
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        webProgressView.setmCurrent(newProgress)
    }

}