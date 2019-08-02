package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

public class AnalyticsService implements Analytics {
    protected String mServiceName;
    protected boolean mIsInitialized = false;

    @Override
    public void init(String serviceName, Context context) {}

    @Override
    public void send(String event, String key, String value) {}

    @Override
    public void send(String event, Bundle bundle) {}

    @Override
    public void sendCustom(String customEvent, String key, String value) {}

    @Override
    public void sendCustom(String customEvent, Bundle bundle) {}

    @Override
    public void deinit() {}
}
