package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

public class AnalyticsService implements Analytics {
    protected String mServiceName;
    protected boolean isInitialized = false;

    @Override
    public void init(String serviceName, Context context) {}

    @Override
    public void send(int event, Bundle bundle) {}

    @Override
    public void deinit() {}
}
