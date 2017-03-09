package com.sakebook.android.sample.customtabssample;

import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.sakebook.android.sample.customtabssample.event.EventActivity;
import com.sakebook.android.sample.customtabssample.future.FutureActivity;
import com.sakebook.android.sample.customtabssample.remoteview.RemoteViewActivity;
import com.sakebook.android.sample.customtabssample.animation.CustomAnimationActivity;
import com.sakebook.android.sample.customtabssample.request.CustomRequestActivity;
import com.sakebook.android.sample.customtabssample.toolbar.CustomToolbarActivity;
import com.sakebook.android.sample.customtabssample.oauth.OauthActivity;
import com.sakebook.android.sample.customtabssample.session.SessionActivity;
import com.sakebook.android.sample.customtabssample.advance.AdvanceActivity;
import com.sakebook.android.sample.customtabssample.bottombar.DeprecatedBottombarActivity;
import com.sakebook.android.sample.customtabssample.prefetch.PrefetchActivity;
import com.sakebook.android.sample.customtabssample.warmup.WarmupActivity;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class LauncherActivity extends AppCompatActivity {

    private final static String HOME_URL = "https://developer.android.com/index.html";
    private final static String CUSTOM_URL = "https://httpbin.org/headers";

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.launch_button:
                    launchCustomTabs(HOME_URL);
                    return;
                case R.id.custom_toolbar_button:
                    intent = CustomToolbarActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                case R.id.animation_button:
                    intent = CustomAnimationActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                case R.id.custom_request_button:
                    intent = CustomRequestActivity.createIntent(LauncherActivity.this, CUSTOM_URL);
                    break;
                case R.id.oauth_button:
                    intent = OauthActivity.createIntent(LauncherActivity.this);
                    break;
                case R.id.remote_view_button:
                    intent = RemoteViewActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                case R.id.warmup_button:
                    intent = WarmupActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                case R.id.prefetch_button:
                    intent = PrefetchActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                case R.id.event_button:
                    intent = EventActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                case R.id.session_button:
                    intent = SessionActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                case R.id.deprecated_bottom_bar_button:
                    intent = DeprecatedBottombarActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                case R.id.session_bottom_bar_button:
                    intent = AdvanceActivity.createIntent(LauncherActivity.this);
                    break;
                case R.id.future_button:
                    intent = FutureActivity.createIntent(LauncherActivity.this, HOME_URL);
                    break;
                default:
                    intent = CustomToolbarActivity.createIntent(LauncherActivity.this, HOME_URL);
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
