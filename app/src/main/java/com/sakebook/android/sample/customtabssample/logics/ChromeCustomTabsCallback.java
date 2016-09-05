package com.sakebook.android.sample.customtabssample.logics;

import android.os.Bundle;
import android.support.customtabs.CustomTabsCallback;

/**
 * Created by sakemotoshinya on 2016/09/03.
 */
public interface ChromeCustomTabsCallback  {
    void onNavigationEvent(int navigationEvent, Bundle extras);
    void extraCallback(String callbackName, Bundle args);
}
