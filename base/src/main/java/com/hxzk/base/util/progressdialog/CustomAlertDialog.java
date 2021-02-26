package com.hxzk.base.util.progressdialog;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import com.hxzk.base.R;
import com.wang.avi.AVLoadingIndicatorView;


/**
 * 作者：created by ${zjt} on 2019/4/27
 * 描述:
 */
public class CustomAlertDialog extends AlertDialog {

    private AVLoadingIndicatorView avi;

    protected CustomAlertDialog(Context context) {
        super(context);
    }

    public CustomAlertDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_custom);
        avi=findViewById(R.id.avi);
        avi.show();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        avi.hide();
    }
}
