package com.sample.realmpractices.presentation.internal;

import android.content.Context;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.database.UserHandlerImpl;
import com.sample.realmpractices.data.entity.mapping.EmailEntityDataMapping;
import com.sample.realmpractices.data.entity.mapping.UserEntityDataMapping;
import com.sample.realmpractices.data.executor.JobExecutor;
import com.sample.realmpractices.data.repository.EmailDataRepository;
import com.sample.realmpractices.data.repository.UserDataRepository;
import com.sample.realmpractices.data.repository.datasource.EmailDataSourceFactory;
import com.sample.realmpractices.data.repository.datasource.UserDataSourceFactory;
import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;
import com.sample.realmpractices.domain.repository.EmailRepository;
import com.sample.realmpractices.domain.repository.UserRepository;
import com.sample.realmpractices.presentation.MyApplication;
import com.sample.realmpractices.presentation.UIThread;

/**
 * Created by app on 3/9/17.
 */

abstract class ApplicationModule {

    private final MyApplication application;

    ApplicationModule(MyApplication myApplication) {
        this.application = myApplication;
    }

    Context provideApplicationContext() {
        return application;
    }

    ThreadExecutor provideThreadExecutor() {
        return new JobExecutor();
    }

    PostExecutionThread providePostExecutionThread() {
        return new UIThread();
    }

    UserHandler provideUserHandler() {
        return new UserHandlerImpl();
    }

    UserRepository provideUserRepository() {
        return new UserDataRepository(new UserDataSourceFactory(provideUserHandler()), new UserEntityDataMapping());
    }

    EmailRepository provideEmailRepository() {
        return new EmailDataRepository(new EmailDataSourceFactory(provideUserHandler()), new EmailEntityDataMapping());
    }
}
