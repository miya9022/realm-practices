package com.sample.realmpractices.data.entity.mapping;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by app on 3/8/17.
 */

public class GsonRealmBuilder {
    private Gson gson;
    private RealmObjectExclusionStrategy realmObjExclusionStrategy;

    public Gson getGson() {
        return gson;
    }

    public GsonRealmBuilder createCustomGsonForRealmObject() {
        realmObjExclusionStrategy = new RealmObjectExclusionStrategy();
        gson = new GsonBuilder()
                .setExclusionStrategies(realmObjExclusionStrategy)
                .create();
        return this;
    }
}
