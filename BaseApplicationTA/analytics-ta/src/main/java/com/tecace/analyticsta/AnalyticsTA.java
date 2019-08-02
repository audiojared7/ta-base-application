package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

import com.tecace.loggerta.LogTA;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecace.analyticsta.ServiceProvider.AMAZON;
import static com.tecace.analyticsta.ServiceProvider.GOOGLE;

public class AnalyticsTA implements Analytics {
    private AnalyticsService mServiceProvider;

    private Map<String, EventCallback.Handler> mCustomEventsMap = new HashMap<>();

    private static AnalyticsTA mAnalyticsTAInstance;
    private AnalyticsTA() {
        if (mAnalyticsTAInstance != null) {
            LogTA.e("Use getInstance() method!");
        }
    }
    public static AnalyticsTA getInstance() {
        if (mAnalyticsTAInstance == null) {
            mAnalyticsTAInstance = new AnalyticsTA();
        }
        return mAnalyticsTAInstance;
    }

    public void addCustomEvent(String eventName, EventCallback.Handler eventHandler) {
        mCustomEventsMap.put(eventName, eventHandler);
    }

    @Override
    public void init(String serviceName, Context context) {
        if (mServiceProvider != null) {
            LogTA.w("Analytics service provider has already been initialized to " + mServiceProvider.mServiceName);
            return;
        }

        if (serviceName.equals(GOOGLE)) {
            mServiceProvider = new GoogleAnalyticsService();
            mServiceProvider.init(GOOGLE, context);
        } else if (serviceName.equals(AMAZON)) {
            mServiceProvider = new AmazonAnalyticsService();
            mServiceProvider.init(AMAZON, context);
        }
    }

    @Override
    public void send(String event, Bundle bundle) {
        if (mServiceProvider == null) {
            LogTA.w("Must intialize analytics service provider first");
            return;
        }

        mServiceProvider.send(event, bundle);
    }

    @Override
    public void send(String event, String key, String value) {
        if (mServiceProvider == null) {
            LogTA.w("Must intialize analytics service provider first");
            return;
        }

        mServiceProvider.send(event, key, value);
    }

    @Override
    public void sendCustom(String customEvent, String key, String value) {
        EventCallback.Handler callback = mCustomEventsMap.get(customEvent);
        callback.send(key, value);
    }

    @Override
    public void sendCustom(String customEvent, Bundle bundle) {
        EventCallback.Handler callback = mCustomEventsMap.get(customEvent);
        callback.send(bundle);
    }

    @Override
    public void deinit() {
        if (mServiceProvider == null) {
            LogTA.w("Analytics service provider hasn't been intialized yet");
            return;
        }

        mServiceProvider.deinit();
        mServiceProvider = null;
    }
}
