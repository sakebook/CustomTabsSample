package com.sakebook.android.sample.customtabssample.receivers;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.sakebook.android.sample.customtabssample.R;

public class ShareBroadcastReceiver extends BroadcastReceiver {
    public ShareBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String url = intent.getDataString();
            if (!TextUtils.isEmpty(url)) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, url);
                context.startActivity(shareIntent);
            } else {
            }
        }

    }
}
