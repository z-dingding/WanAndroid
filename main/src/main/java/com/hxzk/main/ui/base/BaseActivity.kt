package com.hxzk.main.ui.base

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hxzk.base.util.ActivityCollector
import com.hxzk.main.event.MessageEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

open  abstract class BaseActivity : AppCompatActivity() {


    var activity : Activity? = null
    var weakReference :WeakReference<Activity>? = null

    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        weakReference = WeakReference(this)
        ActivityCollector.add(weakReference)
        EventBus.getDefault().register(this)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setupViews()
    }

    override fun onDestroy() {
        super.onDestroy()
        activity = null
        ActivityCollector.remove(weakReference)
        EventBus.getDefault().unregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {}

    abstract fun  setupViews()

    fun showProgressDialog(title: String?, message: String) {
        if (progressDialog == null) {
            progressDialog = ProgressDialog(this).apply {
                if (title != null) {
                    setTitle(title)
                }
                setMessage(message)
                setCancelable(false)
            }
        }
        progressDialog?.show()
    }

    fun closeProgressDialog() {
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }
}