package com.sakebook.android.sample.customtabssample.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.receivers.BottombarBroadcastReceiver;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class RemoteViewActivity extends AppCompatActivity {

    private final static String URL_ARGS = "url";
    private String url = "";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, RemoteViewActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);
        url = getIntent().getStringExtra(URL_ARGS);

        findViewById(R.id.button_remote_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabsWithBottomBar(url);
            }
        });
    }

    private void launchCustomTabsWithBottomBar(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }

        Intent broadcastIntent = new Intent(this, BottombarBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 110, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int[] ids = {R.id.twitter, R.id.line, R.id.facebook};

        CustomTabsIntent customTabsIntent = new CustomTabsIntent
                .Builder()
                .setSecondaryToolbarViews(createBottomBarRemoteView(), ids, pendingIntent)
                .build();

        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private RemoteViews createBottomBarRemoteView() {
        return new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.remote_bottom_layout);
    }
}
