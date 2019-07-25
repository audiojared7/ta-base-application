package com.tecace.tabaseapplication;

import android.app.Application;
import android.content.Context;

import com.tecace.loggerta.LogTA;

public class AppEntry extends Application {
    private static Context mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = this;

        LogTA.plant(mAppContext);
    }

    public static AppEntry getInstance() { return (AppEntry) mAppContext; }

    public static Context getAppContext() { return mAppContext; }
}
