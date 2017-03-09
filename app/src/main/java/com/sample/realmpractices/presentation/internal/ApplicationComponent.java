package com.sample.realmpractices.presentation.internal;

import android.content.Context;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;
import com.sample.realmpractices.domain.repository.UserRepository;
import com.sample.realmpractices.presentation.MyApplication;

/**
 * Created by app on 3/9/17.
 */

public final class ApplicationComponent extends ApplicationModule {

    public ApplicationComponent(MyApplication myApplication) {
        super(myApplication);
    }

    public Context getContext() {
        return super.provideApplicationContext();
    }

    public ThreadExecutor getThreadExecutor() {
        return super.provideThreadExecutor();
    }

    public PostExecutionThread getPostExecutionThread() {
        return super.providePostExecutionThread();
    }

    public UserHandler getUserHandler() {
        return super.provideUserHandler();
    }

    public UserRepository getUserRepository() {
        return super.provideUserRepository();
    }
}
