package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.net.WebServiceApi;
import com.sample.realmpractices.data.net.WebServiceApiImpl;
import com.sample.realmpractices.data.repository.transaction.PutUserEntityTransaction;

/**
 * Created by app on 3/9/17.
 */

public class UserDataSourceFactory {
    private final UserHandler userHandler;
    private final PutUserEntityTransaction putUserEntityTransaction;

    public UserDataSourceFactory(UserHandler userHandler, PutUserEntityTransaction putUserEntityTransaction) {
        this.userHandler = userHandler;
        this.putUserEntityTransaction = putUserEntityTransaction;
    }

    public UserDataSource createDataSource() {
        WebServiceApi webServiceApi = new WebServiceApiImpl();
        return new SavedUserDataSource(userHandler, webServiceApi, putUserEntityTransaction);
    }
}
