package com.tecace.networkmanagerta;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tecace.loggerta.LogTA;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class NetworkManagerTA implements Serializable {

    private static final String TAG = "NetworkManagerTA";
    private static RequestQueue mRequestQueue;
    private String mScheme = Scheme.HTTP; // default to HTTP
    private String mBaseUrl = "";
    private String mPortNo = "";
    private String mEndpoint = "";
    private String mUrl = null;


    private static NetworkManagerTA mNetworkManagerTAInstance;
    private NetworkManagerTA() {
        if (mNetworkManagerTAInstance != null) {
            LogTA.e("Use getInstance() method!");
        }
    }
    public static NetworkManagerTA getInstance() {
        if (mNetworkManagerTAInstance == null) {
            mNetworkManagerTAInstance = new NetworkManagerTA();
        }
        return mNetworkManagerTAInstance;
    }

    public NetworkManagerTA setScheme(String scheme) {
        mScheme = scheme;
        return mNetworkManagerTAInstance;
    }

    public NetworkManagerTA setBaseUrl(String baseUrl) {
        mBaseUrl = baseUrl;
        return mNetworkManagerTAInstance;
    }

    public NetworkManagerTA setPortNo(int portNo) {
        mPortNo = ":" + portNo;
        return mNetworkManagerTAInstance;
    }

    public NetworkManagerTA setEndpoint(String endpoint) {
        mEndpoint = endpoint;
        return mNetworkManagerTAInstance;
    }

    public NetworkManagerTA setUrl(String url) {
        mUrl = url;
        return mNetworkManagerTAInstance;
    }

    public void addToRequestQueue(JSONObject reqBody, Response.Listener onSuccess, Response.ErrorListener onFailure, Context context) {
        String url = mUrl == null ? mScheme + mBaseUrl + mPortNo + mEndpoint : mUrl;
        JsonObjectRequest request = new JsonObjectRequest(url, reqBody, onSuccess, onFailure);
        sendRequest(request, context);
    }

    public void addToRequestQueue(int reqMethod, JSONObject reqBody, Response.Listener onSuccess, Response.ErrorListener onFailure, Context context) {
        String url = mUrl == null ? mScheme + mBaseUrl + mPortNo + mEndpoint : mUrl;
        JsonObjectRequest request = new JsonObjectRequest(reqMethod, url, reqBody, onSuccess, onFailure);
        sendRequest(request, context);
    }

    public void addToRequestQueue(JSONObject reqBody, HashMap reqHeaders, Response.Listener onSuccess, Response.ErrorListener onFailure, Context context) {
        String url = mUrl == null ? mScheme + mBaseUrl + mPortNo + mEndpoint : mUrl;

        JsonObjectRequest request = new JsonObjectRequest(url, reqBody, onSuccess, onFailure){
            @Override
            public Map<String, String> getHeaders() {
                return reqHeaders;
            }
        };

        sendRequest(request, context);
    }

    public void addToRequestQueue(int reqMethod, JSONObject reqBody, HashMap reqHeaders, Response.Listener onSuccess, Response.ErrorListener onFailure, Context context) {
        String url = mUrl == null ? mScheme + mBaseUrl + mPortNo + mEndpoint : mUrl;

        JsonObjectRequest request = new JsonObjectRequest(reqMethod, url, reqBody, onSuccess, onFailure){
            @Override
            public Map<String, String> getHeaders() {
                return reqHeaders;
            }
        };

        sendRequest(request, context);
    }

    private <T> void sendRequest(Request<T> request, Context context) {
        request.setTag(TAG);
        getRequestQueue(context).add(request);
        // due to Singleton implementation, need this so that the next request won't have values from previous request
        resetUrlParams();
    }

    public void cancelPendingRequests() {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(TAG);
        }
    }

    private RequestQueue getRequestQueue(Context context) {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    private void resetUrlParams() {
        mBaseUrl = "";
        mPortNo = "";
        mEndpoint = "";
        mUrl = null;
    }

//    protected Object readResolve() {
//        return NetworkManagerTA.getInstance();
//    }
}
