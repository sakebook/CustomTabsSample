package com.sakebook.android.sample.customtabssample.utils;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.advance.SessionBottombarBroadcastReceiver;
import com.sakebook.android.sample.customtabssample.advance.CustomBottombar;

import org.chromium.customtabsclient.shared.CustomTabsHelper;

/**
 * Created by sakemotoshinya on 16/07/28.
 */
public class CustomTabsUtil {

    public static void launchCustomTabsWithSessionBottombar(Activity activity, String url, @Nullable CustomTabsSession session) {
        String packageName = CustomTabsHelper.getPackageNameToUse(activity);
        if (TextUtils.isEmpty(packageName)) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }
        CustomTabsIntent.Builder builder;
        if (session == null) {
            builder = new CustomTabsIntent.Builder();
        } else {
            builder = new CustomTabsIntent.Builder(session);
        }
        addSessionBottombar(builder, activity, url);

        CustomTabsIntent customTabsIntent = builder.build();
        CustomTabsHelper.addKeepAliveExtra(activity, customTabsIntent.intent);
        customTabsIntent.intent.setPackage(packageName);
        customTabsIntent.launchUrl(activity, Uri.parse(url));
    }

    private static void addSessionBottombar(CustomTabsIntent.Builder builder, Activity activity, String url) {
        Intent broadcastIntent = new Intent(activity, SessionBottombarBroadcastReceiver.class);
        broadcastIntent.setAction(SessionBottombarBroadcastReceiver.ACTION_ADD_FAVORITE);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 120, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        int[] ids = {R.id.image_favorite};
        builder.setSecondaryToolbarViews(CustomBottombar.createSessionBottombar(activity, false), ids, pendingIntent);
        builder.setShowTitle(true);
        builder.addDefaultShareMenuItem();
    }
}
