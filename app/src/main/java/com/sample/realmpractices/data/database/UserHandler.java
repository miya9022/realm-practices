package com.sample.realmpractices.data.database;

import com.sample.realmpractices.data.entity.UserEntity;

import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

public interface UserHandler {

    Observable<UserEntity> get(final int userId);

    void put(UserEntity userEntity);
}
