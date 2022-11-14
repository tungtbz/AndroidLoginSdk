package com.bzsdk.loginmodule.network;

import static com.bzsdk.loginmodule.network.Constants.API_STATUS_CODE;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.bzsdk.loginmodule.BZURL;
import com.bzsdk.loginmodule.BaseResponse;
import com.bzsdk.loginmodule.R;
import com.bzsdk.loginmodule.events.SignUpAccountSuccessEvent;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

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

        mBaseUrl = activity.getResources().getString(R.string.user_url);
        Log.d(TAG, "Init BaseUrl: " + mBaseUrl);
        CreateRequestQueue(activity.getCacheDir());

        mInit = true;
    }

    public void SignupByPassword(String userName, String password, @Nullable String email) {
        String url = mBaseUrl + BZURL.POST_SIGNUP_BY_PASS;
        Log.d(TAG, "--> SignupByPassword --> BZURL: " + url);
        Log.d(TAG, "--> SignupByPassword --> userName: " + userName);
        Log.d(TAG, "--> SignupByPassword --> password: " + password);
        try {
            JSONObject requestData = new JSONObject()
                    .put("username", userName)
                    .put("password", password);
            if (email != null) requestData = requestData.put("email", email);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData,
                    response -> {
                        int httpStatusCode = -1;
                        Log.d(TAG, "--> SignupByPassword --> response: " + response.toString());
                        try {
                            httpStatusCode = response.getInt(Constants.HTTP_STATUS_CODE_STR);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        BaseResponse responseData = new Gson().fromJson(response.toString(), BaseResponse.class);
                        if (responseData.code == Constants.API_STATUS_CODE_OK) {
                            //back to login scene
                            EventBus.getDefault().post(new SignUpAccountSuccessEvent());
                        }
                    },
                    error -> {
                        Log.d(TAG, "--> SignupByPassword --> error: " + error.toString());
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

}
