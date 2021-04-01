package com.hxzk.base.util.progressdialog;

import android.app.Activity;
import android.content.Context;

import com.hxzk.base.R;


/**
 * 作者：created by ${zjt} on 2019/3/1
 * 描述:登录等加载进度框
 */
public class ProgressDialogUtil {


    public volatile  static ProgressDialogUtil progressDialog;
    private  static CustomAlertDialog loadingDialog;


    private ProgressDialogUtil() {
    }



    public static ProgressDialogUtil getInstance(){
        if(progressDialog == null){
           synchronized (ProgressDialogUtil.class){
               if(progressDialog == null){
                   progressDialog =new ProgressDialogUtil();
                   return progressDialog;
               }
           }
        }
        return progressDialog;
    }



    /**
     * 显示Loading
     * @param mContext 上下文
     */
    public void showDialog(Context mContext) {
        // 如果Activity结束，返回true;否则返回false。
        if (!((Activity) mContext).isFinishing()) {
            if (null == loadingDialog) {
                loadingDialog = new CustomAlertDialog(mContext, R.style.Dialog_image);
                loadingDialog.setCancelable(false);
                loadingDialog.show();
            } else {
                if (!loadingDialog.isShowing()) {
                    loadingDialog.show();
                }
            }
        }
    }

    /**
     * 让Loading消失
     */
    public void dismissDialog() {
        if (null != loadingDialog && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }


}
