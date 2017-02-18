package com.sakebook.android.sample.customtabssample;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.sakebook.android.sample.customtabssample.ui.BottombarActivity;
import com.sakebook.android.sample.customtabssample.ui.CustomAnimationActivity;
import com.sakebook.android.sample.customtabssample.ui.SessionBottombarActivity;
import com.sakebook.android.sample.customtabssample.ui.DeprecatedBottombarActivity;
import com.sakebook.android.sample.customtabssample.ui.PrefetchActivity;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

public class LauncherActivity extends AppCompatActivity {

    private final static String URL = "https://developer.android.com/index.html";

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.launch_button:
                    CustomTabsUtil.launchCustomTabs(LauncherActivity.this, URL);
                    break;
                case R.id.prefetch_button:
                    startPrefetchActivity();
                    break;
                case R.id.animation_button:
                    startCustomAnimationActivity();
                    break;
                case R.id.deprecated_bottombar_button:
                    startDeprecatedBottombarActivity();
                    break;
                case R.id.bottombar_button:
                    startBottombarActivity();
                    break;
                case R.id.session_bottombar_button:
                    startSessionBottombarActivity();
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
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layout_parent);
        int viewCount = viewGroup.getChildCount();
        for (int i = 0; i < viewCount; i++) {
            viewGroup.getChildAt(i).setOnClickListener(onClickListener);
        }
    }

    private void startPrefetchActivity() {
        Intent intent = PrefetchActivity.createActivity(this, URL);
        startActivity(intent);
    }

    private void startCustomAnimationActivity() {
        Intent intent = CustomAnimationActivity.createActivity(this, URL);
        startActivity(intent);
    }

    private void startDeprecatedBottombarActivity() {
        Intent intent = DeprecatedBottombarActivity.createActivity(this, URL);
        startActivity(intent);
    }

    private void startBottombarActivity() {
        Intent intent = BottombarActivity.createActivity(this, URL);
        startActivity(intent);
    }

    private void startSessionBottombarActivity() {
        Intent intent = SessionBottombarActivity.createActivity(this);
        startActivity(intent);
    }
}
