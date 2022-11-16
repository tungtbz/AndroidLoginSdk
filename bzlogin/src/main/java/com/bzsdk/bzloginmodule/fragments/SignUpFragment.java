package com.bzsdk.bzloginmodule.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.bzsdk.bzloginmodule.LoginActivity;
import com.bzsdk.bzloginmodule.network.NetworkService;
import com.bzsdk.bzloginmodule.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpFragment extends Fragment {


    TextInputLayout mUserNameInputLayout, mPassInputLayout, mConfirmPassInputLayout;

    TextInputEditText mUserNameTextInputEditText;
    TextInputEditText mPasswordTextInputEditText;
    TextInputEditText mConfirmPasswordTextInputEditText;
    Pattern mEmailPattern;

    public SignUpFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        SetupView();
        String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        mEmailPattern = Pattern.compile(emailRegex);

    }

    @Override
    public void onStop() {

        super.onStop();
    }

    private void SetupView() {

        Button signUp = getView().findViewById(R.id.signup_btn);
        signUp.setOnClickListener(view -> {
            CallSignUp();
        });

        TextView termTextView = getView().findViewById(R.id.txt_term);

        String termText = getActivity().getResources().getString(R.string.term_text);
        SpannableString fpSpannableString = new SpannableString(termText);
        ClickableSpan fpClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
                getActivity().startActivity(browserIntent);
            }
        };

        fpSpannableString.setSpan(fpClickableSpan, 31, termText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termTextView.setText(fpSpannableString);
        termTextView.setMovementMethod(LinkMovementMethod.getInstance());
        termTextView.setHighlightColor(Color.TRANSPARENT);

        TextView signinTextView = getView().findViewById(R.id.txt_backto_signin);

        String backtoSignInText = getActivity().getResources().getString(R.string.alreadyhaveaccount_text);
        SpannableString backSignInSpannableString = new SpannableString(backtoSignInText);
        ClickableSpan sgClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //open forget pass fragment
                Toast.makeText(getActivity(), "Back To Sign In", Toast.LENGTH_SHORT).show();
                OpenSignIn();
            }
        };

        backSignInSpannableString.setSpan(sgClickableSpan, 25, backtoSignInText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signinTextView.setText(backSignInSpannableString);
        signinTextView.setMovementMethod(LinkMovementMethod.getInstance());
        signinTextView.setHighlightColor(Color.TRANSPARENT);

        mUserNameInputLayout = getView().findViewById(R.id.editTextTextEmailAddressLayout);

        mPassInputLayout = getView().findViewById(R.id.editTextPasswordLayout);
        mConfirmPassInputLayout = getView().findViewById(R.id.editTextConfirmPasswordLayout);


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
                int length = editable.length();
                if (length == 0) {
                    mUserNameInputLayout.setError("This field cannot be empty");
                    return;
                }

                if(length < 6) {
                    mUserNameInputLayout.setError("The length of 'Username' must be at least 6 characters");
                    return;
                }
                mUserNameInputLayout.setError(null);
            }
        });



        mPasswordTextInputEditText = getView().findViewById(R.id.pass_edittext);
        mPasswordTextInputEditText.addTextChangedListener(new TextWatcher() {
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
                    mPassInputLayout.setError("This field cannot be empty");
                    return;
                }

                if(length < 6) {
                    mPassInputLayout.setError("The length of 'Password' must be at least 6 characters");
                    return;
                }
                mPassInputLayout.setError(null);
            }
        });

        mConfirmPasswordTextInputEditText = getView().findViewById(R.id.confirmpass_edittext);
        mConfirmPasswordTextInputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 0) {
                    mConfirmPassInputLayout.setError("This field cannot be empty");
                    return;
                }
                Editable pass = mPasswordTextInputEditText.getText();
                if (pass.length() > 0) {
                    if (!pass.toString().equals(editable.toString())) {
                        mConfirmPassInputLayout.setError("Password does not match");

                        return;
                    }
                }
                mConfirmPassInputLayout.setError(null);

            }
        });
    }

    private void CallSignUp() {

        String userName = mUserNameTextInputEditText.getText().toString();
        String pass = mPasswordTextInputEditText.getText().toString();

        Matcher mat = mEmailPattern.matcher(userName);

        LoginActivity activity = (LoginActivity)getActivity();
        activity.ShowLoadingDialog("Signup");

        NetworkService.getInstance().SignupByPassword(userName, pass, mat.matches() ? userName : null, new NetworkService.SignUpEvent() {
            @Override
            public void onSuccess() {
                Toast toast =  Toast.makeText(getActivity(), "Sign up successfully", Toast.LENGTH_LONG);
                toast.show();
                OpenSignIn();
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

    private void OpenSignIn() {
        // Begin the transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.fragment_container, new SignInFragment());

        // Complete the changes added above
        transaction.commit();
    }
}