package com.sample.realmpractices.data.repository.transaction;

import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/11/17.
 */

public class PutListUserEntityTransaction extends DatabaseTransaction<List<UserEntity>> {

    private Observable<List<UserEntity>> transaction;

    public PutListUserEntityTransaction(PostExecutionThread postExecutionThread, ThreadExecutor threadExecutor) {
        super(postExecutionThread, threadExecutor);
    }

    public PutListUserEntityTransaction setTransaction(Observable<List<UserEntity>> transaction) {
        this.transaction = transaction;
        return this;
    }

    @Override
    Observable<List<UserEntity>> transactionObservable() {
        return transaction;
    }
}
