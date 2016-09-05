package com.sakebook.android.sample.customtabssample.logics;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsCallback;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsSession;
import android.text.TextUtils;
import android.util.Log;

import com.sakebook.android.sample.customtabssample.models.SessionHelper;

import org.chromium.customtabsclient.shared.CustomTabsHelper;
import org.chromium.customtabsclient.shared.ServiceConnection;
import org.chromium.customtabsclient.shared.ServiceConnectionCallback;

/**
 * Created by sakemotoshinya on 2016/09/03.
 */
public class ServiceHandler implements ServiceConnectionCallback{

    private Context context;
    private CustomTabsClient customTabsClient;
    private ServiceConnection connection;
    private ServiceHandlerCallback serviceHandlerCallback;
    @Nullable
    private ChromeCustomTabsCallback chromeCustomTabsCallback;

    public ServiceHandler(Context context, ServiceHandlerCallback serviceHandlerCallback, @Nullable ChromeCustomTabsCallback chromeCustomTabsCallback) {
        this.context = context;
        this.serviceHandlerCallback = serviceHandlerCallback;
        this.chromeCustomTabsCallback = chromeCustomTabsCallback;
    }

    public void connect() {
        if (customTabsClient != null) {
            // already connected
            return;
        }
        String connectChromePackageName = CustomTabsHelper.getPackageNameToUse(context);
        if (TextUtils.isEmpty(connectChromePackageName)) {
            serviceHandlerCallback.connectedFail();
            return;
        }
        connection = new ServiceConnection(this);
        CustomTabsClient.bindCustomTabsService(context, connectChromePackageName, connection);
    }

    public void disconnect() {
        if (connection == null) {
            // Unnecessary disconnect
            return;
        }
        customTabsClient = null;
        Log.d("CustomTabsSample", "try disconnect");
        context.unbindService(connection);
    }

    public CustomTabsSession getSession() {
        CustomTabsSession session = SessionHelper.getCurrentSession();
        if (session != null) {
            return session;
        }
        if (chromeCustomTabsCallback != null) {
            session = customTabsClient.newSession(new CustomTabsEventCallback(chromeCustomTabsCallback));
        } else {
            session = customTabsClient.newSession(null);
        }
        SessionHelper.setCurrentSession(session);
        return session;
    }

    @Override
    public void onServiceConnected(CustomTabsClient client) {
        customTabsClient = client;
        customTabsClient.warmup(0L);
        serviceHandlerCallback.connected();
    }

    @Override
    public void onServiceDisconnected() {
        customTabsClient = null;
        connection = null;
        chromeCustomTabsCallback = null;
        serviceHandlerCallback.disconnected();
    }

    private class CustomTabsEventCallback extends CustomTabsCallback {

        private ChromeCustomTabsCallback chromeCustomTabsCallback;

        public CustomTabsEventCallback(ChromeCustomTabsCallback chromeCustomTabsCallback) {
            this.chromeCustomTabsCallback = chromeCustomTabsCallback;
        }

        @Override
        public void onNavigationEvent(int navigationEvent, Bundle extras) {
            String eventName = "";
            switch (navigationEvent) {
                case 1:
                    eventName = "NAVIGATION_STARTED";
                    break;
                case 2:
                    eventName = "NAVIGATION_FINISHED";
                    break;
                case 3:
                    eventName = "NAVIGATION_FAILED";
                    break;
                case 4:
                    eventName = "NAVIGATION_ABORTED";
                    break;
                case 5:
                    eventName = "TAB_SHOWN";
                    break;
                case 6:
                    eventName = "TAB_HIDDEN";
                    break;
            }
            Log.d("CustomTabsSample", "onNavigationEvent: " + eventName);
            super.onNavigationEvent(navigationEvent, extras);
            this.chromeCustomTabsCallback.onNavigationEvent(navigationEvent, extras);
        }

        @Override
        public void extraCallback(String callbackName, Bundle args) {
            Log.d("CustomTabsSample", "extraCallback " + callbackName);
            super.extraCallback(callbackName, args);
            this.chromeCustomTabsCallback.extraCallback(callbackName, args);
        }
    }
}
