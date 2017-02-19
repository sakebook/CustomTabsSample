package com.sakebook.android.sample.customtabssample;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.sakebook.android.sample.customtabssample.ui.BottombarActivity;
import com.sakebook.android.sample.customtabssample.ui.CustomAnimationActivity;
import com.sakebook.android.sample.customtabssample.ui.CustomToolbarActivity;
import com.sakebook.android.sample.customtabssample.ui.SessionBottombarActivity;
import com.sakebook.android.sample.customtabssample.ui.DeprecatedBottombarActivity;
import com.sakebook.android.sample.customtabssample.ui.PrefetchActivity;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class LauncherActivity extends AppCompatActivity {

    private final static String URL = "https://developer.android.com/index.html";
//    private final static String URL = "http://cgi.b4iine.net/env/";


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.launch_button:
                    launchCustomTabs(URL);
                    return;
                case R.id.custom_toolbar_button:
                    intent = CustomToolbarActivity.createIntent(LauncherActivity.this, URL);
                    break;
                case R.id.animation_button:
                    intent = CustomAnimationActivity.createIntent(LauncherActivity.this, URL);
                    break;
                case R.id.prefetch_button:
                    intent = PrefetchActivity.createIntent(LauncherActivity.this, URL);
                    break;
                case R.id.deprecated_bottom_bar_button:
                    intent = DeprecatedBottombarActivity.createIntent(LauncherActivity.this, URL);
                    break;
                case R.id.bottombar_button:
                    intent = BottombarActivity.createIntent(LauncherActivity.this, URL);
                    break;
                case R.id.session_bottom_bar_button:
                    intent = SessionBottombarActivity.createIntent(LauncherActivity.this);
                    break;
                default:
                    intent = CustomToolbarActivity.createIntent(LauncherActivity.this, URL);
                    break;
            }
            startActivity(intent);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initLayout();
    }

    private void initLayout() {
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layout_parent);
        int viewCount = viewGroup.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            viewGroup.getChildAt(i).setOnClickListener(onClickListener);
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
