package com.tecace.eventbusta;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

public class EventBusTA extends Bus {
    private static final EventBusTA mBus = new EventBusTA();

    private final Handler handler = new Handler(Looper.getMainLooper());

    private EventBusTA() {
        // No instances.
    }

    public static EventBusTA getInstance() { return mBus; }

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            handler.post(() -> EventBusTA.super.post(event));
        }
    }
}
