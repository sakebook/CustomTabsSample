package com.sakebook.android.sample.customtabssample.remoteview;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.sakebook.android.sample.customtabssample.R;

/**
 * Created by sakemotoshinya on 2016/09/04.
 */
public class BottombarBroadcastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CustomTabsSample", "BottombarBroadcastReceiver onReceive");
        int clickedId = intent.getIntExtra(CustomTabsIntent.EXTRA_REMOTEVIEWS_CLICKED_ID, -1);
        String url = intent.getDataString();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(context, "Failed share.", Toast.LENGTH_LONG).show();
            return;
        }

        switch (clickedId) {
            case R.id.facebook:
            case R.id.twitter:
            case R.id.line:
                share(context, url, clickedId);
                break;
            default:
                break;
        }
    }

    private void share(Context context, String text, int clickId) {
        try {
            switch (clickId) {
                case R.id.facebook:
                    shareFacebook(context, text);
                    break;
                case R.id.twitter:
                    shareTwitter(context, text);
                    break;
                case R.id.line:
                    shareLINE(context, text);
                    break;
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, "Official client app not found.", Toast.LENGTH_LONG).show();
        }
    }

    private void shareTwitter(Context context, String text) throws ActivityNotFoundException{
        Intent shareIntent = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, text)
                .setPackage("com.twitter.android")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ;
        context.startActivity(shareIntent);
    }

    /**
     * Only work url
     * reference: https://stackoverflow.com/questions/34618514/share-text-via-intent-on-facebook-without-using-facebook-sdk
     * */
    private void shareFacebook(Context context, String url) throws ActivityNotFoundException{
        Intent shareIntent = new Intent(Intent.ACTION_SEND)
                .setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, url)
                .setPackage("com.facebook.katana")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ;
        context.startActivity(shareIntent);
    }

    private void shareLINE(Context context, String text) throws ActivityNotFoundException{
        Intent shareIntent = new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse("line://msg/text/" + text))
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                ;
        context.startActivity(shareIntent);
    }
}
