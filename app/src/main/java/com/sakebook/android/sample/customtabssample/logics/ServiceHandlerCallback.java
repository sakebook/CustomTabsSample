package com.sakebook.android.sample.customtabssample.logics;

/**
 * Created by sakemotoshinya on 2016/09/03.
 */
public interface ServiceHandlerCallback {
    void connectedFail();
    void connected();
    void disconnected();
}
