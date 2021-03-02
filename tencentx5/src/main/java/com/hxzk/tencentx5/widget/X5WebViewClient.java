package com.hxzk.tencentx5.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.hxzk.base.extension.LogKt;

import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Used 主要处理解析，渲染网页等浏览器做的事情。
 * 帮助WebView处理各种通知、请求事件：比如页面加载的开始、结束、失败时的对话框提示
 */
public class X5WebViewClient extends WebViewClient {
    private static final String TAG = "X5WebViewClient";

    /**
     * 依赖的窗口
     */
    private  static Context context;
    private static X5WebView x5WebView;
    /**
     * 是否需要清除历史记录
     */
    private boolean needClearHistory = false;

    public X5WebViewClient(Context conx, X5WebView webView) {
        context = conx;
       x5WebView = webView;
    }


    //设置 WebViewClient且该方法返回 false则说明由 WebView 处理该 url,
    //若设置 WebViewClient 且该方法返回 true ，WebView 不处理，也就是程序员自己做处理
    //return true 表示当前url即使是重定向url也不会再执行
    //return false  表示由系统执行url，直到不再执行此方法，即加载完重定向的ur（即具体的url，不再有重定向）
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        String url = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            url = request.getUrl().toString();
        } else {
            url = request.toString();
        }
        if (!TextUtils.isEmpty(url)) {
            if (url.contains("tel:")) {
                Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(url));
                context.startActivity(intent);
            }else if(url.contains("@")){
                Uri uri = Uri.parse(url);
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                context.startActivity(it);
                //注意endsWith不能区分大小写
            }else if (url.contains("rar")
                    || url.endsWith(".docx") || url.endsWith(".doc")
                    || url.endsWith(".xls") || url.endsWith(".xlsx")  || url.endsWith(".PDF") ||url.endsWith(".pdf")|| url.endsWith(".excel")|| url.endsWith(".ppt")|| url.endsWith(".xlst")|| url.endsWith(".txt")) {
              //处理文件

            }
            else if(url.contains("http")  || url.contains("https") ){
                //view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, request);
            }
        }
        return true;
    }




    /**
     * 网页加载开始时调用，显示加载提示旋转进度条
     */
    @Override
    public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
        super.onPageStarted(webView, url, bitmap);
        LogKt.logDebug(TAG, "{onPageStarted}url=" + url);
    }

    /**
     * 网页加载完成时调用，比如：隐藏加载提示旋转进度条
     */
    @Override
    public void onPageFinished(WebView webView, String url) {
        super.onPageFinished(webView, url);
        LogKt.logDebug(TAG, "{onPageFinished}url=" + url);
    }



    /**
     * 网页加载失败时调用，隐藏加载提示旋转进度条
     * 捕获的是 文件找不到，网络连不上，服务器找不到等问题
     */

    @Override
    public void onReceivedError(WebView webView, int errorCode,
                                String description, String failingUrl) {
        super.onReceivedError(webView, errorCode, description, failingUrl);
        LogKt.logDebug(TAG, "{onReceivedError}failingUrl=" + failingUrl);
    }


    /**
     * 直接捕获到404
     */
    @Override
    public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
        super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        String url = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            url = webResourceRequest.getUrl().toString();
        } else {
            url = webResourceRequest.toString();
        }
        LogKt.logDebug(TAG, "{onReceivedHttpError}url=" + url);
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        super.onReceivedSslError(webView, sslErrorHandler, sslError);
        //信任来自任何网站(http站)的证书
        //sslErrorHandler.proceed();
        sslErrorHandler.cancel();
    }



    /*=================================根据需要清除历史记录=================================*/
    @Override
    public void doUpdateVisitedHistory(WebView webView, String url, boolean isReload) {
        super.doUpdateVisitedHistory(webView, url, isReload);
        if (needClearHistory) {
            //清除历史记录
            webView.clearHistory();
            needClearHistory = false;
        }
    }

    public void setNeedClearHistory(boolean needClearHistory) {
        this.needClearHistory = needClearHistory;
    }



}