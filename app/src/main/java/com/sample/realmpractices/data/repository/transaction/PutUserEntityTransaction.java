package com.sample.realmpractices.data.repository.transaction;

import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;

import rx.Observable;

/**
 * Created by app on 3/11/17.
 */

public class PutUserEntityTransaction extends DatabaseTransaction {

    private Observable transaction;

    public PutUserEntityTransaction(PostExecutionThread postExecutionThread, ThreadExecutor threadExecutor) {
        super(postExecutionThread, threadExecutor);
    }

    public PutUserEntityTransaction setTransaction(Observable transaction) {
        this.transaction = transaction;
        return this;
    }

    @Override
    Observable transactionObservable() {
        return transaction;
    }
}
