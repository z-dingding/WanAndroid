package com.hxzk.tencentx5.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;

import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;

/**
 * @author hxzk
 */
public class X5WebView extends WebView {

	private static final String TAG = "X5WebView";
	/**网页编码*/
	private static final String ENCODENAME = "utf-8";
	/**上下文*/
	private Context mContext;

	private  X5WebChromeClient x5WebChromeClient;
	private  X5WebViewClient x5WebViewClient;

	/**是否可以直接退出，还是返回到上一页(true:可以直接返回，默认；false：不可以直接返回)*/
	private boolean canReturn = true;
	/**当前Webview所处的上下文**/
	private Activity mActivity;


	/**在xml布局文件中定义的时候调用*/
	public X5WebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initWebViewSettings();
	}

	private void initWebViewSettings(){
		// settings 的设计
		WebSettings webSetting = this.getSettings();

		/*=============================JS的相关设置===========================================*/
		//设置WebView是否允许执行JavaScript脚本，默认false，不允许。
		webSetting.setJavaScriptEnabled(true);
		//让JavaScript自动打开窗口，默认false。适用于JavaScript方法window.open()。
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);

		/*=============================缓存机制的相关设置===========================================*/
		//是否允许访问文件，默认允许。注意，这里只是允许或禁止对文件系统的访问，Assets 和 resources 文件使用file:///android_asset和file:///android_res仍是可访问的。
		webSetting.setAllowFileAccess(true);
		//应用缓存API是否可用，默认值false, 结合setAppCachePath(String)使用。
		webSetting.setAppCacheEnabled(true);
		//设置app缓存容量
		webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
		//设置缓存路径
		webSetting.setAppCachePath(mContext.getApplicationContext().getDir("appcache", 0).getPath());
		// 使用localStorage则必须打开, 支持文件存储
		webSetting.setDomStorageEnabled(true);
		webSetting.setGeolocationEnabled(true);
		//只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据
		webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		webSetting.setGeolocationDatabasePath(mContext.getApplicationContext().getDir("geolocation", 0)
				.getPath());

		/*==============================webview页面自适应屏幕的相关设置===========================================*/
		// 排版适应屏幕 设置布局，会引起WebView的重新布局（relayout）,默认值NARROW_COLUMNS
		webSetting.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		//是否应该支持使用其屏幕缩放控件和手势缩放,默认值true
		webSetting.setSupportZoom(true);
		//设置触摸可缩放  ，默认值为false。
		webSetting.setBuiltInZoomControls(true);
		//设置WebView是否支持多窗口。如果设置为true，主程序要实现onCreateWindow(WebView, boolean, boolean, Message)，默认false。
		webSetting.setSupportMultipleWindows(false);
		//设置网页默认编码
		webSetting.setDefaultTextEncodingName(ENCODENAME);
		webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
		//使WebView能自适应手机屏幕大
		webSetting.setUseWideViewPort(true);
		webSetting.setLoadWithOverviewMode(true);

		//去掉缩放按钮
		webSetting.setBuiltInZoomControls(true);
		webSetting.setDisplayZoomControls(false);
		//去掉滚动条
		this.setVerticalScrollBarEnabled(false);
		this.setHorizontalScrollBarEnabled(false);



		//使用WebChormClient的特性处理html页面
		x5WebChromeClient = new X5WebChromeClient(getContext(),this);
		this.setWebChromeClient(x5WebChromeClient);
		//使用WebViewClient的特性处理html页面
		x5WebViewClient = new X5WebViewClient(getContext(),this);
		this.setWebViewClient(x5WebViewClient);

		//实现html文件中可以调用java方法
		addJavascriptInterface(X5WebViewJSInterface.getInstance(mContext,this), "androidMethod");
	}



	/**加载远程网页
	 * @param webUrl - 远程网页链接地址：比如http://m.baidu.com/*/
	public void loadWebUrl(String webUrl){
		this.loadUrl(webUrl);
	}

	/**加载本地assets目录下的网页
	 * @param localUrl - assets目录下的路径：比如www/login.html*/
	public void loadLocalUrl(final String localUrl){
		this.loadUrl("file:///android_asset/"+localUrl);
	}


	public void setCanReturn(boolean canReturn, Activity activtiy) {
		this.canReturn = canReturn;
		this.mActivity = activtiy;
	}

	/**按返回键时， 是不退出当前界面而是返回上一浏览页面还是直接退出当前界面*/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(!canReturn){
			if ((keyCode == KeyEvent.KEYCODE_BACK) && canGoBack()) {
				goBack();
				return true;
			}else if((keyCode == KeyEvent.KEYCODE_BACK) && !canGoBack()){
				//将操作返回到activity处理
				return mActivity == null;
			}
		}
		return super.onKeyDown(keyCode, event);
	}


	public X5WebChromeClient getX5WebChromeClient() {
		return x5WebChromeClient;
	}

	public X5WebViewClient getX5WebViewClient() {
		return x5WebViewClient;
	}

}
