package com.sample.realmpractices.domain.repository;

import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.domain.User;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/7/17.
 */

public interface UserRepository {

    Observable<List<User>> users();

    Observable<User> user(final int uid);

    int deleteUser(final int uid);

    Observable<User> insertUser(UserEntity userEntity);
}
