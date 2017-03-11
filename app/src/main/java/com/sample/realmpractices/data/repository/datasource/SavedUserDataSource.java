package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.data.net.WebServiceApi;
import com.sample.realmpractices.data.repository.transaction.PutListUserEntitySubscriber;
import com.sample.realmpractices.data.repository.transaction.PutListUserEntityTransaction;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

class SavedUserDataSource implements UserDataSource {
    private final WebServiceApi webServiceApi;
    private final UserHandler userHandler;
    private final PutListUserEntityTransaction putListUserEntityTransaction;

    SavedUserDataSource(UserHandler userHandler,
                        WebServiceApi webServiceApi,
                        PutListUserEntityTransaction putListUserEntityTransaction) {
        this.userHandler = userHandler;
        this.webServiceApi = webServiceApi;
        this.putListUserEntityTransaction = putListUserEntityTransaction;
    }

    @Override
    public Observable<List<UserEntity>> userEntities() {
        Observable<List<UserEntity>> listObservable = webServiceApi.getAllUsers();
        putListUserEntityTransaction
                .setTransaction(listObservable)
                .execute(new PutListUserEntitySubscriber(userHandler));
        return listObservable;
    }

    @Override
    public Observable<UserEntity> entityDetail(int userId) {
        return userHandler.get(userId);
    }
}
