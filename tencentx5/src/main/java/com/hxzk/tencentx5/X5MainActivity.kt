package com.hxzk.tencentx5

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.hxzk.network.model.CommonItemModel
import com.hxzk.tencentx5.callback.WebViewProgress
import com.tencent.smtt.sdk.WebView
import kotlinx.android.synthetic.main.activity_x5main.*

class X5MainActivity : AppCompatActivity() , WebViewProgress {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_x5main)
        nativeLightStatusBar(true)
        val model = intent.getParcelableExtra<CommonItemModel>(KEY_ITEMBEAN)
        //设置加载进度的回调
        x5WebView.x5WebChromeClient.setOnProgressChanged(this)
        x5WebView.loadWebUrl(model.link)
        webProgressView.setOnClickListener {
            finish()
        }
    }



    /**
     * 将状态栏设置成透明。只适配Android 5.0以上系统的手机。
     * 谷歌原生方式改变状态栏文字颜色,只支持6.0
     */
    private fun nativeLightStatusBar(dark: Boolean) {
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



    companion object {
        const val KEY_ITEMBEAN = "key_itembean"
    }

    override fun onProgressChanged(view: WebView, newProgress: Int) {
        webProgressView.setmCurrent(newProgress)
    }
}