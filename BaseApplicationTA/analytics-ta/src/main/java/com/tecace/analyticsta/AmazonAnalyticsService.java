package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.amazonaws.mobileconnectors.pinpoint.analytics.AnalyticsEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.tecace.loggerta.LogTA;

import java.util.Arrays;

import static com.tecace.analyticsta.Event.LOGIN;
import static com.tecace.analyticsta.Event.SEARCH;

public class AmazonAnalyticsService extends AnalyticsService {
    private static PinpointManager mPinpointManager;

    @Override
    public void init(String serviceName, Context context) {
        mIsInitialized = true;
        mServiceName = serviceName;

        mPinpointManager = getPinpointManager(context);
        mPinpointManager.getSessionClient().startSession();

        LogTA.w("Analytics initialized to Amazon Pinpoint, session started");
    }

    @Override
    public void send(int event, String key, String value) {
        AnalyticsEvent logEvent = null;
        if (mIsInitialized) {
            switch (event) {
                case LOGIN:
                    LogTA.w("Sending login event");

                    logEvent = mPinpointManager.getAnalyticsClient()
                            .createEvent("login")
                            .withAttribute(key, value);
                    break;
                case SEARCH:
                    LogTA.w("Sending search event");

                    logEvent = mPinpointManager.getAnalyticsClient()
                            .createEvent("search")
                            .withAttribute(key, value);
                    break;
                default:
                    LogTA.w("Received an unrecognized event");
            }
        }
        mPinpointManager.getAnalyticsClient().recordEvent(logEvent);
        mPinpointManager.getAnalyticsClient().submitEvents();
    }

    @Override
    public void send(int event, Bundle bundle) {
        AnalyticsEvent logEvent = null;
        String[] eventParams = (String[]) bundle.keySet().toArray();
        if (mIsInitialized) {
            switch (event) {
                case LOGIN:
                    LogTA.w("Sending login event");
                    logEvent = mPinpointManager.getAnalyticsClient().createEvent("login");
                    break;
                case SEARCH:
                    LogTA.w("Sending search event");
                    logEvent = mPinpointManager.getAnalyticsClient().createEvent("search");
                    break;
                default:
                    LogTA.w("Received an unrecognized event");
            }
        }
        if (logEvent != null) {
            for (int i = 0; i < bundle.size(); i++) {
                logEvent = logEvent.withAttribute(eventParams[i], bundle.getString(eventParams[i]));
            }
        }
        mPinpointManager.getAnalyticsClient().recordEvent(logEvent);
        mPinpointManager.getAnalyticsClient().submitEvents();
    }

    @Override
    public void deinit() {
        if (mIsInitialized) {
            mPinpointManager.getSessionClient().stopSession();
            mPinpointManager.getAnalyticsClient().submitEvents();

            mIsInitialized = false;
            mServiceName = null;
            mPinpointManager = null;

            LogTA.w("De-initialized Amazon Pinpoint, session ended");
        } else {
            LogTA.w("Amazon hasn't been intialized yet");
        }
    }

    private static PinpointManager getPinpointManager(final Context applicationContext) {
        if (mPinpointManager == null) {
            // Initialize the AWS Mobile Client
            final AWSConfiguration awsConfig = new AWSConfiguration(applicationContext);
            AWSMobileClient.getInstance().initialize(applicationContext, awsConfig, new Callback<UserStateDetails>() {
                @Override
                public void onResult(UserStateDetails userStateDetails) {
                    LogTA.i("AWS mobile client initialization success. User state: " + userStateDetails.getUserState());
                }

                @Override
                public void onError(Exception e) {
                    LogTA.e("AWS mobile client initialization error: " + e);
                }
            });

            PinpointConfiguration pinpointConfig = new PinpointConfiguration(
                    applicationContext,
                    AWSMobileClient.getInstance(),
                    awsConfig);

            return new PinpointManager(pinpointConfig);
        }
        return mPinpointManager;
    }
}
