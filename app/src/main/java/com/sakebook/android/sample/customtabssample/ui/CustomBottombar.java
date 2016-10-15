package com.sakebook.android.sample.customtabssample.ui;

import android.graphics.Color;
import android.widget.RemoteViews;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;

/**
 * Created by sakemotoshinya on 2016/09/06.
 */
public final class CustomBottombar {

    public static RemoteViews createSessionBottombar() {
        RemoteViews remoteViews = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.remote_custom_bottom_layout);
        return remoteViews;
    }

    public static RemoteViews createBottombar() {
        RemoteViews remoteViews = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.remote_bottom_layout);
        return remoteViews;
    }


}
