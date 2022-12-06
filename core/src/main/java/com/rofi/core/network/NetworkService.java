package com.rofi.core.network;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class NetworkService {
    private static NetworkService mInstace = null;

    private String TAG = "~~~~~~[NetworkService]";
    private String mBaseUrl;
    private RequestQueue mRequestQueue;
    private boolean mInit = false;

    public static NetworkService getInstance() {
        if (null == mInstace) {
            mInstace = new NetworkService();
        }
        return mInstace;
    }

    public void Init(Activity activity) {
        if (mInit) return;

        mBaseUrl = Constants.BASE_URL;

        Log.d(TAG, "Init BaseUrl: " + mBaseUrl);
        CreateRequestQueue(activity.getCacheDir());

        mInit = true;
    }

    public void SetDebug(boolean isDebug){
        mBaseUrl = isDebug ? Constants.BASE_URL_DEBUG : Constants.BASE_URL;
    }

    public void GetUserInfo(String accessToken , @NonNull GetUserInfoCallback callback){
        if(accessToken == null) {
            callback.onError("accessToken is null");
            return;
        }
        String url = mBaseUrl + ApiPaths.GET_USER_INFO;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Log.d(TAG, "GetUserInfo: " + response);
                        callback.onSuccess(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Log.d(TAG, "error: " + error.getLocalizedMessage());
                        callback.onError(error.getLocalizedMessage());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
                params.put("Authorization", "Bearer " + accessToken);
                return params;
            }
        };

        mRequestQueue.add(stringRequest);
    }

    public void SignupByPassword(String userName, String password,
                                 @Nullable String email,
                                 @NonNull SignUpCallback signUpEventListener) {
        String url = mBaseUrl + ApiPaths.POST_SIGNUP_BY_PASS;
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

    public void SigninWithPass(String userName, String pass, @NonNull SignInCallback signinEvent) {
        String url = mBaseUrl + ApiPaths.POST_LOGIN_BY_PASS;
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
//                            LoginResponse responseData = new Gson().fromJson(response.toString(), LoginResponse.class);
//                            signinEvent.onError(responseData.message);
                            signinEvent.onError("SignIn Failed!");
                        }
                    },
                    error -> {
                        Log.d(TAG, "--> SignupByPassword --> error: " + error.networkResponse.statusCode);
                        signinEvent.onError("SignIn Failed!");
//                        try {
//                            String jsonString =
//                                    new String(
//                                            error.networkResponse.data,
//                                            HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));
//
//                            JSONArray jsonArray = new JSONArray(jsonString);
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//                            String message = jsonObject.getString(Constants.MESSAGE_STR);
//
//                            signinEvent.onError(message);
//
//                        } catch (UnsupportedEncodingException | JSONException e) {
//                            e.printStackTrace();
//                        }
                    });

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SignInWithGG(String idToken, SignInCallback signinEvent) {
        String url = mBaseUrl + ApiPaths.POST_LOGIN_BY_GOOGLE;
        Log.d(TAG, "--> SignInWithGG --> URL: " + url);
        Log.d(TAG, "--> SignInWithGG --> token: " + idToken);

        try {
            JSONObject requestData = new JSONObject()
                    .put(Constants.CODE_TOKEN_STR, idToken);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData,
                    response -> {
                        if (response.has(Constants.ACCESS_TOKEN_STR)) {
                            signinEvent.onSuccess(response.toString());
                        } else {
//                            LoginResponse responseData = new Gson().fromJson(response.toString(), LoginResponse.class);
//                            signinEvent.onError(responseData.message);

                            signinEvent.onError("SignIn Failed!");
                        }
                    },
                    error -> {
                        Log.d(TAG, "--> SignupByPassword --> error: " + error.networkResponse.statusCode);
                        signinEvent.onError("SignIn Failed!");
//                        try {
//                            String jsonString =
//                                    new String(
//                                            error.networkResponse.data,
//                                            HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));
//
//                            JSONArray jsonArray = new JSONArray(jsonString);
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//                            String message = jsonObject.getString(Constants.MESSAGE_STR);
//
//                            signinEvent.onError(message);
//
//                        } catch (UnsupportedEncodingException | JSONException e) {
//                            e.printStackTrace();
//                        }
                    });

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SignInWithFb(String fbToken, SignInCallback signInCallback) {
        String url = mBaseUrl + ApiPaths.POST_LOGIN_BY_FACEBOOK;
        Log.d(TAG, "--> SignInWithFb --> URL: " + url);
        Log.d(TAG, "--> SignInWithFb --> fbToken: " + fbToken);

        try {
            JSONObject requestData = new JSONObject()
                    .put(Constants.CODE_TOKEN_STR, fbToken);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData,
                    response -> {
                        if (response.has(Constants.ACCESS_TOKEN_STR)) {
                            signInCallback.onSuccess(response.toString());
                        } else {
//                            LoginResponse responseData = new Gson().fromJson(response.toString(), LoginResponse.class);
                            signInCallback.onError("SignIn Failed!");
                        }
                    },
                    error -> {
                        Log.d(TAG, "--> SignupByPassword --> error: " + error.networkResponse.statusCode);
                        signInCallback.onError("SignIn Failed!");

//                        try {
//                            String jsonString =
//                                    new String(
//                                            error.networkResponse.data,
//                                            HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));
//
//                            JSONArray jsonArray = new JSONArray(jsonString);
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//                            String message = jsonObject.getString(Constants.MESSAGE_STR);
//
//                            signInCallback.onError(message);
//
//                        } catch (UnsupportedEncodingException | JSONException e) {
//                            e.printStackTrace();
//                        }
                    });

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SignInByGuest(String uid, SignInCallback signInCallback){
        String url = mBaseUrl + ApiPaths.POST_LOGIN_BY_GUEST;

        try {
            JSONObject requestData = new JSONObject()
                    .put(Constants.GUEST_ID_STR, uid);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData,
                    response -> {
                        if (response.has(Constants.ACCESS_TOKEN_STR)) {
                            signInCallback.onSuccess(response.toString());
                        } else {
//                            LoginResponse responseData = new Gson().fromJson(response.toString(), LoginResponse.class);
                            signInCallback.onError("SignIn Failed!");
                        }
                    },
                    error -> {
                        Log.d(TAG, "--> SignupByPassword --> error: " + error.networkResponse.statusCode);
                        signInCallback.onError("SignIn Failed!");
                    });

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void SendOpt_ResetPassword(String email, BaseCallback sendOtpCallback) {
        String url = mBaseUrl + ApiPaths.POST_RECOVERY_PASSWORD_REQUEST_OPT;

        try {
            JSONObject requestData = new JSONObject()
                    .put(Constants.ACCOUNT_STR, email);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData,
                    response -> {
                        BaseResponse responseData = new Gson().fromJson(response.toString(), BaseResponse.class);
                        if (responseData.code == Constants.API_STATUS_CODE_OK) {
                            //back to login scene
                            sendOtpCallback.onSuccess();
                        } else {
                            Log.d(TAG, "--> SendOpt_ResetPassword ->>> : " + responseData.message);
                            sendOtpCallback.onError(responseData.message);
                        }
                    },
                    error -> {
                        Log.d(TAG, "--> SendOpt_ResetPassword --> error: " + error.networkResponse.statusCode);
                        sendOtpCallback.onError("Request Opt Error!");

//                        try {
//                            String jsonString =
//                                    new String(
//                                            error.networkResponse.data,
//                                            HttpHeaderParser.parseCharset(error.networkResponse.headers, "utf-8"));
//
//                            JSONArray jsonArray = new JSONArray(jsonString);
//                            JSONObject jsonObject = jsonArray.getJSONObject(0);
//                            String message = jsonObject.getString(Constants.MESSAGE_STR);
//
//                            sendOtpCallback.onError(message);
//
//                        } catch (UnsupportedEncodingException | JSONException e) {
//                            e.printStackTrace();
//                        }
                    });

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ValidateOpt_ResetPassword(String account, String otp, String newPassword, BaseCallback sendOtpCallback) {
        String url = mBaseUrl + ApiPaths.POST_RECOVERY_PASSWORD_VALIDATE_OPT;
        Log.d(TAG, "--> ValidateOpt_ResetPassword --> URL: " + url);
        Log.d(TAG, "--> ValidateOpt_ResetPassword --> otp: " + otp);
        try {
            JSONObject requestData = new JSONObject()
                    .put(Constants.ACCOUNT_STR, account)
                    .put(Constants.NEWPASSWORD_STR, newPassword)
                    .put(Constants.OTP_STR, otp);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData,
                    response -> {
                        BaseResponse responseData = new Gson().fromJson(response.toString(), BaseResponse.class);
                        if (responseData.code == Constants.API_STATUS_CODE_OK) {
                            //back to login scene
                            sendOtpCallback.onSuccess();
                        } else {
                            Log.d(TAG, "--> ValidateOpt_ResetPassword ->>> : " + responseData.message);
                            sendOtpCallback.onError(responseData.message);
                        }
                    },
                    error -> {
                        Log.d(TAG, "--> ValidateOpt_ResetPassword --> error: " + error.networkResponse.statusCode);
                        sendOtpCallback.onError("ValidateOpt Error!");
                    });

            mRequestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public RequestQueue GetRequestQueue(){
        return mRequestQueue;
    }

    public String getUrl(String apiPath){
        return mBaseUrl + apiPath;
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

    public interface BaseCallback {
        void onSuccess();
        void onError(String message);
    }


    public interface SignUpCallback extends BaseCallback {
    }

    public interface SignInCallback {
        void onSuccess(String jsonStr);
        void onError(String message);
    }

    public interface RequestOtpEvent extends BaseCallback {
    }

    public interface GetUserInfoCallback{
        void onSuccess(String jsonStr);
        void onError(String message);
    }
}
