package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

public interface Analytics {
    void init(String serviceName, Context context);

    void send(String event, Bundle bundle);

    void send(String event, String key, String value);

    void sendCustom(String customEvent, String key, String value);

    void sendCustom(String customEvent, Bundle bundle);

    void deinit();
}
