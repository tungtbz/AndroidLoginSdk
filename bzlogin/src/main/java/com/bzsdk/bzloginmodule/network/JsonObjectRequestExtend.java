package com.bzsdk.bzloginmodule.network;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;

import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JsonObjectRequestExtend extends JsonRequest<JSONObject> {


    public JsonObjectRequestExtend(
            String url, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(Method.GET, url, null, listener, errorListener);
    }


    /**
     * Creates a new request.
     *
     * @param method        the HTTP method to use
     * @param url           URL to fetch the JSON from
     * @param jsonRequest   A {@link JSONObject} to post with the request. Null indicates no
     *                      parameters will be posted along with request.
     * @param listener      Listener to receive the JSON response
     * @param errorListener Error listener, or null to ignore errors.
     */
    public JsonObjectRequestExtend(
            int method,
            String url,
            @Nullable JSONObject jsonRequest,
            Response.Listener<JSONObject> listener,
            @Nullable Response.ErrorListener errorListener) {
        super(
                method,
                url,
                jsonRequest != null ? jsonRequest.toString() : null,
                listener,
                errorListener);
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {

        try {

            String jsonString =
                    new String(
                            response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));

            JSONObject jsonObject = new JSONObject(jsonString);
            return Response.success(jsonObject, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
