package com.bzsdk.bzloginmodule.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.bzsdk.bzloginmodule.R;

public class LoadingDialog {
    Context mContext;
    Dialog mDialog;
    private long timeOut = 10000;
    private long inverval = 500;
    CountDownTimer countDownTimer;
    private boolean isCountDown = false;
    public LoadingDialog(Context context) {
        InitDialog(context);
    }

    private void InitDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.loading_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.create();

        countDownTimer = new CountDownTimer(timeOut, inverval) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                HideDialog();
            }
        };
    }

    public void ShowDialog(String title) {
        if (mDialog.isShowing())
            mDialog.dismiss();

        if(isCountDown) {
            countDownTimer.cancel();
            HideDialog();
        }

        TextView textView = mDialog.findViewById(R.id.textView);
        textView.setText(title);

        mDialog.show();

        countDownTimer.start();
        isCountDown = true;

    }

    public void HideDialog() {
        isCountDown = false;
        mDialog.dismiss();
    }
}
