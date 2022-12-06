package com.bzsdk.bzloginmodule;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.bzsdk.bzloginmodule.dialogs.LoadingDialog;
import com.bzsdk.bzloginmodule.fragments.SignInFragment;
import com.bzsdk.bzloginmodule.fragments.SignUpFragment;
import com.bzsdk.bzloginmodule.network.LoginResponse;
import com.google.gson.Gson;
import com.rofi.core.network.Constants;

public class LoginActivity extends FragmentActivity {

    private Fragment mCurFragment;
    private LoadingDialog mLoadingDialog;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        ActionBar actionBar = getSupportActionBar();
//        if(actionBar != null) actionBar.hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        mLoadingDialog = new LoadingDialog(LoginActivity.this);

        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view -> {
            LoginActivity.this.onBackPressed();
        });

        if (savedInstanceState == null) {
            ShowSignInFragment();
        }

        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        String appLinkAction = appLinkIntent.getAction();
        Uri appLinkData = appLinkIntent.getData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    public void ShowSignInFragment() {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mCurFragment = new SignInFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_container, mCurFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void ShowSignUpFragment() {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        mCurFragment = new SignUpFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_container, mCurFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void showLoadingDialog(String title) {
        mLoadingDialog.ShowDialog(title);
    }

    public void hideLoadingDialog() {
        mLoadingDialog.HideDialog();
    }

    public void onLoginSuccess(String data) {
        LoginService.getInstance().SaveAccessTokenAfterLogin(getApplicationContext(), data);
        mLoadingDialog.HideDialog();
        finish();
        LoginService.getInstance().DispatchOnLogin(data);
    }

}