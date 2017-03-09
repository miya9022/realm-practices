package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.data.net.WebServiceApi;

import java.util.List;

import rx.Observable;

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
        return webServiceApi.getAllUsers();
    }

    @Override
    public Observable<UserEntity> entityDetail(int userId) {
        return userHandler.get(userId);
    }
}
