package com.sample.realmpractices.domain.interactor;

import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;
import rx.subscriptions.Subscriptions;

/**
 * Created by app on 3/7/17.
 */

public abstract class UseCase<T> {

    private PostExecutionThread postExecutionThread;
    private ThreadExecutor threadExecutor;

    private Subscription subscription = Subscriptions.empty();

    protected UseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    protected abstract Observable<T> buildUseCaseObservable();

    public void execute(Subscriber<T> useCaseSubscriber) {
        this.subscription = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler())
                .subscribe(useCaseSubscriber);
    }

    public void unsubscribe() {
        if(!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
