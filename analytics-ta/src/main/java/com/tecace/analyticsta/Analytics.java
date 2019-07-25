package com.tecace.analyticsta;

import android.content.Context;
import android.os.Bundle;

public interface Analytics {
    void init(String serviceName, Context context);

    void send(int event, Bundle bundle);

    void deinit();
}
