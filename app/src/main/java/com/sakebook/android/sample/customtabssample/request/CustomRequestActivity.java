package com.sakebook.android.sample.customtabssample.request;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Browser;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sakebook.android.sample.customtabssample.R;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class CustomRequestActivity extends AppCompatActivity {

    private final static String URL_ARGS = "url";
    private String url = "";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, CustomRequestActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_request);
        url = getIntent().getStringExtra(URL_ARGS);

        findViewById(R.id.button_request_header).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabsWithHeader(url);
            }
        });
        findViewById(R.id.button_request_referrer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabsWithReferrer(CustomRequestActivity.this, url);
            }
        });
        findViewById(R.id.button_request_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabs(url);
            }
        });
    }

    private void launchCustomTabsWithHeader(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setPackage(packageName);
        Bundle headers = new Bundle();
        headers.putString("header1", "value1");
        headers.putString("header2", "value2");
        customTabsIntent.intent.putExtra(Browser.EXTRA_HEADERS, headers);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void launchCustomTabsWithReferrer(Context context, String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setPackage(packageName);

        customTabsIntent.intent.putExtra(Intent.EXTRA_REFERRER,
                Uri.parse("android-app://" + context.getPackageName()));
        customTabsIntent.launchUrl(context, Uri.parse(url));
    }

    private void launchCustomTabs(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder().build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
