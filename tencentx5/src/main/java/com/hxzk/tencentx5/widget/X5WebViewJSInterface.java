package com.hxzk.tencentx5.widget;


import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Used Java和Java script交互的工具类
 */

public class X5WebViewJSInterface {
    private static final String TAG = "X5WebViewJSInterface";

    private X5WebViewJSInterface jsInterface;
    /**
     * 依赖的窗口
     */
    private Context mContext;
    private X5WebView x5WebView;

    Handler handler = new Handler(Looper.getMainLooper());
    /**
     * 当前页面所有的图片集合
     */
    ArrayList<String> imageURLList = new ArrayList<>();

    public X5WebViewJSInterface(Context context, X5WebView webView) {
        mContext = context;
        x5WebView = webView;
    }

    public X5WebView getX5Webview() {
        return x5WebView;
    }


    @JavascriptInterface
    public void scanCode(final String imageUrl) {
        //js调用android是在非主线程中,所以要切回来
        handler.post(new Runnable() {
            @Override
            public void run() {
                getImage(imageUrl);
            }
        });

    }

    @JavascriptInterface
    public void addImageUrl(String url) {
        imageURLList.add(url);
    }

    @JavascriptInterface
    public void showImage(String url) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: 点击了图片");
                Toast.makeText(mContext, "点击了图片", Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 二维码识别
     *
     * @param imageUrl 二维码图片的地址
     */
    private void getImage(String imageUrl) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: 点击了二维码识别");
                Toast.makeText(mContext, "点击了二维码识别", Toast.LENGTH_LONG).show();
            }
        });
    }

}
