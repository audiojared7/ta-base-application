package com.tecace.analyticsta;

import android.os.Bundle;

public class EventCallback {
    public interface Handler {
        void send(String key, String value);
        void send(Bundle bundle);
    }
}
