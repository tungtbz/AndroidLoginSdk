package com.bzsdk.loginmodule.network;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.bzsdk.loginmodule.R;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class NetworkService {
    private static NetworkService mInstace = null;

    private String TAG = "~~~~~~[NetworkService]";
    private String mBaseUrl;
    private RequestQueue mRequestQueue;
    private boolean mInit = false;

//    private SignUpEvent mSignUpEvent;
//    private SigninEvent mSigninEvent;

    public static NetworkService getInstance() {
        if (null == mInstace) {
            mInstace = new NetworkService();
        }
        return mInstace;
    }

    public void Init(Activity activity) {
        if (mInit) return;

        mBaseUrl = activity.getResources().getString(R.string.user_url);
        Log.d(TAG, "Init BaseUrl: " + mBaseUrl);
        CreateRequestQueue(activity.getCacheDir());

        mInit = true;
    }

    public void SignupByPassword(String userName, String password,
                                 @Nullable String email,
                                 @NonNull SignUpEvent signUpEventListener) {
        String url = mBaseUrl + BZURL.POST_SIGNUP_BY_PASS;
        Log.d(TAG, "--> SignupByPassword --> BZURL: " + url);
        Log.d(TAG, "--> SignupByPassword --> userName: " + userName);
        Log.d(TAG, "--> SignupByPassword --> password: " + password);

//        mSignUpEvent = signUpEventListener;

        try {
            JSONObject requestData = new JSONObject()
                    .put(Constants.USERNAME_STR, userName)
                    .put(Constants.PASSWORD_STR, password);
            if (email != null) requestData = requestData.put(Constants.EMAIL_STR, email);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData,
                    response -> {
                        BaseResponse responseData = new Gson().fromJson(response.toString(), BaseResponse.class);
                        if (responseData.code == Constants.API_STATUS_CODE_OK) {
                            //back to login scene
                            signUpEventListener.onSuccess();
                        } else {
                            Log.d(TAG, "--> SignupByPassword ->>> : " + responseData.message);
                            signUpEventListener.onError(responseData.message);
                        }
                    },
                    error -> {
                        try {
                            String jsonString =
                                    new String(
                                            error.networkResponse.data,
                                            HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));

                            JSONArray jsonArray = new JSONArray(jsonString);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String message = jsonObject.getString(Constants.MESSAGE_STR);
                            signUpEventListener.onError(message);
                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    });

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SigninWithPass(String userName, String pass, @NonNull SigninEvent signinEvent) {
        String url = mBaseUrl + BZURL.POST_LOGIN_BY_PASS;
//        mSigninEvent = signinEvent;
        Log.d(TAG, "--> SigninWithPass --> BZURL: " + url);
        Log.d(TAG, "--> SigninWithPass --> userName: " + userName);
        Log.d(TAG, "--> SigninWithPass --> password: " + pass);
        try {
            JSONObject requestData = new JSONObject()
                    .put(Constants.ACCOUNT_STR, userName)
                    .put(Constants.PASSWORD_STR, pass);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData,
                    response -> {
                        if (response.has(Constants.ACCESS_TOKEN_STR)) {
                            signinEvent.onSuccess(response.toString());
                        } else {
                            LoginResponse responseData = new Gson().fromJson(response.toString(), LoginResponse.class);
                            if (responseData.code == Constants.API_STATUS_CODE_USER_NOT_EXIT) {
                                signinEvent.onError("Username or password is incorrect. Please try again.");
                            }
                        }
                    },
                    error -> {
                        Log.d(TAG, "--> SignupByPassword --> error: " + error.networkResponse.statusCode);
                        try {
                            String jsonString =
                                    new String(
                                            error.networkResponse.data,
                                            HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));

                            JSONArray jsonArray = new JSONArray(jsonString);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            String message = jsonObject.getString(Constants.MESSAGE_STR);

                            signinEvent.onError(message);

                        } catch (UnsupportedEncodingException | JSONException e) {
                            e.printStackTrace();
                        }
                    });

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void CreateRequestQueue(File rootDirectory) {
        if (mRequestQueue == null) {
            Cache cache = new DiskBasedCache(rootDirectory, 1024 * 1024);
            // Set up the network to use HttpURLConnection as the HTTP client.
            Network network = new BasicNetwork(new HurlStack());

            mRequestQueue = new RequestQueue(cache, network);

            mRequestQueue.start();
        }
    }

    public interface SignUpEvent {
        void onSuccess();

        void onError(String message);
    }

    public interface SigninEvent {
        void onSuccess(String jsonStr);

        void onError(String message);
    }
}
