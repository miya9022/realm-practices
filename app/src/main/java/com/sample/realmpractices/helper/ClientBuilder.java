package com.sample.realmpractices.helper;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sample.realmpractices.model.User;
import com.sample.realmpractices.util.Params;

import java.util.List;

import io.realm.RealmObject;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by app on 2/13/17.
 */

public final class ClientBuilder {

    private static Gson createCustomGsonForRealmObject() {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
    }

    public static WebService createDefaultWebservice() {
        return new Retrofit.Builder()
                .baseUrl(Params.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(createCustomGsonForRealmObject()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(WebService.class);
    }

    public interface WebService {
        @GET("user.json")
        Observable<List<User>> getAllUsers();
    }
}
