package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

import com.tecace.loggerta.LogTA;

import static com.tecace.analyticsta.ServiceProvider.AMAZON;
import static com.tecace.analyticsta.ServiceProvider.GOOGLE;

public class AnalyticsTA implements Analytics {
    private AnalyticsService mServiceProvider;

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
    public void send(int event, Bundle bundle) {
        if (mServiceProvider == null) {
            LogTA.w("Must intialize analytics service provider first");
            return;
        }

        mServiceProvider.send(event, bundle);
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
