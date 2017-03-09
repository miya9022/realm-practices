package com.sample.realmpractices.data.database;

import com.sample.realmpractices.data.entity.UserEntity;

import io.realm.Realm;
import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

public class UserHandlerImpl implements UserHandler {

    private RealmProvider realmProvider;

    public UserHandlerImpl() {
        realmProvider = new RealmProvider();
        if (Realm.getDefaultInstance() == null) {
            realmProvider.setupRealmConfig();
        }
    }

    @Override
    public Observable<UserEntity> get(int userId) {
        return realmProvider.getRealmObjectById(UserEntity.class, "id", userId).asObservable();
    }

    @Override
    public void put(UserEntity userEntity) {
        realmProvider.insert(userEntity);
    }
}
