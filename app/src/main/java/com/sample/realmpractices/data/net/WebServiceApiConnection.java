package com.sample.realmpractices.data.net;

import com.sample.realmpractices.data.entity.mapping.GsonRealmBuilder;
import com.sample.realmpractices.util.Params;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by app on 3/8/17.
 */

class WebServiceApiConnection {

    static WebServiceApi createDefaultWebServiceApi() {
        return new Retrofit.Builder()
                .baseUrl(Params.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonRealmBuilder().createCustomGsonForRealmObject().getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build()
                .create(WebServiceApi.class);
    }


}
