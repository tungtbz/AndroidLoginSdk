package com.bzsdk.loginmodule.fragments;

import android.graphics.Color;
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

import com.bzsdk.loginmodule.network.NetworkService;
import com.bzsdk.loginmodule.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN_ORDERED)
    public void OnSigUpDone(){
        OpenSignIn();
    }


    private void SetupView() {

        Button signUp = getView().findViewById(R.id.signup_btn);
        signUp.setOnClickListener(view -> {
            CallSignUp();
        });

        TextView forgotPasswordTextView = getView().findViewById(R.id.txt_term);

        String forgetPassText = getActivity().getResources().getString(R.string.term_text);
        SpannableString fpSpannableString = new SpannableString(forgetPassText);
        ClickableSpan fpClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //open forget pass fragment
                Toast.makeText(getActivity(), "Open Term And Service Page", Toast.LENGTH_SHORT).show();
            }
        };
        fpSpannableString.setSpan(fpClickableSpan, 0, forgetPassText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        forgotPasswordTextView.setText(fpSpannableString);
        forgotPasswordTextView.setMovementMethod(LinkMovementMethod.getInstance());
        forgotPasswordTextView.setHighlightColor(Color.TRANSPARENT);

        TextView signinTextView = getView().findViewById(R.id.txt_backto_signin);

        String backtoSignInText = getActivity().getResources().getString(R.string.alreadyhaveaccount_text);
        SpannableString backSignInSpannableString = new SpannableString(backtoSignInText);
        ClickableSpan sgClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //open forget pass fragment
                Toast.makeText(getActivity(), "Back To Sign In", Toast.LENGTH_SHORT).show();
            }
        };

        backSignInSpannableString.setSpan(sgClickableSpan, 25, backtoSignInText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        signinTextView.setText(backSignInSpannableString);
        signinTextView.setMovementMethod(LinkMovementMethod.getInstance());
        signinTextView.setHighlightColor(Color.TRANSPARENT);

        mUserNameInputLayout = getView().findViewById(R.id.editTextTextEmailAddressLayout);
        mPassInputLayout = getView().findViewById(R.id.editTextPasswordLayout);
        mConfirmPassInputLayout = getView().findViewById(R.id.editTextConfirmPasswordLayout);


        mUserNameTextInputEditText = getView().findViewById(R.id.username_edittext);
        mPasswordTextInputEditText = getView().findViewById(R.id.pass_edittext);
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
        NetworkService.getInstance().SignupByPassword(userName,pass, mat.matches() ? userName : null);
    }

    private void OpenSignIn(){
        // Begin the transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.fragment_container, new SignInFragment());

        // Complete the changes added above
        transaction.commit();
    }
}