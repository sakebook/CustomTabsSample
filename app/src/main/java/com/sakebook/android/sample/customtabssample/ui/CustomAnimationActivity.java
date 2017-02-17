package com.sakebook.android.sample.customtabssample.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sakebook.android.sample.customtabssample.LauncherActivity;
import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

public class CustomAnimationActivity extends AppCompatActivity {

    private final static String URL_ARGS = "url";
    private String url = "";

    public static Intent createActivity(Context context, String url) {
        Intent intent = new Intent(context, CustomAnimationActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_animation);
        url = getIntent().getStringExtra(URL_ARGS);

        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomTabsUtil.launchCustomTabs(CustomAnimationActivity.this, url);
            }
        });
    }
}
