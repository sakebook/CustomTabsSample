package com.sakebook.android.sample.customtabssample.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sakebook.android.sample.customtabssample.LauncherActivity;
import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.receivers.ShareBroadcastReceiver;
import com.sakebook.android.sample.customtabssample.services.ClipboardService;
import com.sakebook.android.sample.customtabssample.utils.ResourceUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

import static android.support.customtabs.CustomTabsIntent.EXTRA_ENABLE_URLBAR_HIDING;

public class CustomToolbarActivity extends AppCompatActivity {

    private final static String URL_ARGS = "url";
    private String url = "";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, CustomToolbarActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_toolbar);
        url = getIntent().getStringExtra(URL_ARGS);

        findViewById(R.id.button_toolbar_dark).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabs(url, R.color.brown_500);
            }
        });
        findViewById(R.id.button_toolbar_light).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabs(url, R.color.yellow_500);
            }
        });
        findViewById(R.id.button_toolbar_no_tint_action_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabs(url);
            }
        });
        findViewById(R.id.button_toolbar_add_menu_item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                launchCustomTabsWithMenuItem(url);
            }
        });
    }

    private void launchCustomTabs(@NonNull String url, @ColorRes int colorRes) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }

        CustomTabsIntent.Builder builder =
                new CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(this, colorRes))
                        .setActionButton(ResourceUtil.getBitmap(this, R.drawable.ic_android_pink_500), "sample", createActivityPendingIntent(12), true)
                        .setShowTitle(true)
                ;

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.intent.putExtra(EXTRA_ENABLE_URLBAR_HIDING, false);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void launchCustomTabs(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }

        CustomTabsIntent.Builder builder =
                new CustomTabsIntent.Builder()
                        .setActionButton(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher), "sample1", createActivityPendingIntent(11)) // Not displayed.
                        .setActionButton(ResourceUtil.getBitmap(this, R.drawable.ic_android_pink_500), "sample2", createActivityPendingIntent(12))
                ;

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void launchCustomTabsWithMenuItem(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }

        CustomTabsIntent.Builder builder =
                new CustomTabsIntent.Builder()
                        .addMenuItem("copy1", createServicePendingIntent(21))
                        .addMenuItem("copy2", createServicePendingIntent(22))
                        .addMenuItem("share1", createBroadcastPendingIntent(23))
                        .addMenuItem("share2", createBroadcastPendingIntent(24))
                        .addMenuItem("home1", createActivityPendingIntent(25))
                        .addMenuItem("home2", createActivityPendingIntent(26)) // Not displayed.
                        .addDefaultShareMenuItem()
                        .setCloseButtonIcon(ResourceUtil.getBitmap(this, R.drawable.ic_android_pink_500))
                ;

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private PendingIntent createActivityPendingIntent(int requestCode) {
        Intent intent = new Intent(this, LauncherActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent createBroadcastPendingIntent(int requestCode) {
        Intent intent = new Intent(this, ShareBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }

    private PendingIntent createServicePendingIntent(int requestCode) {
        Intent intent = new Intent(this, ClipboardService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }
}
