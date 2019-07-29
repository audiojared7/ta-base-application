package com.tecace.loggerta;

import android.content.Context;

import timber.log.Timber;

public class LogTA {
    private static Context mContext;

    public static void plant(Context context) {
        mContext = context;
        if (BuildConfig.DEBUG) {
            Timber.plant(new LoggingTree());
        } else {
            Timber.plant(new NotLoggingTree());
        }
    }

    public static void v (String message) { Timber.v(message); }
    public static void d (String message) { Timber.d(message); }
    public static void i (String message) { Timber.i(message); }
    public static void w (String message) { Timber.w(message); }
    public static void e (String message) { Timber.e(message); }
    public static void f (String tag, String message) { FileLog.log(tag, message, mContext); }
}
