package com.sample.realmpractices.domain.executor;

import rx.Scheduler;

/**
 * Created by app on 3/7/17.
 */

public interface PostExecutionThread {
    Scheduler getScheduler();
}
