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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bzsdk.loginmodule.R;

public class SignInFragment extends Fragment {
    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        InitViews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    private void InitViews() {
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

    }

    private void OpenSignUp(){
        // Begin the transaction
        FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.fragment_container, new SignUpFragment());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();
    }
}