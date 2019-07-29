package com.tecace.baseapplicationta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.squareup.otto.Subscribe;
import com.tecace.analyticsta.AnalyticsTA;
import com.tecace.eventbusta.EventBusTA;
import com.tecace.loggerta.LogTA;
import com.tecace.networkmanagerta.NetworkManagerTA;
import com.tecace.networkmanagerta.Scheme;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.tecace.analyticsta.Event.SEARCH;
import static com.tecace.analyticsta.ServiceProvider.AMAZON;
import static com.tecace.analyticsta.ServiceProvider.GOOGLE;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.init_button_google)
    Button mInitButtonGoogle;
    @BindView(R.id.init_button_amazon) Button mInitButtonAmazon;
    @BindView(R.id.send_button)        Button mSendButton;
    @BindView(R.id.deinit_button)      Button mDeInitButton;

    private AnalyticsTA mAnalyticsTA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBusTA.getInstance().register(this);

        mAnalyticsTA = AnalyticsTA.getInstance();

        mInitButtonGoogle.setOnClickListener(view -> mAnalyticsTA.init(GOOGLE, this));
        mInitButtonAmazon.setOnClickListener(view -> mAnalyticsTA.init(AMAZON, this));

        mSendButton.setOnClickListener(view -> {
            Bundle bundle = new Bundle();
            bundle.putString("search_query", "my first search query");
            mAnalyticsTA.send(SEARCH, bundle);

            mAnalyticsTA.send(SEARCH, "search_query", "my second search query");

//            bundle.putString("username", "audiojared");
//            bundle.putString("password", "not a safe password");
//            mAnalyticsTA.send(LOGIN, bundle);
        });

        mDeInitButton.setOnClickListener(view -> mAnalyticsTA.deinit());
    }

    void testNetwork() {
        Response.Listener onProjectInfoSuccess = response -> {
            // do something with the response
            LogTA.w(response.toString()+"\n.\n");
        };
        Response.ErrorListener onProjectInfoFailure = error -> {
            // do something with the error
            LogTA.w("ERROR: " + error);
        };

        Response.Listener onSignInSuccess = response -> {
            // do something with the response
            LogTA.w(response.toString()+"\n.\n");
            try {
                JSONObject responseObj = new JSONObject(response.toString());
                String jwtToken = responseObj.getJSONObject("Data").getString("token");

                NetworkManagerTA.getInstance()
                        .setScheme(Scheme.HTTP)
                        .setBaseUrl("nteam.tecace.com")
                        .setPortNo(4510)
                        .setEndpoint("/api/v1/project")
                        .addHeaderParam("Authorization", "JWT " + jwtToken)
                        .addToRequestQueue(onProjectInfoSuccess, onProjectInfoFailure, this);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        };
        Response.ErrorListener onSignInFailure = error -> {
            // do something with the error
            LogTA.w("ERROR: " + error);
        };

        NetworkManagerTA.getInstance()
                .setScheme(Scheme.HTTP)
                .setBaseUrl("nteam.tecace.com")
                .setPortNo(4510)
                .setEndpoint("/api/v1/signin")
                .setMethod(Request.Method.POST)
                .addBodyParam("email", "tecace101@gmail.com")
                .addBodyParam("password", "1234")
                .addToRequestQueue(onSignInSuccess, onSignInFailure, this);
    }
}
