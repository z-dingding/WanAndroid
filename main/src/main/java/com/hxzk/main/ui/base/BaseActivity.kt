package com.hxzk.main.ui.base

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewStub
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.hxzk.base.extension.logWarn
import com.hxzk.base.util.ActivityCollector
import com.hxzk.base.util.AndroidVersion
import com.hxzk.base.util.progressdialog.ProgressDialogUtil
import com.hxzk.main.R
import com.hxzk.main.callback.PermissionListener
import com.hxzk.main.callback.RequestLifecycle
import com.hxzk.main.event.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference
import java.util.*

open abstract class BaseActivity : AppCompatActivity(), RequestLifecycle {


    var activity: Activity? = null
    var weakReference: WeakReference<Activity>? = null

    /**
     * Activity中由于服务器异常导致加载失败显示的布局。
     */
    private var loadErrorView: View? = null

    /**
     * Activity中由于网络异常导致加载失败显示的布局。
     */
    private var badNetworkView: View? = null

    /**
     * Activity中当界面上没有任何内容时展示的布局。
     */
    private var noContentView: View? = null


    var toolbar: Toolbar? = null

    private var mListener: PermissionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        weakReference = WeakReference(this)
        ActivityCollector.add(weakReference)
        EventBus.getDefault().register(this)
    }


    override fun setContentView(layoutResID: Int) {
        //设置竖屏
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.setContentView(layoutResID)
        setupViews()
    }



    override fun onDestroy() {
        super.onDestroy()
        activity = null
        ActivityCollector.remove(weakReference)
        EventBus.getDefault().unregister(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 将状态栏设置成透明。只适配Android 5.0以上系统的手机。
     */
    protected fun transparentStatusBar() {
        if (AndroidVersion.hasLollipop()) {
            val decorView = window.decorView
            decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.statusBarColor = Color.TRANSPARENT
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {


    }

    open fun setupViews(){}

    protected fun setupToolbar(title:String = "") {
        toolbar = findViewById(R.id.toolbar)
        if (title.isNotBlank()){
            toolbar?.title = title
        }
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        //true给左上角图标的左边加上一个返回的图标
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun startLoading() {
        hideAllStateView()
        ProgressDialogUtil.getInstance().showDialog(this)
    }


    override fun loadFailed(msg: String?) {
        ProgressDialogUtil.getInstance().dismissDialog()
    }

    override fun loadFinished() {
        ProgressDialogUtil.getInstance().dismissDialog()
    }

    /**
     * 隐藏服务器异常，没有网络，没有数据三种状态的View
     */

    fun hideAllStateView() {
        badNetworkView?.visibility = View.GONE
        noContentView?.visibility = View.GONE
        loadErrorView?.visibility = View.GONE
    }

    /**
     * 当Activity中的加载内容服务器返回失败，通过此方法显示提示界面给用户。
     *
     * @param tip
     * 界面中的提示信息
     */
    protected fun showLoadErrorView(tip: String) {
        if (loadErrorView != null) {
            loadErrorView?.visibility = View.VISIBLE
            return
        }
        val viewStub = findViewById<ViewStub>(R.id.loadErrorView)
        if (viewStub != null) {
            loadErrorView = viewStub.inflate()
            val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.loadErrorText)
            loadErrorText?.text = tip
        }
    }

    /**
     * 当Activity中的内容因为网络原因无法显示的时候，通过此方法显示提示界面给用户。
     *
     * @param listener
     * 重新加载点击事件回调
     */
    protected fun showBadNetworkView(listener: View.OnClickListener) {
        if (badNetworkView != null) {
            badNetworkView?.visibility = View.VISIBLE
            return
        }
        val viewStub = findViewById<ViewStub>(R.id.badNetworkView)
        if (viewStub != null) {
            badNetworkView = viewStub.inflate()
            val badNetworkRootView = badNetworkView?.findViewById<View>(R.id.badNetworkRootView)
            badNetworkRootView?.setOnClickListener(listener)
        }
    }

    /**
     * 当Activity中没有任何内容的时候，通过此方法显示提示界面给用户。
     * @param tip
     * 界面中的提示信息
     */
    protected fun showNoContentView(tip: String = "暂无数据") {
        if (noContentView != null) {
            noContentView?.visibility = View.VISIBLE
            return
        }
        val viewStub = findViewById<ViewStub>(R.id.noContentView)
        if (viewStub != null) {
            noContentView = viewStub.inflate()
            val noContentText = noContentView?.findViewById<TextView>(R.id.noContentText)
            noContentText?.text = tip
        }
    }



    /**
     * 检查和处理运行时权限，并将用户授权的结果通过PermissionListener进行回调。
     *
     * @param permissions
     * 要检查和处理的运行时权限数组
     * @param listener
     * 用于接收授权结果的监听器
     */
    protected fun handlePermissions(permissions: Array<String>?, listener: PermissionListener) {
        if (permissions == null || activity == null) {
            return
        }
        mListener = listener
        //过滤出需要请求的权限(部分权限已经授权)进行请求
        val requestPermissionList = ArrayList<String>()
        for (permission in permissions) {
            //如果权限没有授予
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionList.add(permission)
            }
        }
        if (!requestPermissionList.isEmpty()) {
            ActivityCompat.requestPermissions(activity!!, requestPermissionList.toTypedArray(), 1)
        } else {
            listener.onGranted()
        }
    }

    open fun permissionsGranted() {
        // 由子类来进行具体实现
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        //请求授权的权限数组
        permissions: Array<String>,
        //授权结果,包括授权和拒绝的
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //处理没有授权权限的申请结果
        when (requestCode) {
            1 -> if (grantResults.isNotEmpty()) {
                val deniedPermissions = ArrayList<String>()
                for (i in grantResults.indices) {
                    val grantResult = grantResults[i]
                    val permission = permissions[i]
                    //0授权，-1拒绝授权
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        deniedPermissions.add(permission)
                    }
                }
                if (deniedPermissions.isEmpty()) {
                    mListener!!.onGranted()
                } else {
                    mListener!!.onDenied(deniedPermissions)
                }
            }
        }
    }



    /**
     * 隐藏软键盘。
     */
    fun hideSoftKeyboard() {
        try {
            //获取当前activity中获得焦点的view
            val view = currentFocus
            if (view != null) {
                //获取调用的view依附在哪个window的令牌
                val binder = view.windowToken
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.hideSoftInputFromWindow(binder, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        } catch (e: Exception) {
            logWarn( e.message, e)
        }

    }

    /**
     * 显示软键盘。
     */
    fun showSoftKeyboard(editText: EditText?) {
        try {
            if (editText != null) {
                editText.requestFocus()
                val manager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                manager.showSoftInput(editText, 0)
            }
        } catch (e: Exception) {
            logWarn( e.message, e)
        }

    }
}