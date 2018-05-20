package com.cyl.musiclake.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import com.cyl.musiclake.utils.LogUtil;
import android.widget.ProgressBar;

/**
 * Created by master on 2018/4/7.
 */

public class DialogUtils {

    private static AlertDialog mProgressDialog;

    public static final void showResultDialog(Context context, String msg,
                                              String title) {
        if (msg == null) return;
        String rmsg = msg.replace(",", "\n");
        LogUtil.d("Util", rmsg);
        new AlertDialog.Builder(context).setTitle(title).setMessage(rmsg)
                .setNegativeButton("知道了", null).create().show();
    }

    public static final void showProgressDialog(Context context, String title,
                                                String message) {
        dismissDialog();
        if (TextUtils.isEmpty(title)) {
            title = "请稍候";
        }
        if (TextUtils.isEmpty(message)) {
            message = "正在加载...";
        }
        mProgressDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setView(new ProgressBar(context))
                .create();
    }

    public static AlertDialog showConfirmCancelDialog(Context context,
                                                      String title, String message,
                                                      DialogInterface.OnClickListener posListener) {
        AlertDialog dlg = new AlertDialog.Builder(context).setMessage(message)
                .setPositiveButton("确认", posListener)
                .setNegativeButton("取消", null).create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
        return dlg;
    }

    public static final void dismissDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
