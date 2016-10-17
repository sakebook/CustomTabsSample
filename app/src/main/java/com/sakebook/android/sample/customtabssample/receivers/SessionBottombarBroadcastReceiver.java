package com.sakebook.android.sample.customtabssample.receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;
import android.util.Log;

import com.sakebook.android.sample.customtabssample.R;
import com.sakebook.android.sample.customtabssample.models.SessionHelper;
import com.sakebook.android.sample.customtabssample.ui.CustomBottombar;

import java.util.Iterator;

/**
 * Created by sakemotoshinya on 2016/10/15.
 */

public class SessionBottombarBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_ADD_FAVORITE = "action_add_favorite";
    public static final String ACTION_REMOVE_FAVORITE = "action_remove_favorite";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CustomTabsSample", "SessionBottombarBroadcastReceiver onReceive");
        Log.d("CustomTabsSample", "SessionBottombarBroadcastReceiver onReceive action: " + intent.getAction());
        int clickedId = intent.getIntExtra(CustomTabsIntent.EXTRA_REMOTEVIEWS_CLICKED_ID, -1);

        CustomTabsSession session = SessionHelper.getCurrentSession();
        if (session == null) {
            Log.d("CustomTabsSample", "onReceive: session is null");
            return;
        }
        Intent broadcastIntent = new Intent(context, SessionBottombarBroadcastReceiver.class);
        PendingIntent pendingIntent;

        if (clickedId == R.id.image_favorite) {
            String action = intent.getAction();
            if (!TextUtils.isEmpty(action)) {
                if (action.equals(ACTION_ADD_FAVORITE)) {
                    broadcastIntent.setAction(ACTION_REMOVE_FAVORITE);
                    pendingIntent = PendingIntent.getBroadcast(context, 120, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    int[] ids = {R.id.image_favorite};
                    session.setSecondaryToolbarViews(CustomBottombar.createSessionBottombar(context, true), ids, pendingIntent);
                } else {
                    broadcastIntent.setAction(ACTION_ADD_FAVORITE);
                    pendingIntent = PendingIntent.getBroadcast(context, 120, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    int[] ids = {R.id.image_favorite};
                    session.setSecondaryToolbarViews(CustomBottombar.createSessionBottombar(context, false), ids, pendingIntent);
                }
            }
        }

        Bundle extras = intent.getExtras();
        if (extras != null) {
            Iterator<?> it = extras.keySet().iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                Log.d("CustomTabsSample", "onReceive: SessionBottombarBroadcastReceiver: key    : " + key);
                Log.d("CustomTabsSample", "onReceive: SessionBottombarBroadcastReceiver: key get: " + extras.get(key));
            }
        } else {
            Log.d("CustomTabsSample", "onReceive: SessionBottombarBroadcastReceiver: extras null");
        }
    }
}
