package com.sample.realmpractices.data.net;

import com.sample.realmpractices.data.entity.UserEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/8/17.
 */

public class WebServiceApiImpl implements WebServiceApi {

    @Override
    public Observable<List<UserEntity>> getAllUsers() {
        return WebServiceApiConnection.createDefaultWebServiceApi().getAllUsers();
    }
}
