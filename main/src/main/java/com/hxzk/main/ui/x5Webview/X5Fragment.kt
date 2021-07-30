package com.hxzk.main.ui.x5Webview

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.CheckBox
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import com.hxzk.base.extension.sToast
import com.hxzk.base.util.GlobalUtil
import com.hxzk.main.R
import com.hxzk.main.extension.getViewModelFactory
import com.hxzk.main.ui.base.BaseFragment
import androidx.lifecycle.observe
import com.hxzk.base.util.Preference
import com.hxzk.main.common.Const
import com.hxzk.network.WanApi
import com.hxzk.network.model.CommonItemModel
import com.hxzk.tencentx5.callback.WebViewProgress
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.fragment_x5.*


class X5Fragment : BaseFragment(), WebViewProgress {

    private val viewModel by viewModels<X5FragViewModel> { getViewModelFactory()}

    lateinit var model: CommonItemModel
    lateinit var activity: X5MainActivity

    /**
     * 是否选择了不在提醒
     */
    private var isForwardAndDoNotAlertAgainChecked = false

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
        if(model.link.contains("http")){
            x5WebView.loadWebUrl(model.link)
        }else{
            x5WebView.loadWebUrl(WanApi.baseUrl+model.link)
        }

        webProgressView.setOnClickListener {
            activity.finish()
        }
        //如果已经收藏设置返回按钮背景为红色
        if(model.collect) webProgressView.setmBgColor(Color.RED)
        //存储历史记录
        model.browseTime =GlobalUtil.currentDateString
        model.let { viewModel.insertItem(it) }

        initEvent()
        //提醒用户,双击可以收藏文章
        showAlert()
    }

    private fun showAlert() {
        var isSHowTip  by Preference(Const.X5Fragment.KEY_COLLECTIONTIPS,true)
        if(isSHowTip){
        val dialogView = LayoutInflater.from(activity).inflate(R.layout.dialog_alert_for_big_gif, null)
        val checkBox = dialogView.findViewById<CheckBox>(R.id.checkbox)
        val dialog = AlertDialog.Builder(activity, R.style.AlertDialogStyle)
                .setTitle(GlobalUtil.getString(R.string.remind))
                .setMessage(GlobalUtil.getString(R.string.you_are_playing_big_gif_with_roaming))
                .setView(dialogView)
                .setPositiveButton(GlobalUtil.getString(R.string.forward)) { _, _ ->
                    if (isForwardAndDoNotAlertAgainChecked) {
                        //选择不在提示,将值改为不在提示
                        isSHowTip =false
                    }

                }.create()
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            isForwardAndDoNotAlertAgainChecked = isChecked
        }
        }
    }


    // 上次点击时间
    private var lastClickTime = 0L
    // 两次点击间隔时间（毫秒）
    private val FAST_CLICK_DELAY_TIME = 500

    private fun initEvent() {
        x5WebView.setOnTouchListener { v, event ->
            if (MotionEvent.ACTION_DOWN == event?.action) {
                if (System.currentTimeMillis() - lastClickTime >= FAST_CLICK_DELAY_TIME){
                    lastClickTime = System.currentTimeMillis()
                }else{
                    //执行收藏逻辑
                    viewModel.collecteArticle(model.id)
                    // TODO: 2021/7/19 需要去更新列表中的收藏状态为已收藏 
                }
            }
            false
        }

        viewModel.isCollectionSuccess.observe(activity){
            //如果收藏成功就改变返回按钮背景颜色
            webProgressView.setmBgColor(Color.RED)

        }
    }



    override fun onProgressChanged(view: WebView, newProgress: Int) {
        if(webProgressView != null){
            webProgressView.setmCurrent(newProgress)
        }
    }

}