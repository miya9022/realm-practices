package com.sample.realmpractices;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by app on 2/13/17.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
