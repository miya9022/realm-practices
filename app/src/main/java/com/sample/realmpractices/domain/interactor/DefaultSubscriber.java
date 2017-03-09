package com.sample.realmpractices.domain.interactor;

import rx.Subscriber;

/**
 * Created by app on 3/7/17.
 */

public abstract class DefaultSubscriber<T> extends Subscriber<T> {

    @Override
    public abstract void onCompleted();

    @Override
    public abstract void onError(Throwable e);

    @Override
    public abstract void onNext(T t);
}
