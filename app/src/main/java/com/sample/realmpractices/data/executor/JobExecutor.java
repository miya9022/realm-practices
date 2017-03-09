package com.sample.realmpractices.data.executor;

import android.support.annotation.NonNull;

import com.sample.realmpractices.domain.executor.ThreadExecutor;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by app on 3/9/17.
 */

public class JobExecutor implements ThreadExecutor {
    private final ThreadPoolExecutor threadPoolExecutor;

    public JobExecutor() {
        this.threadPoolExecutor = new ThreadPoolExecutor(3, 5, 10,
                TimeUnit.SECONDS, new LinkedBlockingDeque<>(), new JobThreadFactory());
    }

    @Override
    public void execute(Runnable command) {
        this.threadPoolExecutor.execute(command);
    }

    private static class JobThreadFactory implements ThreadFactory {

        private int counter = 0;

        @Override
        public Thread newThread(@NonNull Runnable r) {
            return new Thread(r, "android_" + counter++);
        }
    }
}
