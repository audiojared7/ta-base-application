package com.tecace.tabaseapplication.events;

public class TestEvent {
    private String mTestMessage;

    public TestEvent(String testMessage) {
        mTestMessage = testMessage;
    }

    public String getTestMessage() {
        return mTestMessage;
    }
}
