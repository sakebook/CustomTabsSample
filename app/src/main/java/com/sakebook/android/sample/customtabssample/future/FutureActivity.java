package com.sakebook.android.sample.customtabssample.future;

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
import com.sakebook.android.sample.customtabssample.remoteview.RemoteViewActivity;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class FutureActivity extends AppCompatActivity {

    private final static String URL_ARGS = "url";
    private String url = "";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, FutureActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future);
        url = getIntent().getStringExtra(URL_ARGS);

        findViewById(R.id.button_intent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchIntent(url);
            }
        });
        findViewById(R.id.button_intent_browser_ui).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchIntentForceBrowserUI(url);
            }
        });
    }

    private void launchIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void launchIntentForceBrowserUI(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(CustomTabsIntent.setAlwaysUseBrowserUI(intent));
    }
}
