package com.sakebook.android.sample.customtabssample.animation;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sakebook.android.sample.customtabssample.R;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class CustomAnimationActivity extends AppCompatActivity {

    private final static String URL_ARGS = "url";
    private String url = "";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, CustomAnimationActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_animation);
        url = getIntent().getStringExtra(URL_ARGS);

        findViewById(R.id.button_animation_slide_in).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabsWithSlide(url);
            }
        });
        findViewById(R.id.button_animation_modal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabsWithModal(url);
            }
        });
    }

    private void launchCustomTabsWithSlide(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }

        CustomTabsIntent.Builder builder =
                new CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setStartAnimations(this, R.anim.customtabs_in_slide_anim, R.anim.application_out_slide_anim)
                        .setExitAnimations(this, R.anim.application_in_slide_anim, R.anim.customtabs_out_slide_anim)
                ;

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void launchCustomTabsWithModal(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            return;
        }

        CustomTabsIntent.Builder builder =
                new CustomTabsIntent.Builder()
                        .setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
                        .setStartAnimations(this, R.anim.customtabs_in_modal_anim, R.anim.application_out_modal_anim)
                        .setExitAnimations(this, R.anim.application_in_modal_anim, R.anim.customtabs_out_modal_anim)
                ;

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }
}
