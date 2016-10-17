package com.sakebook.android.sample.customtabssample.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.logics.ChromeCustomTabsCallback;
import com.sakebook.android.sample.customtabssample.logics.ServiceHandler;
import com.sakebook.android.sample.customtabssample.logics.ServiceHandlerCallback;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

public class SessionBottombarActivity extends AppCompatActivity {

    private final static String URL = "https://developer.android.com/index.html";
    private ServiceHandler serviceHandler;
    public ChromeCustomTabsCallback customTabsEventCallback = new ChromeCustomTabsCallback() {
        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            Log.d("CustomTabsSample", "onNavigationEvent");
        }

        @Override
        public void extraCallback(String callbackName, Bundle args) {
            Log.d("CustomTabsSample", "extraCallback");
        }
    };

    private ServiceHandlerCallback serviceHandlerCallback = new ServiceHandlerCallback() {
        @Override
        public void connectedFail() {
            Log.d("CustomTabsSample", "connectedFail");
        }

        @Override
        public void connected() {
            Log.d("CustomTabsSample", "connected");
        }

        @Override
        public void disconnected() {
            Log.d("CustomTabsSample", "disconnected");
        }
    };

    public static Intent createActivity(Context context) {
        return new Intent(context, SessionBottombarActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session_bottombar);
        serviceHandler = new ServiceHandler(this, serviceHandlerCallback, customTabsEventCallback);

        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsUtil.launchCustomTabsWithSessionBottombar(SessionBottombarActivity.this, URL, serviceHandler.getSession());
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        serviceHandler.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        serviceHandler.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
