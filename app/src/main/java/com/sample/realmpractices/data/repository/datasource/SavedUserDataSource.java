package com.sample.realmpractices.data.repository.datasource;

import com.annimon.stream.Stream;
import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.data.net.WebServiceApi;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by app on 3/9/17.
 */

class SavedUserDataSource implements UserDataSource {
    private final WebServiceApi webServiceApi;
    private final UserHandler userHandler;

    SavedUserDataSource(UserHandler userHandler, WebServiceApi webServiceApi) {
        this.userHandler = userHandler;
        this.webServiceApi = webServiceApi;
    }

    @Override
    public Observable<List<UserEntity>> userEntities() {
        Observable<List<UserEntity>> listObservable = webServiceApi.getAllUsers();
        listObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::insertDataToRealm);
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
