package com.rofi.referral;

import android.util.Log;
import android.widget.TableRow;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.rofi.core.network.BaseResponse;
import com.rofi.core.network.Constants;

import com.rofi.core.network.JsonObjectRequestExtend;
import com.rofi.core.network.NetworkService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RofiReferralHelper {
    public class RefLinkData {
        public String code;
        public String link;
    }

    public class RefLinkResponse{
        public int code;
        public String message;
        public RefLinkData data;
    }

    public interface RefLinkCallback{
        void onSuccess(String data);
        void onError(String data);
    }

    private static RofiReferralHelper mInstace = null;
    private final String POST_USER_CHECKIN = "/api/referral/v1/users/check-in";
    private final String POST_USER_JOIN_CAMPAIGN = "/api/referral/v1/users/campaigns/%s/join";


    public static RofiReferralHelper getInstance() {
        if (mInstace == null) mInstace = new RofiReferralHelper();
        return mInstace;
    }

    private String TAG = RofiReferralHelper.class.toString();

    private String refCodeCached;

    public void SetRefCodeToCache(String refCode) {
        refCodeCached = refCode;
    }

    public String GetRefCodeFromCache() {
        return refCodeCached;
    }

    public void JoinCampaign(String accessToken, String campaignCode, RefLinkCallback callback) {
        Log.d(TAG, "--> GetRefLink --> accessToken: " + accessToken);
        Log.d(TAG, "--> GetRefLink --> campaignCode: " + campaignCode);

        String apiFormat = String.format(POST_USER_JOIN_CAMPAIGN, campaignCode);
        String url = NetworkService.getInstance().getUrl(apiFormat);
        Log.d(TAG, "--> GetRefLink --> url: " + url);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "GetRefLink: " + response);
                        RefLinkResponse responseData = new Gson().fromJson(response, RefLinkResponse.class);
                        if(responseData.code == 0){
                            Log.d(TAG, "GetRefLink: " + responseData.data.link);
                            Log.d(TAG, "GetRefLink: " + responseData.data.code);
                            callback.onSuccess(response);
                        }else{
                            callback.onError(responseData.message);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error: " + error.getLocalizedMessage());
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

        RequestQueue requestQueue = NetworkService.getInstance().GetRequestQueue();
        requestQueue.add(stringRequest);
    }

    public void RequestCheckInRefCode(String accessToken, String camCode, String gameId, String refCode, NetworkService.BaseCallback callback) {

        Log.d(TAG, "--> RequestCheckInRefCode --> accessToken: " + accessToken);
        Log.d(TAG, "--> RequestCheckInRefCode --> refCode: " + refCode);
        try {
            JSONObject requestData = new JSONObject()
                    .put(Constants.GAME_ID_STR, gameId)
                    .put(Constants.CAMPAIGN_CODE_STR, camCode)
                    .put(Constants.CODE_STR, refCode);
            String url = NetworkService.getInstance().getUrl(POST_USER_CHECKIN);

            JsonObjectRequestExtend request = new JsonObjectRequestExtend(Request.Method.POST, url, requestData, response -> {

                try {
                    int resCode = response.getInt(Constants.HTTP_STATUS_CODE_STR);
                    Log.d(TAG, "RequestCheckInRefCode: " + resCode);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                BaseResponse responseData = new Gson().fromJson(response.toString(), BaseResponse.class);
                if (responseData.code == Constants.API_STATUS_CODE_OK) {
                    callback.onSuccess();
                } else {
                    Log.d(TAG, "--> RequestCheckInRefCode ->>> : " + responseData.message);
                    callback.onError(responseData.message);
                }
            }, error -> {
                Log.d(TAG, "--> RequestCheckInRefCode ->>> " + error.networkResponse.statusCode);
                callback.onError(error.getLocalizedMessage());

            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json; charset=UTF-8");
                    params.put("Authorization", "Bearer " + accessToken);
                    return params;
                }
            };

            RequestQueue requestQueue = NetworkService.getInstance().GetRequestQueue();

            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
