package com.sakebook.android.sample.customtabssample;

import android.content.Intent;
import android.support.customtabs.CustomTabsClient;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sakebook.android.sample.customtabssample.ui.BottombarActivity;
import com.sakebook.android.sample.customtabssample.ui.PrefetchActivity;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class LauncherActivity extends AppCompatActivity {

    private final static String URL = "https://developer.android.com/index.html";
//    private final static String URL = "https://google.com";

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.launch_button:
                    CustomTabsUtil.launchCustomTabs(LauncherActivity.this, URL, null);
                    break;
                case R.id.warm_up_button:
                    if (view.getTag() == null) {
                        boolean success = CustomTabsClient.connectAndInitialize(LauncherActivity.this,
                                CustomTabsHelper.getPackageNameToUse(LauncherActivity.this));
                        view.setTag(success);
                        Toast.makeText(LauncherActivity.this, "Warm up now. Tap again.", Toast.LENGTH_SHORT).show();
                    } else {
                        view.setTag(null);
                        CustomTabsUtil.launchCustomTabs(LauncherActivity.this, URL, null);
                    }
                    break;
                case R.id.prefetch_button:
                    startPrefetchActivity();
                    break;
                case R.id.bottombar_button:
                    startBottombarActivity();
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initLayout();
    }

    private void initLayout() {
        findViewById(R.id.launch_button).setOnClickListener(onClickListener);
        findViewById(R.id.warm_up_button).setOnClickListener(onClickListener);
        findViewById(R.id.prefetch_button).setOnClickListener(onClickListener);
        findViewById(R.id.bottombar_button).setOnClickListener(onClickListener);
    }

    private void startPrefetchActivity() {
        Intent intent = PrefetchActivity.createActivity(this);
        startActivity(intent);
    }

    private void startBottombarActivity() {
        Intent intent = BottombarActivity.createActivity(this);
        startActivity(intent);
    }
}
