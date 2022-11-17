package com.bzsdk.bzloginmodule;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bzsdk.bzloginmodule.dialogs.LoadingDialog;
import com.bzsdk.bzloginmodule.fragments.PasswordRecoveryFragment;
import com.bzsdk.bzloginmodule.fragments.ResetPasswordCompleteFragment;
import com.bzsdk.bzloginmodule.fragments.ResetPasswordFragment;

public class ResetPasswordActivity extends AppCompatActivity {

    private Fragment mCurFragment;
    private LoadingDialog mLoadingDialog;
    private ImageView topImage;
    ImageView backBtn;
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
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(view -> {
            ResetPasswordActivity.this.onBackPressed();
        });

        topImage = findViewById(R.id.imageView3);

        ShowFragment();
        ResetPasswordActivity.this.getSupportFragmentManager().addFragmentOnAttachListener((fragmentManager, fragment) -> {

            Log.d("FragmentOnAttach", "FragmentOnAttach: " + fragment.getClass().toString());

            if(fragment.getClass() == PasswordRecoveryFragment.class){
                topImage.setImageResource(R.drawable.ic_forgot_password);
            } else if(fragment.getClass() == ResetPasswordFragment.class){
                topImage.setImageResource(R.drawable.ic_opt);
            } else if(fragment.getClass() == ResetPasswordCompleteFragment.class){
                topImage.setImageResource(R.drawable.ic_success);
                backBtn.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void ShowFragment() {
        // Begin the transaction
        FragmentTransaction ft = ResetPasswordActivity.this.getSupportFragmentManager().beginTransaction();
        mCurFragment = new PasswordRecoveryFragment();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_container, mCurFragment);
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }

    public void onChangeFragment(){

    }

    public void showLoadingDialog(String title) {
        mLoadingDialog.ShowDialog(title);
    }

    public void hideLoadingDialog() {
        mLoadingDialog.HideDialog();
    }
}