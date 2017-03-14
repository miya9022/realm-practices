package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.entity.UserEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

public interface UserDataSource {
    Observable<List<UserEntity>> userEntities();

    Observable<UserEntity> entityDetail(final int userId);

    void deleteUser(int userId);

    Observable<UserEntity> insertUser(UserEntity userEntity);
}
