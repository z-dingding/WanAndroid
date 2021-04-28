package com.hxzk.tencentx5.widget;

import android.content.Context;
import android.net.Uri;

import com.hxzk.tencentx5.callback.WebViewProgress;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;


/**
 * Used 处理解析，渲染网页等浏览器做的事情。辅助WebView处理Javascript的对话框，网站图标，网站title，加载进度等
 */
public class X5WebChromeClient extends WebChromeClient {

	private static final String TAG = "X5WebChromeClient";


	private Context mContext;
	private X5WebView x5WebView;



	public X5WebChromeClient(Context context, X5WebView x5WebView) {
		this.mContext = context;
		this.x5WebView = x5WebView;
	}

	/**
	 * 加载进度值
	 */
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		super.onProgressChanged(view, newProgress);
		if(mWebViewProgress != null){
			mWebViewProgress.onProgressChanged(view,newProgress);
		}
	}

	WebViewProgress mWebViewProgress;
	public void setOnProgressChanged(WebViewProgress webViewProgress){
		mWebViewProgress = webViewProgress;
	}


	/*=========================================实现webview打开文件管理器功能==============================================*/

    //5.0--版本用到的
	private static  com.tencent.smtt.sdk.ValueCallback<Uri> choseFileUri;
	//5.0++版本用到的
	private  static com.tencent.smtt.sdk.ValueCallback <Uri[]> choseFileUriArr;


	/**
	 * 5.0及以上系统回调onShowFileChooser
	 */
	@Override
	public boolean onShowFileChooser(WebView webView,
                                     ValueCallback<Uri[]> filePathCallback,
                                     FileChooserParams fileChooserParams) {

		openFileChooserImplForAndroid5(filePathCallback);
		return true;
	}

	/**
	 * 5.0++的调用
	 */
	private void openFileChooserImplForAndroid5(com.tencent.smtt.sdk.ValueCallback <Uri[]> filePathCallback) {
		choseFileUriArr = filePathCallback;
		dispatchTakePictureIntent();
	}
	/**
	 * 	5.0以下系统回调openFileChooser方法(没有包括3.0及其以下,因为几乎不存在3.0的手机了，所以没有考虑)
	 */
	@Override
	public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
		openFileChooserImpl(valueCallback);
	}


	/**
	 * 5.0--的调用
	 */

	private void openFileChooserImpl(com.tencent.smtt.sdk.ValueCallback <Uri> valueCallback) {
		choseFileUri = valueCallback ;
		dispatchTakePictureIntent();
	}


	/**
	 * 拍照或者打开文件管理器
	 */
	private void dispatchTakePictureIntent() {

	}


	public com.tencent.smtt.sdk.ValueCallback <Uri> getmUploadMessage() {
		return choseFileUri;
	}
	public void setChoseFileUri(com.tencent.smtt.sdk.ValueCallback<Uri> choseFileUri) {
		X5WebChromeClient.choseFileUri = choseFileUri;
	}

	public com.tencent.smtt.sdk.ValueCallback <Uri[]> getmUploadCallbackAboveL() {
		return choseFileUriArr;
	}
	public void setChoseFileUriArr(com.tencent.smtt.sdk.ValueCallback <Uri[]> choseFileUriArr) {
		X5WebChromeClient.choseFileUriArr = choseFileUriArr;
	}

}
