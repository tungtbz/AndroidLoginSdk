package com.bzsdk.bzloginmodule.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bzsdk.bzloginmodule.LoginService;
import com.bzsdk.bzloginmodule.R;
import com.bzsdk.bzloginmodule.ResetPasswordActivity;
import com.rofi.core.network.NetworkService;

import java.util.regex.Pattern;

public class SendPasswordRecoveryRequestFragment extends Fragment {

    EditText mUserNameTextInputEditText;

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

        mUserNameTextInputEditText = getView().findViewById(R.id.edit_text_username);

        sendOtpBtn = getView().findViewById(R.id.send_opt_btn);
        sendOtpBtn.setOnClickListener(view -> {

            ResetPasswordActivity activity = (ResetPasswordActivity) getActivity();
            activity.showLoadingDialog(getString(R.string.loading_text));

            Editable email = mUserNameTextInputEditText.getText();

            if(email.length() == 0){
                Toast toast = Toast.makeText(getActivity(), getString(R.string.edittext_not_empty_error), Toast.LENGTH_LONG);
                toast.show();
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
        transaction.replace(R.id.fragment_container, new VerifyOtpAndResetPasswordFragment());
        // Complete the changes added above
        transaction.commit();
    }
}