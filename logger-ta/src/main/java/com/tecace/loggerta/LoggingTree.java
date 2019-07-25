package com.tecace.loggerta;

import android.util.Log;

import timber.log.Timber;

public class LoggingTree extends Timber.DebugTree {

    @Override
    protected void log(int priority, String tag, String message, Throwable t) {
        switch (priority) {
            case Log.VERBOSE:  Log.v(tag, message, t);  break;
            case Log.DEBUG:    Log.d(tag, message, t);  break;
            case Log.INFO:     Log.i(tag, message, t);  break;
            case Log.WARN:     Log.w(tag, message, t);  break;
            case Log.ERROR:    Log.e(tag, message, t);  break;
            case Log.ASSERT:   Log.e(tag, message, t);  break;
        }
    }

    @Override
    protected String createStackElementTag(StackTraceElement element) {
        // Add log statements line number to the log
        return super.createStackElementTag(element) + " - " + element.getLineNumber();
    }
}
