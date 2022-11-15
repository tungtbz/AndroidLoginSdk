package com.bzsdk.loginmodule.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.TextView;

import com.bzsdk.loginmodule.R;

public class LoadingDialog {
    Context mContext;
    Dialog mDialog;

    public LoadingDialog(Context context) {
        InitDialog(context);
    }

    private void InitDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.loading_dialog);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        mDialog.create();
    }

    public void ShowDialog(String title) {
        if (mDialog.isShowing()) mDialog.dismiss();

        TextView textView = mDialog.findViewById(R.id.textView);
        textView.setText(title);

        mDialog.show();
    }

    public void HideDialog() {
        mDialog.dismiss();
    }
}
