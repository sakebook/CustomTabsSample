package com.sakebook.android.sample.customtabssample.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class OauthActivity extends AppCompatActivity {

    private String url = "";

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, OauthActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oauth);
        Log.d("CustomTabsSample", this.getClass().getSimpleName() + ": onCreate: " + getIntent().getDataString());
        url = getString(R.string.oauth_request_url, BuildConfig.CLIENT_ID);

        findViewById(R.id.button_oauth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabs(url);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (TextUtils.isEmpty(intent.getDataString())) {
            return;
        }
        Uri uri = intent.getData();
        String code = uri.getQueryParameter("code");
        if (TextUtils.isEmpty(code)) {
            // Maybe error
            String error = uri.getQueryParameter("error");
            Snackbar.make(findViewById(R.id.button_oauth), "Error reason: " + error, Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(findViewById(R.id.button_oauth), "Success code: " + code, Snackbar.LENGTH_LONG).show();
        }
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
