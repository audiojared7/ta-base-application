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
        if (mServiceProvider == null) {
            if (serviceName.equals(GOOGLE)) {
                mServiceProvider = new GoogleAnalyticsService();
                mServiceProvider.init(GOOGLE, context);
            } else if (serviceName.equals(AMAZON)) {
                mServiceProvider = new AmazonAnalyticsService();
                mServiceProvider.init(AMAZON, context);
            }
        } else {
            LogTA.w("Service provider has already been initialized to " + mServiceProvider.mServiceName);
        }
    }

    @Override
    public void send(int event, Bundle bundle) {
        if (mServiceProvider != null) {
            mServiceProvider.send(event, bundle);
        } else {
            LogTA.w("Must intialize analytics service provider first");
        }
    }

    @Override
    public void deinit() {
        if (mServiceProvider != null) {
            mServiceProvider.deinit();
        } else {
            LogTA.w("Service provider hasn't been intialized yet");
        }
    }
}
