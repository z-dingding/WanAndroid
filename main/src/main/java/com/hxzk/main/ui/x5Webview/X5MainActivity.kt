package com.hxzk.main.ui.x5Webview

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.Fragment
import com.hxzk.main.R
import com.hxzk.main.callback.PermissionListener
import com.hxzk.main.ui.NeedPermissionFragment
import com.hxzk.main.ui.NeedPermissionFragment.Companion.REQUEST_PERMISSION_SETTING
import com.hxzk.main.ui.base.BaseActivity
import com.hxzk.main.ui.base.BaseFragment

class X5MainActivity : BaseActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5main)
        refreshPermissionStatus()
    }

    override fun setupViews() {
        statusBarLight(true)
    }

    /**
     * 检查权限
     */
    private fun refreshPermissionStatus() {
        val requestPermission = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE)
        handlePermissions(requestPermission, object :
            PermissionListener {
            override fun onGranted() {
                permissionsGranted()
            }

            override fun onDenied(deniedPermissions: List<String>) {
                val fragment = NeedPermissionFragment()
                fragment.setPermissions(deniedPermissions.toTypedArray())
                replaceFragment(fragment)
            }
        })
    }
    lateinit var fragment : X5Fragment
    override fun permissionsGranted() {
      fragment = BaseFragment.getInstance<X5Fragment>(X5Fragment ::class.java,null)
        replaceFragment(fragment)
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commitAllowingStateLoss()
    }
    /**
     * 将状态栏设置成透明。只适配Android 5.0以上系统的手机。
     * 谷歌原生方式改变状态栏文字颜色,只支持6.0
     * true为设置状态栏为亮色模式（状态栏图标和文字变成黑色）
     */
    private fun statusBarLight(dark: Boolean) {
         //将状态栏设置成透明
        window.statusBarColor = Color.TRANSPARENT
        val decor: View = window.decorView
        if (dark) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //SYSTEM_UI_FLAG_LIGHT_STATUS_BAR设置状态栏为亮色模式（状态栏图标和文字变成黑色）
                decor.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        } else {
            //SYSTEM_UI_FLAG_LAYOUT_STABLE:在状态栏或者导航栏变化的时候不会被顶上去或者顶下去了
            decor.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_PERMISSION_SETTING -> refreshPermissionStatus()
        }
    }
    companion object {
        const val KEY_ITEMBEAN = "key_itembean"
    }

}