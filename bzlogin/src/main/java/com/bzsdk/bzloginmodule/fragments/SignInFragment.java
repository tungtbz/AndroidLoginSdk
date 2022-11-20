package com.bzsdk.bzloginmodule.fragments;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bzsdk.bzloginmodule.LoginActivity;
import com.bzsdk.bzloginmodule.LoginService;
import com.bzsdk.bzloginmodule.R;
import com.bzsdk.bzloginmodule.ResetPasswordActivity;
import com.bzsdk.bzloginmodule.network.NetworkService;
import com.bzsdk.bzloginmodule.ui.CircleButton;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInResult;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;

public class SignInFragment extends Fragment {
    //    TextInputEditText mUserNameEditText, mPasswordEditText;
    EditText mUserNameEditText, mPasswordEditText;
    private String TAG = "~~~~~~[SignInFragment]";
    private CircleButton mFacebookBtn, mGoogleBtn, mGuestBtn;

    public SignInFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
        InitViews();
        InitFb();
        InitGoogle();
    }

    ActivityResultLauncher<IntentSenderRequest> signinActivityResultLauncher;
    ActivityResultLauncher<IntentSenderRequest> signupActivityResultLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        signinActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        String idToken = credential.getGoogleIdToken();
                        String username = credential.getId();
                        Log.d(TAG, "gg login token: " + idToken);
                        Log.d(TAG, "gg username: " + username);
                        SignInWithGg(credential.getGoogleIdToken());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                });


        signupActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(), result -> {
                    try {
                        SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(result.getData());
                        SignInWithGg(credential.getGoogleIdToken());
                    } catch (ApiException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false);
    }

    private void OpenForgotPassActivity() {
        //Show Login Activity
        Intent myIntent = new Intent(getActivity(), ResetPasswordActivity.class);
        getActivity().startActivity(myIntent);
    }

    private void setButtonScale() {
        float scale = 0.5f;
        mFacebookBtn = getView().findViewById(R.id.facebook_btn);
        mFacebookBtn.setScaleX(scale);
        mFacebookBtn.setScaleY(scale);

        mGoogleBtn = getView().findViewById(R.id.google_btn);
        mGoogleBtn.setScaleX(scale);
        mGoogleBtn.setScaleY(scale);

        mGuestBtn = getView().findViewById(R.id.guest_btn);
        mGuestBtn.setScaleX(scale);
        mGuestBtn.setScaleY(scale);
    }

    private void InitViews() {
        setButtonScale();

        mUserNameEditText = getView().findViewById(R.id.edit_text_username);
        mPasswordEditText = getView().findViewById(R.id.edit_text_password);

        TextView forgotPasswordTextView = getView().findViewById(R.id.forgotpasstxt);
        String forgetPassText = getActivity().getResources().getString(R.string.forgetpas_text);
        SpannableString fpSpannableString = new SpannableString(forgetPassText);
        ClickableSpan fpClickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                //open forget pass fragment
                OpenForgotPassActivity();

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

    private void OpenSignUp() {
        // Begin the transaction
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

        // Replace the contents of the container with the new fragment
        transaction.replace(R.id.fragment_container, new SignUpFragment());
        transaction.addToBackStack(null);

        // Complete the changes added above
        transaction.commit();
    }

    private void callSignIn() {
        String userName = mUserNameEditText.getText().toString();
        String pass = mPasswordEditText.getText().toString();
        if (userName.length() <= 0) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.username_min_length_error), Toast.LENGTH_LONG);
            toast.show();
            return;
        }
        if (pass.length() <= 0) {
            Toast toast = Toast.makeText(getActivity(), getString(R.string.password_min_length_error), Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        LoginActivity activity = (LoginActivity) getActivity();
        activity.showLoadingDialog("Signin");


        NetworkService.getInstance().SigninWithPass(userName, pass, new NetworkService.SignInCallback() {
            @Override
            public void onSuccess(String jsonStr) {
                Log.d(TAG, "onSuccess: " + jsonStr);
                Toast toast = Toast.makeText(getActivity(), "Sign in successfully", Toast.LENGTH_LONG);
                toast.show();
                activity.onLoginSuccess(jsonStr);
            }

            @Override
            public void onError(String message) {
                //show toast
                Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                toast.show();
                activity.hideLoadingDialog();
            }
        });
    }

    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;
    private BeginSignInRequest signUpRequest;
    private boolean showOneTapUI = true;

    private void InitGoogle() {

        Log.d(TAG, "InitGoogle: " + LoginService.getInstance().getGoogleWebClientId());
        showOneTapUI = true;
        oneTapClient = Identity.getSignInClient(getActivity());
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        // Your server's client ID, not your Android client ID.
                        .setServerClientId(LoginService.getInstance().getGoogleWebClientId())
                        // Only show accounts previously used to sign in.
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                // Automatically sign in when exactly one credential is retrieved.
                .setAutoSelectEnabled(true)
                .build();

        signUpRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(LoginService.getInstance().getGoogleWebClientId())
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .build();

        CircleButton ggSignIn = getView().findViewById(R.id.google_btn);

        ggSignIn.setOnClickListener(view -> {
            displayGGSignIn();
        });
    }

    private void displayGGSignIn() {
        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(getActivity(),
                        new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult beginSignInResult) {

                                IntentSender intentSender = beginSignInResult.getPendingIntent().getIntentSender();
                                IntentSenderRequest intentSenderRequest = (new IntentSenderRequest.Builder(intentSender)).build();

                                signinActivityResultLauncher.launch(intentSenderRequest, null);
                            }
                        })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        e.printStackTrace();
                        Log.d(TAG, e.getLocalizedMessage());
                        displayGGSignUp();
                    }
                });
    }

    private void displayGGSignUp() {
        if (!showOneTapUI) return;
        oneTapClient.beginSignIn(signUpRequest)
                .addOnSuccessListener(getActivity(),
                        new OnSuccessListener<BeginSignInResult>() {
                            @Override
                            public void onSuccess(BeginSignInResult beginSignInResult) {

                                IntentSender intentSender = beginSignInResult.getPendingIntent().getIntentSender();
                                IntentSenderRequest intentSenderRequest = (new IntentSenderRequest.Builder(intentSender)).build();
                                signupActivityResultLauncher.launch(intentSenderRequest, null);
                            }
                        })
                .addOnFailureListener(getActivity(), new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // No saved credentials found. Launch the One Tap sign-up flow, or
                        // do nothing and continue presenting the signed-out UI.
                        Log.d(TAG, e.getLocalizedMessage());
                    }
                });
    }

    private void InitFb() {
        CallbackManager callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken().getToken());
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken());

                LoginActivity activity = (LoginActivity) getActivity();
                activity.showLoadingDialog("Signin");

                NetworkService.getInstance().SignInWithFb(loginResult.getAccessToken().getToken(), new NetworkService.SignInCallback() {
                    @Override
                    public void onSuccess(String jsonStr) {
                        Toast toast = Toast.makeText(getActivity(), "Sign in successfully", Toast.LENGTH_LONG);
                        toast.show();
                        activity.onLoginSuccess(jsonStr);
                    }

                    @Override
                    public void onError(String message) {
                        Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                        toast.show();
                        activity.hideLoadingDialog();
                    }
                });

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancelLoginFb: ");
            }

            @Override
            public void onError(@NonNull FacebookException e) {
                Log.e(TAG, "onError: ", e.fillInStackTrace());
            }
        });

        CircleButton fbLogin = getView().findViewById(R.id.facebook_btn);
        fbLogin.setOnClickListener(view -> {
            LoginManager.getInstance().logInWithReadPermissions(this, callbackManager, Arrays.asList("public_profile"));
        });
    }

    private void SignInWithGg(String token) {
        LoginActivity activity = (LoginActivity) getActivity();
        activity.showLoadingDialog("Signin");
        NetworkService.getInstance().SignInWithGG(token, new NetworkService.SignInCallback() {
            @Override
            public void onSuccess(String jsonStr) {
                Toast toast = Toast.makeText(getActivity(), "Sign in successfully", Toast.LENGTH_LONG);
                toast.show();
                activity.onLoginSuccess(jsonStr);
            }

            @Override
            public void onError(String message) {
                Toast toast = Toast.makeText(getActivity(), message, Toast.LENGTH_LONG);
                toast.show();
                activity.hideLoadingDialog();
            }
        });
    }
}