package com.sakebook.android.sample.customtabssample.ui;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.utils.CustomTabsUtil;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

public class DeprecatedBottombarActivity extends AppCompatActivity {

    private final static String URL_ARGS = "url";
    private String url = "";

    public static Intent createIntent(Context context, String url) {
        Intent intent = new Intent(context, DeprecatedBottombarActivity.class);
        intent.putExtra(URL_ARGS, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deprecated_bottombar);
        url = getIntent().getStringExtra(URL_ARGS);

        findViewById(R.id.launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCustomTabs(url);
            }
        });
    }

    private void launchCustomTabs(String url) {
        String packageName = CustomTabsHelper.getPackageNameToUse(this);
        if (TextUtils.isEmpty(packageName)) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        addDeprecatedToolbar(builder, url);

        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(this, Uri.parse(url));
    }

    private void addDeprecatedToolbar(CustomTabsIntent.Builder builder, String url) {
        Intent browseIntent = new Intent();
        browseIntent.setAction(Intent.ACTION_VIEW);
        browseIntent.setData(Uri.parse(url));
        PendingIntent pendingBrowseIntent = PendingIntent.getActivity(this, 100, browseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_open_in_browser_black);
        builder.addToolbarItem(1, icon, "view", pendingBrowseIntent);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        PendingIntent pendingShareIntent = PendingIntent.getActivity(this, 101, shareIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        Bitmap icons = BitmapFactory.decodeResource(getResources(), R.drawable.ic_share_black);
        builder.addToolbarItem(2, icons, "share", pendingShareIntent);
    }

}
