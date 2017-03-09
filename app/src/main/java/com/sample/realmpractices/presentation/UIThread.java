package com.sample.realmpractices.presentation;

import com.sample.realmpractices.domain.executor.PostExecutionThread;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by app on 3/9/17.
 */

public class UIThread implements PostExecutionThread {

    @Override
    public Scheduler getScheduler() {
        return AndroidSchedulers.mainThread();
    }
}
