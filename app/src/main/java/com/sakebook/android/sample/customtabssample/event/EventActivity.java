package com.sakebook.android.sample.customtabssample.event;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.utils.ResourceUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;
import org.chromium.customtabsclient.shared.ServiceConnection;
import org.chromium.customtabsclient.shared.ServiceConnectionCallback;

public class EventActivity extends AppCompatActivity implements ServiceConnectionCallback {

    private final static String URL_ARGS = "url";

    private CustomTabsClient customTabsClient;
    private ServiceConnection connection;
    private CustomTabsSession session;
    private String url = "";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        url = getIntent().getStringExtra(URL_ARGS);
        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabs(url);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        connect();
    }

    private void connect() {
        if (customTabsClient != null) {
            // already connected
            return;
        }
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        connection = new ServiceConnection(this);
        CustomTabsClient.bindCustomTabsService(this, packageName, connection);
    }


    @Override
    protected void onStop() {
        super.onStop();
        disconnect();
    }

    private void disconnect() {
        if (connection == null) {
            // Unnecessary disconnect
            return;
        }
        customTabsClient = null;
        unbindService(connection);
    }


    @Override
    public void onServiceConnected(CustomTabsClient client) {
        customTabsClient = client;
        customTabsClient.warmup(0L);
        // Set callback
        session = customTabsClient.newSession(new MyCustomTabsCallback());
    }

    @Override
    public void onServiceDisconnected() {
        customTabsClient = null;
        connection = null;
//        session = null;
    }

    private void launchCustomTabs(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }
        // Put session
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(session)
                .build();
        customTabsIntent.intent.setPackage(packageName);
        // Keep alive service
        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private class MyCustomTabsCallback extends CustomTabsCallback {

        private Handler handler;

        public MyCustomTabsCallback() {
            super();
            handler = new Handler();
        }

        @Override
        public void onNavigationEvent(final int navigationEvent, Bundle extras) {
            super.onNavigationEvent(navigationEvent, extras);
            String eventName = getEventName(navigationEvent);
            showMessage(eventName);
        }

        private String getEventName(int navigationEvent) {
            String eventName;
            if (navigationEvent == NAVIGATION_STARTED) {
                eventName = "NAVIGATION_STARTED";
            } else if (navigationEvent == NAVIGATION_FINISHED) {
                eventName = "NAVIGATION_FINISHED";
            } else if (navigationEvent == NAVIGATION_FAILED) {
                eventName = "NAVIGATION_FAILED";
            } else if (navigationEvent == NAVIGATION_ABORTED) {
                eventName = "NAVIGATION_ABORTED";
            } else if (navigationEvent == TAB_SHOWN) {
                eventName = "TAB_SHOWN";
            } else if (navigationEvent == TAB_HIDDEN) {
                eventName = "TAB_HIDDEN";
            } else {
                eventName = "UNKNOWN";
            }
            return eventName;
        }

        private void showMessage(final String message) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(EventActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
