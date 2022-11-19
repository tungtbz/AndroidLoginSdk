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
import android.widget.EditText;
import android.widget.Toast;

//import com.bzsdk.bzloginmodule.BuildConfig;
import com.bzsdk.bzloginmodule.LoginService;
import com.bzsdk.bzloginmodule.R;
import com.bzsdk.bzloginmodule.ResetPasswordActivity;
import com.bzsdk.bzloginmodule.network.NetworkService;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class VerifyOtpAndResetPasswordFragment extends Fragment {
    EditText mOtpEditText, mPasswordEditText, mPassConfirmEditText;

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
            String otp = mOtpEditText.getText().toString();
            String pass = mPasswordEditText.getText().toString();
            String passConfirm = mPassConfirmEditText.getText().toString();

            if(otp.length() != 6){
                Toast toast = Toast.makeText(getActivity(), getString(R.string.otp_not_empty_error), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if(pass.length() == 0 || pass.length() < 6){
                Toast toast = Toast.makeText(getActivity(), getString(R.string.password_min_length_error), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            if(!passConfirm.equals(pass)){
                Toast toast = Toast.makeText(getActivity(), getString(R.string.password_not_match_error), Toast.LENGTH_SHORT);
                toast.show();
                return;
            }

            String currentAccount = LoginService.getInstance().getCurrentAccount();
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

        mOtpEditText = getView().findViewById(R.id.otp_edittext);
        mPasswordEditText = getView().findViewById(R.id.edit_text_password);
        mPassConfirmEditText = getView().findViewById(R.id.edit_text_confirm_password);

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