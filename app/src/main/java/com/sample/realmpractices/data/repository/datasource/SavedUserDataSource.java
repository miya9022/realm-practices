package com.sample.realmpractices.data.repository.datasource;

import com.annimon.stream.Stream;
import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.data.net.WebServiceApi;
import com.sample.realmpractices.data.repository.transaction.ListUserEntitySubscriber;
import com.sample.realmpractices.data.repository.transaction.PutUserEntityTransaction;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

class SavedUserDataSource implements UserDataSource {
    private final WebServiceApi webServiceApi;
    private final UserHandler userHandler;
    private final PutUserEntityTransaction putUserEntityTransaction;

    SavedUserDataSource(UserHandler userHandler,
                        WebServiceApi webServiceApi,
                        PutUserEntityTransaction putUserEntityTransaction) {
        this.userHandler = userHandler;
        this.webServiceApi = webServiceApi;
        this.putUserEntityTransaction = putUserEntityTransaction;
    }

    @Override
    public Observable<List<UserEntity>> userEntities() {
        Observable<List<UserEntity>> listObservable = webServiceApi.getAllUsers();
        putUserEntityTransaction
                .setTransaction(listObservable)
                .execute(new ListUserEntitySubscriber(userHandler));
        return listObservable;
    }

    private void insertDataToRealm(List<UserEntity> userEntities) {
        Stream.of(userEntities).forEach(userHandler::put);
    }

    @Override
    public Observable<UserEntity> entityDetail(int userId) {
        return userHandler.get(userId);
    }
}
