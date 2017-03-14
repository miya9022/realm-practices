package com.sample.realmpractices.data.repository;

import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.data.entity.mapping.UserEntityDataMapping;
import com.sample.realmpractices.data.repository.datasource.UserDataSource;
import com.sample.realmpractices.data.repository.datasource.UserDataSourceFactory;
import com.sample.realmpractices.domain.repository.UserRepository;
import com.sample.realmpractices.domain.User;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

public class UserDataRepository implements UserRepository {

    private final UserDataSourceFactory userDataSourceFactory;
    private final UserEntityDataMapping userEntityDataMapping;

    public UserDataRepository(UserDataSourceFactory userDataSourceFactory, UserEntityDataMapping userEntityDataMapping) {
        this.userDataSourceFactory = userDataSourceFactory;
        this.userEntityDataMapping = userEntityDataMapping;
    }

    @Override
    public Observable<List<User>> users() {
        final UserDataSource userDataSource = userDataSourceFactory.createDataSource();
        return userDataSource.userEntities().map(userEntityDataMapping::replicateUserListFromRealm);
    }

    @Override
    public Observable<User> user(int uid) {
        final UserDataSource userDataSource = userDataSourceFactory.createDataSource();
        return userDataSource.entityDetail(uid).map(userEntityDataMapping::replicateUserFromRealm);
    }

    @Override
    public int deleteUser(int uid) {
        final UserDataSource userDataSource = userDataSourceFactory.createDataSource();
        userDataSource.deleteUser(uid);
        return uid;
    }

    @Override
    public Observable<User> insertUser(UserEntity userEntity) {
        final UserDataSource userDataSource = userDataSourceFactory.createDataSource();
        return userDataSource.insertUser(userEntity).map(userEntityDataMapping::replicateUserFromRealm);
    }
}
