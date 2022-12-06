package com.bzsdk.bzloginmodule;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bzsdk.bzloginmodule.network.LoginResponse;
import com.rofi.core.network.Constants;
import com.rofi.core.network.NetworkService;

import com.google.gson.Gson;

public class LoginService {
    private static LoginService instance;
    private String currentAccount;
    private String currentToken;
    private String googleWebClientId;
    private String refCodeCached;

    private SignInListener signInListener;

    public static LoginService getInstance() {
        if (instance == null) instance = new LoginService();
        return instance;
    }

    public void Init(Context context){
        LoadSavedAccessToken(context);
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

    public void getCurrentUserInfo(String accessToken, NetworkService.GetUserInfoCallback callback) {
        NetworkService.getInstance().GetUserInfo(accessToken, callback);
    }

    public void DispatchOnLogin(String data) {
        if (signInListener != null) signInListener.onSignInSuccess(data);
    }

    public void SaveAccessTokenAfterLogin(Context context, String loginData){
        LoginResponse responseData = new Gson().fromJson(loginData, LoginResponse.class);
        SharedPreferences sharedPref = context.getSharedPreferences(Constants.ACC_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.ACC_KEY, responseData.accessToken);
        editor.apply();

        currentToken = responseData.accessToken;
    }

    private void LoadSavedAccessToken(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(
                Constants.ACC_FILE_NAME, Context.MODE_PRIVATE);
        String defaultValue = "";
        String accessToken = sharedPref.getString(Constants.ACC_KEY, defaultValue);
//        Log.d(LoginService.class.toString(), "LoadSavedAccessToken: " + accessToken);
        setCurrentToken(accessToken);
    }

    public interface SignInListener {
        void onSignInSuccess(String data);
    }
}
