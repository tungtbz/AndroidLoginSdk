package com.bzsdk.bzloginmodule.fragments;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bzsdk.bzloginmodule.LoginService;
import com.bzsdk.bzloginmodule.R;
import com.bzsdk.bzloginmodule.ResetPasswordActivity;
import com.bzsdk.bzloginmodule.network.NetworkService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PasswordRecoveryFragment extends Fragment {
    TextInputLayout mUserNameInputLayout;
    TextInputEditText mUserNameTextInputEditText;

    Button sendOtpBtn;
    Pattern mEmailPattern;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_recovery, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        mEmailPattern = Pattern.compile(emailRegex);
        InitView();
    }

    private void InitView() {
        mUserNameInputLayout = getView().findViewById(R.id.editTextTextEmailAddress);

        mUserNameTextInputEditText = getView().findViewById(R.id.otp_edittext);
        mUserNameTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int textLength = editable.length();
                if (textLength <= 0) {
                    mUserNameInputLayout.setError(getString(R.string.edittext_not_empty_error));
                    sendOtpBtn.setClickable(false);
                    return;
                }

                Matcher mat = mEmailPattern.matcher(editable);
                if (!mat.matches()) {
                    mUserNameInputLayout.setError(getString(R.string.email_format_error));
                    sendOtpBtn.setClickable(false);
                    return;
                }

                mUserNameInputLayout.setError(null);
                sendOtpBtn.setClickable(true);
            }
        });

        sendOtpBtn = getView().findViewById(R.id.send_opt_btn);
        sendOtpBtn.setOnClickListener(view -> {

            ResetPasswordActivity activity = (ResetPasswordActivity) getActivity();
            activity.showLoadingDialog(getString(R.string.loading_text));

            Editable email = mUserNameTextInputEditText.getText();

            if(email.length() == 0){
                mUserNameInputLayout.setError(getString(R.string.edittext_not_empty_error));
                return;
            }

            NetworkService.getInstance().SendOpt_ResetPassword(email.toString(), new NetworkService.BaseCallback() {
                @Override
                public void onSuccess() {
                    LoginService.getInstance().setCurrentAccount(email.toString());
                    activity.hideLoadingDialog();
                    OpenResetPasswordScene();
                }

                @Override
                public void onError(String message) {
                    Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                    toast.show();
                    activity.hideLoadingDialog();
                }
            });
        });
    }

    private void OpenResetPasswordScene() {
        // Begin the transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.fragment_container, new ResetPasswordFragment());
        transaction.addToBackStack(null);
        // Complete the changes added above
        transaction.commit();
    }
}