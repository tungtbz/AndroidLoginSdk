package com.bzsdk.loginmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bzsdk.loginmodule.dialogs.LoadingDialog;
import com.bzsdk.loginmodule.fragments.SignInFragment;
import com.bzsdk.loginmodule.fragments.SignUpFragment;
import com.bzsdk.loginmodule.network.NetworkService;
import com.facebook.CallbackManager;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends AppCompatActivity {

    private Fragment mCurFragment;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        mLoadingDialog = new LoadingDialog(LoginActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ShowSignInFragment();

        NetworkService.getInstance().Init(LoginActivity.this);
    }

    public void ShowSignInFragment() {
        // Begin the transaction
        FragmentTransaction ft = LoginActivity.this.getSupportFragmentManager().beginTransaction();
        mCurFragment = new SignInFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_container, mCurFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void ShowSignUpFragment() {
        // Begin the transaction
        FragmentTransaction ft = LoginActivity.this.getSupportFragmentManager().beginTransaction();
        mCurFragment = new SignUpFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_container, mCurFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void ShowLoadingDialog(String title) {
        mLoadingDialog.ShowDialog(title);
    }

    public void HideLoadingDialog() {
        mLoadingDialog.HideDialog();
    }
}