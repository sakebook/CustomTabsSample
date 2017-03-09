package com.sakebook.android.sample.customtabssample.warmup;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sakebook.android.sample.customtabssample.R;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class WarmupActivity extends AppCompatActivity {

    private final static String URL_ARGS = "url";
    private String url = "";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, WarmupActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warmup);
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
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        CustomTabsClient.connectAndInitialize(this, packageName);
    }

    private void launchCustomTabs(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                .build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

}
