package com.sample.realmpractices.data.database;

import com.sample.realmpractices.data.entity.EmailEntity;
import com.sample.realmpractices.data.entity.UserEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/9/17.
 */

public interface UserHandler {

    Observable<UserEntity> get(final int userId);

    Observable<UserEntity> put(UserEntity userEntity);

    void deleteUser(final int uid);

    List<EmailEntity> getEmailsByUserId(final int userId);
}
