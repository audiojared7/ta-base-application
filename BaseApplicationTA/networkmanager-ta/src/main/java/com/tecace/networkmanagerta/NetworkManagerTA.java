package com.tecace.networkmanagerta;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tecace.loggerta.LogTA;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import static com.tecace.networkmanagerta.Scheme.HTTP;

public class NetworkManagerTA implements Serializable {

    private static final String TAG = "NetworkManagerTA";
    private static RequestQueue mRequestQueue;

    private String mScheme = HTTP; // default to HTTP
    private String mBaseUrl = "";
    private int mPortNo = 80; // default to 80
    private String mEndpoint = "";
    private String mUrl = null;
    private int mReqMethod = Request.Method.GET;
    private JSONObject mReqBody = new JSONObject();
    private HashMap<String, String> mReqHeaders = new HashMap<>();

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
        mPortNo = portNo;
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

    public NetworkManagerTA setMethod(int method) {
        mReqMethod = method;
        return mNetworkManagerTAInstance;
    }

    public NetworkManagerTA addBodyParam(String key, String value) {
        if (mReqBody == null) {
            mReqBody = new JSONObject();
        }
        try {
            mReqBody.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mNetworkManagerTAInstance;
    }

    public NetworkManagerTA addHeaderParam(String key, String value) {
        mReqHeaders.put(key, value);
        return mNetworkManagerTAInstance;
    }

    public void addToRequestQueue(Response.Listener onSuccess, Response.ErrorListener onFailure, Context context) {
        String url = (mUrl == null ? String.format("%s%s:%d%s", mScheme, mBaseUrl, mPortNo, mEndpoint) : mUrl);

        JsonObjectRequest request;
        if (mReqHeaders.size() == 0) {
            request = new JsonObjectRequest(mReqMethod, url, mReqBody, onSuccess, onFailure);
        } else {
            final HashMap<String, String> headers = mReqHeaders;
            request = new JsonObjectRequest(mReqMethod, url, mReqBody, onSuccess, onFailure){
                @Override
                public Map<String, String> getHeaders() {
                    return headers;
                }
            };
        }

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
        mScheme = HTTP;
        mBaseUrl = "";
        mPortNo = 80;
        mEndpoint = "";
        mUrl = null;
        mReqMethod = Request.Method.GET;
        mReqBody = null;
        mReqHeaders = new HashMap<>();
    }

//    protected Object readResolve() {
//        return NetworkManagerTA.getInstance();
//    }
}
