package com.hxzk.tencentx5.widget;


import android.content.Context;


/**
 * Used Java和Java script交互的工具类
 */

public class X5WebViewJSInterface {
    private static final String TAG = "X5WebViewJSInterface";

    private static X5WebViewJSInterface instance;
    /**
     * 依赖的窗口
     */
    private Context mContext;
    private static X5WebView x5WebView;

    public static X5WebViewJSInterface getInstance(Context mContext, X5WebView x5WebView) {
        if (instance == null) {
            instance = new X5WebViewJSInterface();
        }
        instance.mContext = mContext;
        X5WebViewJSInterface.x5WebView = x5WebView;
        return instance;
    }

    public static X5WebView getX5Webview() {
        return x5WebView;
    }

}
