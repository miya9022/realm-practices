package com.sample.realmpractices.presentation;

import android.app.Application;

import com.sample.realmpractices.presentation.internal.ApplicationComponent;

import io.realm.Realm;

/**
 * Created by app on 2/13/17.
 */

public class MyApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        applicationComponent = new ApplicationComponent(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
