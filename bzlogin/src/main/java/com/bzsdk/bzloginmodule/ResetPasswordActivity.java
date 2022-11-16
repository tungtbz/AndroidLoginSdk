package com.bzsdk.bzloginmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bzsdk.bzloginmodule.dialogs.LoadingDialog;
import com.bzsdk.bzloginmodule.fragments.PassordRecoveryFragment;
import com.bzsdk.bzloginmodule.fragments.SignInFragment;

public class ResetPasswordActivity extends AppCompatActivity {

    private Fragment mCurFragment;
    private LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_reset_password);

        mLoadingDialog = new LoadingDialog(ResetPasswordActivity.this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        ShowFragment();
    }

    public void ShowFragment() {
        // Begin the transaction
        FragmentTransaction ft = ResetPasswordActivity.this.getSupportFragmentManager().beginTransaction();
        mCurFragment = new PassordRecoveryFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_container, mCurFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }
}