package com.sakebook.android.sample.customtabssample.session;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.utils.ResourceUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;
import org.chromium.customtabsclient.shared.ServiceConnection;
import org.chromium.customtabsclient.shared.ServiceConnectionCallback;

import java.util.Random;

public class SessionActivity extends AppCompatActivity implements ServiceConnectionCallback {

    private final static String URL_ARGS = "url";

    private CustomTabsClient customTabsClient;
    private ServiceConnection connection;
    private String url = "";
    // FIXME: not so hot
    private static CustomTabsSession session;
    private static int pointer = 0;
    private static boolean isActiveRemoteView = true;
    private final static int[] icons = { R.drawable.twitter, R.drawable.line, R.drawable.facebook };
    private final static int ids[] = {R.id.text_view};
    private final static String ACTION_TOGGLE_REMOTE_VIEW = "action_toggle_remote_view";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, SessionActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);
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
        session = customTabsClient.newSession(null);
    }

    @Override
    public void onServiceDisconnected() {
        customTabsClient = null;
        connection = null;
    }

    private void launchCustomTabs(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }
        Intent snsIntent = new Intent(this, SessionBroadcastReceiver.class);
        PendingIntent snsPendingIntent = PendingIntent.getBroadcast(this, 121, snsIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(session)
                .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setActionButton(ResourceUtil.createBitmap(SessionActivity.this, icons[0]), "SNS", snsPendingIntent)
                .addMenuItem("toggle", createRemoteViewPendingIntent(this))
                .setSecondaryToolbarViews(SessionBroadcastReceiver.createRemoteView(), ids, null)
                .build();
        customTabsIntent.intent.setPackage(packageName);
        CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private static PendingIntent createRemoteViewPendingIntent(Context context) {
        Intent remoteViewIntent = new Intent(context, SessionBroadcastReceiver.class);
        remoteViewIntent.setAction(ACTION_TOGGLE_REMOTE_VIEW);
        return PendingIntent.getBroadcast(context, 122, remoteViewIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static class SessionBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // click action button
            if (TextUtils.isEmpty(intent.getAction())) {
                pointer = (pointer + 1) % 3;
                session.setActionButton(ResourceUtil.createBitmap(context, icons[pointer]), "SNS");
                return;
            }
            boolean successUpdateRemoteView;
            if (!isActiveRemoteView) {
                successUpdateRemoteView = session.setSecondaryToolbarViews(createRemoteView(), ids, createRemoteViewPendingIntent(context));
            } else {
                // need dummy remote view. null is not work.
                successUpdateRemoteView = session.setSecondaryToolbarViews(dummyRemoteView(), null, null);
            }
            Log.d("CustomTabsSample", "SessionBroadcastReceiver onReceive successUpdateRemoteView: " + successUpdateRemoteView);
            if (successUpdateRemoteView) {
                isActiveRemoteView = !isActiveRemoteView;
            }
        }

        static RemoteViews createRemoteView() {
            RemoteViews remoteView = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.remote_transparent_layout);
            Random random = new Random();
            remoteView.setTextColor(R.id.text_view, Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            return remoteView;
        }

        static RemoteViews dummyRemoteView() {
            RemoteViews remoteView = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.remote_dummy_layout);
            return remoteView;
        }
    }
}
