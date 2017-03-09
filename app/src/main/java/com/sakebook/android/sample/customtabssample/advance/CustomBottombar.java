package com.sakebook.android.sample.customtabssample.advance;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.sakebook.android.sample.customtabssample.BuildConfig;
import com.sakebook.android.sample.customtabssample.R;

/**
 * Created by sakemotoshinya on 2016/09/06.
 */
public final class CustomBottombar {

    public static RemoteViews createSessionBottombar(Context context, boolean shouldAddFavorite) {
        RemoteViews remoteViews = new RemoteViews(BuildConfig.APPLICATION_ID, R.layout.remote_custom_bottom_layout);
        if (shouldAddFavorite) {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_favorite_white_36dp);
            remoteViews.setImageViewBitmap(R.id.image_favorite, bitmap);
//            remoteViews.setImageViewResource(R.id.image_favorite, R.drawable.ic_favorite_white_36dp);
            remoteViews.setTextViewText(R.id.text_description, context.getResources().getString(R.string.favorite_after_text));
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_favorite_border_white_36dp);
            remoteViews.setImageViewBitmap(R.id.image_favorite, bitmap);
//            remoteViews.setImageViewResource(R.id.image_favorite, R.drawable.ic_favorite_border_white_36dp);
            remoteViews.setTextViewText(R.id.text_description, context.getResources().getString(R.string.favorite_before_text));
        }
        return remoteViews;
    }
}
