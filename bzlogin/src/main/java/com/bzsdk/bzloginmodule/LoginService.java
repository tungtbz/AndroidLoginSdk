package com.bzsdk.bzloginmodule;

public class LoginService {
    private static LoginService instance;
    private String currentAccount;
    private String currentToken;
    private String googleWebClientId;

    private SignInListener signInListener;

    public static LoginService getInstance() {
        if (instance == null) instance = new LoginService();
        return instance;
    }

    public void setGoogleWebClientId(String id) {
        googleWebClientId = id;
    }

    public String getGoogleWebClientId() {
        return googleWebClientId;
    }

    public void setCurrentAccount(String account) {
        currentAccount = account;
    }

    public void setCurrentToken(String token) {
        currentToken = token;
    }

    public String getCurrentAccount() {
        return currentAccount;
    }

    public String getCurrentToken() {
        return currentToken;
    }

    public void setSignInListener(SignInListener listener) {
        signInListener = listener;
    }


    public void DispatchOnLogin(String data) {
        if (signInListener != null) signInListener.onSignInSuccess(data);
    }

    public interface SignInListener {
        void onSignInSuccess(String data);
    }
}
