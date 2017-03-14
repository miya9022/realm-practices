package com.sample.realmpractices.data.database;

import com.sample.realmpractices.data.entity.EmailEntity;
import com.sample.realmpractices.data.entity.UserEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

public class UserHandlerImpl implements UserHandler {

    private RealmProvider realmProvider;

    public UserHandlerImpl() {
        realmProvider = new RealmProvider();
        realmProvider.setupRealmConfig();
    }

    @Override
    public Observable<UserEntity> get(int userId) {
        return realmProvider.getRealmObjectById(UserEntity.class, "id", userId).asObservable();
    }

    @Override
    public Observable<UserEntity> put(UserEntity userEntity) {
        return Observable.just((UserEntity) realmProvider.insert(userEntity));
    }

    @Override
    public void deleteUser(final int uid) {
        realmProvider.deleteById(UserEntity.class, uid);
    }

    @Override
    public List<EmailEntity> getEmailsByUserId(int userId) {
        return ((UserEntity) realmProvider.getRealmObjectById(UserEntity.class, "id", userId)).getEmails();
    }
}
