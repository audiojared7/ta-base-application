package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.tecace.loggerta.LogTA;

import static com.tecace.analyticsta.Event.BUTTON_PRESS;
import static com.tecace.analyticsta.Event.LOGIN;
import static com.tecace.analyticsta.Event.SEARCH;

public class GoogleAnalyticsService extends AnalyticsService {
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void init(String serviceName, Context context) {
        mIsInitialized = true;
        mServiceName = serviceName;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    @Override
    public void send(int event, Bundle bundle) {
        if (mIsInitialized) {
            switch (event) {
                case LOGIN:
                    LogTA.w("made it to login event");
                    bundle.putString("username", "audiojared");
                    bundle.putString("password", "not a safe password");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                    break;
                case SEARCH:
                    LogTA.w("made it to search event");
                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SEARCH, bundle);
                    break;
                case BUTTON_PRESS:
                    LogTA.w("made it to button press event");
                    mFirebaseAnalytics.logEvent("button_press", bundle);
                    break;
                default:
                    LogTA.w("Received an unrecognized event");
            }
        }
    }

    @Override
    public void deinit() {
        if (mIsInitialized) {
            mIsInitialized = false;
            mServiceName = null;
            mFirebaseAnalytics = null;
        } else {
            LogTA.w("Google hasn't been intialized yet");
        }
    }
}
