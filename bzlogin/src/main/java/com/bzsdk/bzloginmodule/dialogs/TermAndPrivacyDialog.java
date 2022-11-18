package com.bzsdk.bzloginmodule.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Button;

import com.bzsdk.bzloginmodule.R;

public class TermAndPrivacyDialog {
    Dialog mDialog;
    WebView webView;

    public TermAndPrivacyDialog(Context context) {
        InitDialog(context);
    }

    private void InitDialog(Context context) {
        mDialog = new Dialog(context);
        mDialog.setContentView(R.layout.dialog_term_privacy_layout);
//        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.create();

        webView = mDialog.findViewById(R.id.webview);

        Button closeBtn = mDialog.findViewById(R.id.btn_close);
        closeBtn.setOnClickListener(view -> {
            mDialog.dismiss();
        });
    }

    public void showDialog(String url) {
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        webView.loadUrl(url);
    }


}
