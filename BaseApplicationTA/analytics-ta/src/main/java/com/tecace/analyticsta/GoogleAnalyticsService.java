package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tecace.loggerta.LogTA;

import static com.tecace.analyticsta.Event.APP_UNINSTALL;
import static com.tecace.analyticsta.Event.LOGIN;
import static com.tecace.analyticsta.Event.SEARCH;

public class GoogleAnalyticsService extends AnalyticsService {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void init(String serviceName, Context context) {
        mIsInitialized = true;
        mServiceName = serviceName;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        LogTA.w("Analytics initialized to Google");
    }

    @Override
    public void send(String event, String key, String value) {
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        if (mIsInitialized) {
            LogTA.w(String.format("Sending %s event", event));
            mFirebaseAnalytics.logEvent(event, bundle);
        }
    }

    @Override
    public void send(String event, Bundle bundle) {
        if (mIsInitialized) {
            LogTA.w(String.format("Sending %s event", event));
            mFirebaseAnalytics.logEvent(event, bundle);
        }
    }

    @Override
    public void deinit() {
        if (mIsInitialized) {
            mIsInitialized = false;
            mServiceName = null;
            mFirebaseAnalytics = null;

            LogTA.w("De-initialized Google Analytics");
        } else {
            LogTA.w("Google hasn't been intialized yet");
        }
    }
}
