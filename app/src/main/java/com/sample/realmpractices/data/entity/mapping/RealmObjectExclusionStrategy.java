package com.sample.realmpractices.data.entity.mapping;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

import io.realm.RealmObject;

/**
 * Created by app on 3/8/17.
 */

public class RealmObjectExclusionStrategy implements ExclusionStrategy {

    @Override
    public boolean shouldSkipField(FieldAttributes f) {
        return f.getDeclaringClass().equals(RealmObject.class);
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }
}
