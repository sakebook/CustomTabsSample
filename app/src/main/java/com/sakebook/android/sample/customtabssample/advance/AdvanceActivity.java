package com.sakebook.android.sample.customtabssample.advance;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.utils.ResourceUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class AdvanceActivity extends AppCompatActivity {

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

    public static Intent createIntent(Context context) {
        return new Intent(context, AdvanceActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance);
        serviceHandler = new ServiceHandler(this, serviceHandlerCallback, customTabsEventCallback);

        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabsWithSessionBottombar(AdvanceActivity.this, URL, serviceHandler.getSession());
            }
        });
        serviceHandler.setMayLaunchUrl(URL);
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

    public void launchCustomTabsWithSessionBottombar(Activity activity, String url, @Nullable CustomTabsSession session) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        CustomTabsIntent.Builder builder;
        if (session == null) {
            builder = new CustomTabsIntent.Builder();
        } else {
            builder = new CustomTabsIntent.Builder(session);
        }

        // Intent hacked
        Intent cctIntent = new CustomTabsIntent.Builder(session).build().intent;
        cctIntent.setData(Uri.parse(url));
        PendingIntent cctPendingIntent = PendingIntent.getActivity(activity, 124, cctIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        addSessionBottombar(builder, activity, cctPendingIntent);

        CustomTabsIntent customTabsIntent = builder
                .setToolbarColor(Color.WHITE)
                .build();
        CustomTabsHelper.addKeepAliveExtra(activity, customTabsIntent.intent);
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    private void addSessionBottombar(CustomTabsIntent.Builder builder, Activity activity, PendingIntent pIntent) {
        Intent broadcastIntent = new Intent(activity, AdvanceBroadcastReceiver.class);
        broadcastIntent.setAction(AdvanceBroadcastReceiver.ACTION_ADD_FAVORITE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 120, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int[] ids = {R.id.image_favorite};
        builder.setSecondaryToolbarViews(CustomBottombar.createSessionBottombar(activity, false), ids, pendingIntent);
        builder.setActionButton(ResourceUtil.createBitmap(activity, R.drawable.ic_home), "home", pIntent);
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
    }
}
