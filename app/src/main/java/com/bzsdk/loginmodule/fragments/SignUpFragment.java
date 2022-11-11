package com.bzsdk.loginmodule.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bzsdk.loginmodule.R;

public class SignUpFragment extends Fragment {

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
    }

    private void SetupView(){
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
    }
}