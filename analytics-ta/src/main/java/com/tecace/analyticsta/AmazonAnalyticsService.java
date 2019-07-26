package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobile.client.Callback;
import com.amazonaws.mobile.client.UserStateDetails;
import com.amazonaws.mobile.config.AWSConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointConfiguration;
import com.amazonaws.mobileconnectors.pinpoint.PinpointManager;
import com.tecace.loggerta.LogTA;

public class AmazonAnalyticsService extends AnalyticsService {
    public static PinpointManager mPinpointManager;

    @Override
    public void init(String serviceName, Context context) {
        mIsInitialized = true;
        mServiceName = serviceName;

        mPinpointManager = getPinpointManager(context);
        mPinpointManager.getSessionClient().startSession();

        LogTA.w("Started Amazon Pinpoint session");
    }

    @Override
    public void send(int event, Bundle bundle) {

    }

    @Override
    public void deinit() {
        if (mIsInitialized) {
            mPinpointManager.getSessionClient().stopSession();
            mPinpointManager.getAnalyticsClient().submitEvents();

            mIsInitialized = false;
            mServiceName = null;
            mPinpointManager = null;

            LogTA.w("Ended Amazon Pinpoint session");
        } else {
            LogTA.w("Amazon hasn't been intialized yet");
        }
    }

    public static PinpointManager getPinpointManager(final Context applicationContext) {
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
