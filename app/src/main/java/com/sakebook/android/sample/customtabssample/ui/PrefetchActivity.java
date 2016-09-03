package com.sakebook.android.sample.customtabssample.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;
import org.chromium.customtabsclient.shared.ServiceConnection;
import org.chromium.customtabsclient.shared.ServiceConnectionCallback;

public class PrefetchActivity extends AppCompatActivity implements ServiceConnectionCallback {

    private final static String URL_ARGS = "url";

    private CustomTabsClient customTabsClient;
    private ServiceConnection connection;
    private CustomTabsSession session;
    private String url = "";

    public static Intent createActivity(Context context, String url) {
        Intent intent = new Intent(context, PrefetchActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefetch);
        url = getIntent().getStringExtra(URL_ARGS);
        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsUtil.launchCustomTabs(PrefetchActivity.this, url, session);
//                CustomTabsUtil.launchCustomTabsWithBottombar(PrefetchActivity.this, URL, session);
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
        session = customTabsClient.newSession(new CustomTabsEventCallback());
        session.mayLaunchUrl(Uri.parse(url), null, null);
    }

    @Override
    public void onServiceDisconnected() {
        customTabsClient = null;
        connection = null;
        session = null;
    }


    public class CustomTabsEventCallback extends CustomTabsCallback {

        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            super.onNavigationEvent(navigationEvent, extras);
        }

        @Override
        public void extraCallback(String callbackName, Bundle args) {
            super.extraCallback(callbackName, args);
        }
    }
}
