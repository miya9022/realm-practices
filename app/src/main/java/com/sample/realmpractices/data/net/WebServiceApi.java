package com.sample.realmpractices.data.net;

import com.sample.realmpractices.data.entity.UserEntity;

import java.util.List;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by app on 3/8/17.
 */

public interface WebServiceApi {
    @GET("user.json")
    Observable<List<UserEntity>> getAllUsers();
}
