package com.bzsdk.bzloginmodule.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

//import com.bzsdk.bzloginmodule.BuildConfig;
import com.bzsdk.bzloginmodule.LoginService;
import com.bzsdk.bzloginmodule.R;
import com.bzsdk.bzloginmodule.ResetPasswordActivity;
import com.bzsdk.bzloginmodule.network.NetworkService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class VerifyOtpAndResetPasswordFragment extends Fragment {
    TextInputLayout mOtpInputLayout, mPasswordLayout, mConfirmPasswordLayout;
    TextInputEditText mOtpEditText, mPasswordEditText, mPassConfirmEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reset_password, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        InitView();
    }

    private void InitView() {

        Button resetpassBtn =  getView().findViewById(R.id.reset_pass_btn);
        resetpassBtn.setOnClickListener(view -> {

//            if (BuildConfig.BUILD_TYPE == "debug") {
//                openResetPassSuccessScene();
//                return;
//            }

            String currentAccount = LoginService.getInstance().getCurrentAccount();
            String otp = mOtpEditText.getText().toString();
            String pass = mPassConfirmEditText.getText().toString();

            ResetPasswordActivity activity = (ResetPasswordActivity) getActivity();
            activity.showLoadingDialog(getString(R.string.loading_text));

            NetworkService.getInstance().ValidateOpt_ResetPassword(currentAccount, otp, pass, new NetworkService.BaseCallback() {
                @Override
                public void onSuccess() {
                    activity.hideLoadingDialog();
                    //open last scene
                    openResetPassSuccessScene();
                }

                @Override
                public void onError(String message) {
                    Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                    toast.show();
                    activity.hideLoadingDialog();
                }
            });
        });

        mOtpInputLayout = getView().findViewById(R.id.otp_text_layout);
        mPasswordLayout = getView().findViewById(R.id.editTextPasswordLayout);
        mConfirmPasswordLayout = getView().findViewById(R.id.editTextConfirmPasswordLayout);

        mOtpEditText = getView().findViewById(R.id.otp_edittext);

        mPasswordEditText = getView().findViewById(R.id.pass_edittext);
        mPasswordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int length = editable.length();
                if (length == 0) {
                    mPasswordLayout.setError(getString(R.string.edittext_not_empty_error));
                    return;
                }

                if (length < 6) {
                    mPasswordLayout.setError(getString(R.string.password_min_length_error));
                    return;
                }
                mPasswordLayout.setError(null);
            }
        });


        mPassConfirmEditText = getView().findViewById(R.id.confirmpass_edittext);
        mPassConfirmEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mConfirmPasswordLayout.setError(getString(R.string.edittext_not_empty_error));
                    return;
                }
                Editable pass = mPasswordEditText.getText();
                if (pass.length() > 0) {
                    if (!pass.toString().equals(editable.toString())) {
                        mConfirmPasswordLayout.setError(getString(R.string.password_not_match_error));

                        return;
                    }
                }
                mConfirmPasswordLayout.setError(null);

            }
        });
    }

    private void openResetPassSuccessScene(){
        // Begin the transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.fragment_container, new ResetPasswordCompleteFragment());
        // Complete the changes added above
        transaction.commit();
    }
}