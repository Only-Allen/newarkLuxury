package com.avatar.newarkluxury;

import android.app.Application;

/**
 * Created by chx on 2016/12/13.
 */

public class NewArkApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
