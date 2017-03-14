package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.data.net.WebServiceApi;
import com.sample.realmpractices.data.repository.transaction.DatabaseTransaction;
import com.sample.realmpractices.data.repository.transaction.PutListUserEntitySubscriber;
import com.sample.realmpractices.data.repository.transaction.PutListUserEntityTransaction;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

class UserSavedDataSource implements UserDataSource {
    private final WebServiceApi webServiceApi;
    private final UserHandler userHandler;
    private final DatabaseTransaction databaseTransaction;

    UserSavedDataSource(UserHandler userHandler,
                        WebServiceApi webServiceApi,
                        DatabaseTransaction databaseTransaction) {
        this.userHandler = userHandler;
        this.webServiceApi = webServiceApi;
        this.databaseTransaction = databaseTransaction;
    }

    @Override
    public Observable<List<UserEntity>> userEntities() {
        Observable<List<UserEntity>> listObservable = webServiceApi.getAllUsers();
        new PutListUserEntityTransaction(
                    databaseTransaction.getPostExecutionThread(), databaseTransaction.getThreadExecutor()
                )
                .setTransaction(listObservable)
                .execute(new PutListUserEntitySubscriber(userHandler));
        return listObservable;
    }

    @Override
    public Observable<UserEntity> entityDetail(int userId) {
        return userHandler.get(userId);
    }

    @Override
    public void deleteUser(int userId) {
        userHandler.deleteUser(userId);
    }
}
