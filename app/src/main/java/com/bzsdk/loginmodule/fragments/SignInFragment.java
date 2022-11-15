package com.bzsdk.loginmodule.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Selection;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bzsdk.loginmodule.LoginActivity;
import com.bzsdk.loginmodule.R;
import com.bzsdk.loginmodule.network.NetworkService;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.material.textfield.TextInputEditText;

public class SignInFragment extends Fragment {
    TextInputEditText mUserNameEditText, mPasswordEditText;
    private String TAG = "~~~~~~[SignInFragment]";
    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        InitViews();
        InitFb();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    private void InitViews() {
        mUserNameEditText = getView().findViewById(R.id.username_edittext);
        mPasswordEditText = getView().findViewById(R.id.password_edittext);

        TextView forgotPasswordTextView = getView().findViewById(R.id.forgotpasstxt);
        String forgetPassText = getActivity().getResources().getString(R.string.forgetpas_text);
        SpannableString fpSpannableString = new SpannableString(forgetPassText);
        ClickableSpan fpClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //open forget pass fragment
                Toast.makeText(getActivity(), "Open Forget Pass Scene", Toast.LENGTH_SHORT).show();
            }
        };
        fpSpannableString.setSpan(fpClickableSpan, 0, forgetPassText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotPasswordTextView.setText(fpSpannableString);
        forgotPasswordTextView.setMovementMethod(LinkMovementMethod.getInstance());
        forgotPasswordTextView.setHighlightColor(Color.TRANSPARENT);


        TextView signupTextView = getView().findViewById(R.id.open_signup);
        String signupText = getActivity().getResources().getString(R.string.donthaveaccount_text);
        SpannableString signupSpannableString = new SpannableString(signupText);

        ClickableSpan signupClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //open forget pass fragment
                Toast.makeText(getActivity(), "Open Signup", Toast.LENGTH_SHORT).show();
                OpenSignUp();

            }
        };
        signupSpannableString.setSpan(signupClickableSpan, 22, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        signupTextView.setText(signupSpannableString);
        signupTextView.setMovementMethod(LinkMovementMethod.getInstance());
        signupTextView.setHighlightColor(Color.TRANSPARENT);

        Button signinBtn = getView().findViewById(R.id.signin_btn);
        signinBtn.setOnClickListener(view -> {
            callSignIn();
        });
    }

    private void OpenSignUp(){
        // Begin the transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.fragment_container, new SignUpFragment());
        transaction.addToBackStack(null);

        // Complete the changes added above
        transaction.commit();
    }

    private void callSignIn(){
        String userName = mUserNameEditText.getText().toString();
        String pass = mPasswordEditText.getText().toString();

        LoginActivity activity = (LoginActivity)getActivity();
        activity.ShowLoadingDialog("Signin");

        NetworkService.getInstance().SigninWithPass(userName, pass, new NetworkService.SigninEvent() {
            @Override
            public void onSuccess(String jsonStr) {
                Toast toast =  Toast.makeText(getActivity(), "Sign in successfully", Toast.LENGTH_LONG);
                toast.show();
                activity.HideLoadingDialog();
            }

            @Override
            public void onError(String message) {
                //show toast
                Toast toast =  Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                toast.show();
                activity.HideLoadingDialog();
            }
        });
    }

    private void InitFb() {
        CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) getView().findViewById(R.id.login_button);
        loginButton.setFragment(this);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken().getToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(@NonNull FacebookException e) {

            }
        });
    }
}