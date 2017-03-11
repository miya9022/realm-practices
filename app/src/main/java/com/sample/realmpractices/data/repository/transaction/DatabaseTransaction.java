package com.sample.realmpractices.data.repository.transaction;

import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by app on 3/10/17.
 */

public abstract class DatabaseTransaction<T> {
    private PostExecutionThread postExecutionThread;
    private ThreadExecutor threadExecutor;
    private Subscription subscription = Subscriptions.empty();

    public DatabaseTransaction(PostExecutionThread postExecutionThread, ThreadExecutor threadExecutor) {
        this.postExecutionThread = postExecutionThread;
        this.threadExecutor = threadExecutor;
    }

    abstract Observable<T> transactionObservable();

    public void execute(Subscriber<T> useCaseSubscriber) {
        Observable<T> transaction = transactionObservable();
        if(transaction != null) {
            this.subscription = transaction
                    .subscribeOn(Schedulers.from(threadExecutor))
                    .observeOn(postExecutionThread.getScheduler())
                    .subscribe(useCaseSubscriber);
        }
    }

    public void unsubscribe() {
        if(!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
