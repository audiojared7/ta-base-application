package com.tecace.tabaseapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.otto.Subscribe;
import com.tecace.analyticsta.AnalyticsTA;
import com.tecace.eventbusta.EventBusTA;
import com.tecace.loggerta.LogTA;
import com.tecace.networkmanagerta.NetworkManagerTA;
import com.tecace.networkmanagerta.Scheme;
import com.tecace.tabaseapplication.R;
import com.tecace.tabaseapplication.events.TestEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tecace.analyticsta.Event.SEARCH;
import static com.tecace.analyticsta.ServiceProvider.AMAZON;
import static com.tecace.analyticsta.ServiceProvider.GOOGLE;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.init_button)   Button mInitButton;
    @BindView(R.id.send_button)   Button mSendButton;
    @BindView(R.id.deinit_button) Button mDeInitButton;

    private AnalyticsTA mAnalyticsTA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBusTA.getInstance().register(this);

        mAnalyticsTA = AnalyticsTA.getInstance();

        mInitButton.setOnClickListener(view -> {
//            mAnalyticsTA.init(GOOGLE, this);
            mAnalyticsTA.init(AMAZON, this);
        });
        mSendButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("search_term", "my search query");
            mAnalyticsTA.send(SEARCH, bundle);
        });
        mDeInitButton.setOnClickListener(view -> {
            mAnalyticsTA.deinit();
        });
    }

    void testNetwork() {
        Response.Listener onSuccess = (Response.Listener<JSONObject>) response -> {
            // do something with the response
            LogTA.w(response.toString()+"\n.\n");
        };
        Response.ErrorListener onFailure = error -> {
            // do something with the error
            LogTA.w("ERROR: " + error);
        };



        JSONObject body = new JSONObject();
        try {
            body.put("email", "tecace101@gmail.com");
            body.put("password", "1234");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManagerTA.getInstance()
                .setScheme(Scheme.HTTP)
                .setBaseUrl("nteam.tecace.com")
                .setPortNo(4510)
                .setEndpoint("/api/v1/signin")
                .addToRequestQueue(body, onSuccess, onFailure, this);



        HashMap<String, String> headers = new HashMap<>();
        headers.put("Authorization", "JWT eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0eXBlIjoiYWNjZXNzIiwidXNlciI6eyJpZCI6ImViZGI5MmFkLTA2Y2ItNDc0OS1iNzA3LTgwMzFkOTExMTBlYyIsImVtYWlsIjoidGVjYWNlMTAxQGdtYWlsLmNvbSIsImZuYW1lIjoiVGVjQWNlIiwibG5hbWUiOiJTb2Z0d2FyZSIsImlzYWRtaW4iOjEsIm9yZ2lkIjoiNDc4Y2ZlMDQtZjg1MS00ZTA1LWIwZDktNjVkNDQ4ZGEzZTJjIiwicGhvbmVudW1iZXIiOiI0MjUtOTUyLTYwNzAiLCJwaG90b3VybCI6bnVsbH0sImlhdCI6MTU2MzkxOTM5OSwiZXhwIjoxNTY0MDA1Nzk5fQ.JymaYFm0vcgdt9hlOttUBkG2LQgGH2tlTxmr5eiQfgg");
        NetworkManagerTA.getInstance()
                .setScheme(Scheme.HTTP)
                .setBaseUrl("nteam.tecace.com:4510")
                .setEndpoint("/api/v1/project")
                .addToRequestQueue(Request.Method.GET, null, headers, onSuccess, onFailure, this);



        JSONObject body2 = new JSONObject();
        try {
            body2.put("field1", "value1");
            body2.put("field2", "value2");
            body2.put("field3", "value3");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        NetworkManagerTA.getInstance()
                .setUrl("http://192.168.86.74:3000/testReturnPOST")
                .addToRequestQueue(Request.Method.POST, body2, onSuccess, onFailure, this);
    }

    @Subscribe
    public void OnTestEventReceived(TestEvent testEvent) {
        LogTA.w(testEvent.getTestMessage());
    }
}
