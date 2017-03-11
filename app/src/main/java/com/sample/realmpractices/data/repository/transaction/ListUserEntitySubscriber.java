package com.sample.realmpractices.data.repository.transaction;

import com.annimon.stream.Stream;
import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.domain.interactor.DefaultSubscriber;

import java.util.List;

/**
 * Created by app on 3/11/17.
 */

public class ListUserEntitySubscriber extends DefaultSubscriber<List<UserEntity>> {

    private final UserHandler userHandler;

    public ListUserEntitySubscriber(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(List<UserEntity> userEntities) {
        if (userHandler != null) {
            Stream.of(userEntities).forEach(userHandler::put);
        }
    }
}
