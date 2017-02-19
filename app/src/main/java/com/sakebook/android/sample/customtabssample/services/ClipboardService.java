package com.sakebook.android.sample.customtabssample.services;

import android.app.IntentService;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.sakebook.android.sample.customtabssample.R;

public class ClipboardService extends IntentService {

    private Handler handler;

    public ClipboardService() {
        super("ClipboardService");
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String url = intent.getDataString();
            if (!TextUtils.isEmpty(url)) {
                ClipboardManager clipboard = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("", url);
                clipboard.setPrimaryClip(clipData);
                showMessage(getString(R.string.customize_toolbar_curl_copy, url));
            } else {
                showMessage(getString(R.string.customize_toolbar_curl_copy_error));
            }
        }
    }

    private void showMessage(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ClipboardService.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
