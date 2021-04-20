package com.hxzk.tencentx5.callback

import com.tencent.smtt.sdk.WebView

/**
 *作者：created by zjt on 2021/4/20
 *描述:webview加载进度的回调
 *
 */
interface WebViewProgress {
    fun onProgressChanged(view : WebView, newProgress : Int)
}