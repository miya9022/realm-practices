package com.sample.realmpractices.helper;

import android.util.Log;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.RealmResults;

/**
 * Created by app on 2/13/17.
 */

public class RealmProvider {
    private static final String TAG = RealmProvider.class.getSimpleName();

    public static void setupRealmConfig() {
//        Realm.removeDefaultConfiguration();
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    private static Realm getDefaultInstance() {
        return Realm.getDefaultInstance();
    }

    public static int getLastId(Class<? extends RealmObject> clazz, String fieldName) {
        if (getDefaultInstance().where(clazz).findAll().size() <= 0) return 0;
        return getDefaultInstance().where(clazz).max(fieldName).intValue();
    }

    public void insert(RealmObject o) {
        getDefaultInstance().executeTransactionAsync(realm -> {
            realm.copyToRealm(o);
            if (onChangeListener != null)
                onChangeListener.onChange(Type.INSERT, o);
        }, () -> Log.d(TAG, "insert success"), (e) -> Log.d(TAG, "insert failed caused by " + e.getMessage()));
    }

    public void deleteFirst(Class<? extends RealmObject> c) {
        getDefaultInstance().executeTransactionAsync(realm -> {
            RealmObject object = getDefaultInstance().where(c).findFirst();
            Object o = realm.copyFromRealm(object);
            object.deleteFromRealm();
            if (onChangeListener != null)
                onChangeListener.onChange(Type.DELETE, o);
        }, () -> Log.d(TAG, "delete success"), (e) -> Log.d(TAG, "delete failed caused by " + e.getMessage()));
    }

    public void deleteLast(Class<? extends RealmObject> c) {
        getDefaultInstance().executeTransactionAsync(realm -> {
            RealmObject object = getDefaultInstance().where(c).findAll().last();
            Object o = realm.copyFromRealm(object);
            object.deleteFromRealm();
            if (onChangeListener != null)
                onChangeListener.onChange(Type.DELETE, o);
        }, () -> Log.d(TAG, "delete success"), (e) -> Log.d(TAG, "delete failed caused by " + e.getMessage()));
    }

    public void deleteById(Class<? extends RealmObject> c, int id) {
        getDefaultInstance().executeTransactionAsync(realm -> {
            RealmObject object = getRealmObjectById(c, "id", id);
            Object o = realm.copyFromRealm(object);
            object.deleteFromRealm();
            if (onChangeListener != null)
                onChangeListener.onChange(Type.DELETE, o);
        },() -> Log.d(TAG, "delete success"), (e) -> Log.d(TAG, "delete failed caused by " + e.getMessage()));
    }

    public void deleteTable(Class<? extends RealmObject> c) {
        getDefaultInstance().executeTransactionAsync(realm -> {
            realm.delete(c);
        },() -> Log.d(TAG, "delete success"), (e) -> Log.d(TAG, "delete failed caused by " + e.getMessage()));
    }

    public RealmObject getRealmObjectById(Class<? extends RealmObject> c, String idName, int id) {
        return getDefaultInstance().where(c).equalTo(idName, id).findFirst();
    }

    public List<? extends RealmObject> getAllByClass(Class<? extends RealmObject> clazz) {
        return getDefaultInstance().copyFromRealm(queryAll(clazz));
    }

    public RealmResults<? extends RealmObject> queryAll(Class<? extends RealmObject> c) {
        return getDefaultInstance().where(c).findAll();
    }

    private OnChangeListener onChangeListener;

    public void addOnChangeListener(OnChangeListener listener) {
        this.onChangeListener = listener;
    }

    public void closeRealm() {
        if (getDefaultInstance().isClosed()) return;
        getDefaultInstance().close();
    }

    public interface OnChangeListener {
        void onChange(int type, Object object);
    }

    public class Type {
        public static final int INSERT = 1;
        public static final int DELETE = 2;
    }
}
