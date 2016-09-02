package com.sakebook.android.sample.customtabssample.ui;

import android.content.Context;
import android.content.Intent;
import android.support.customtabs.CustomTabsClient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class BottombarActivity extends AppCompatActivity {

    private final static String URL = "https://developer.android.com/index.html";

    public static Intent createActivity(Context context) {
        return new Intent(context, BottombarActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottombar);

        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        CustomTabsClient.connectAndInitialize(this, packageName);

        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CustomTabsSample", "BottombarActivity warm up is ");
                CustomTabsUtil.launchCustomTabsWithBottombar(BottombarActivity.this, URL, null);
            }
        });


//        Intent intent = new Intent(Intent.ACTION_VIEW);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this, 200, intent, PendingIntent.FLAG_CANCEL_CURRENT);
//        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
//        int[] ids = {0};
//        builder.setSecondaryToolbarViews(createRemoteViews(), ids, pendingIntent);
//        CustomTabsIntent customTabsIntent = builder.build();
//        customTabsIntent.intent.setPackage(packageName);
//        customTabsIntent.launchUrl(this, Uri.parse("http://yahoo.co.jp"));
    }


    public static RemoteViews createRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.remote_bottom_layout);
//        remoteViews.setImageViewResource(R.id.remote_image, R.drawable.ic_android_teal_700_18dp);
        return remoteViews;
    }
}
