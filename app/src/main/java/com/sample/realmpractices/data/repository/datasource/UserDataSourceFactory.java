package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.net.WebServiceApi;
import com.sample.realmpractices.data.net.WebServiceApiImpl;
import com.sample.realmpractices.data.repository.transaction.PutListUserEntityTransaction;

/**
 * Created by app on 3/9/17.
 */

public class UserDataSourceFactory {
    private final UserHandler userHandler;
    private final PutListUserEntityTransaction putListUserEntityTransaction;

    public UserDataSourceFactory(UserHandler userHandler,
                                 PutListUserEntityTransaction putListUserEntityTransaction) {
        this.userHandler = userHandler;
        this.putListUserEntityTransaction = putListUserEntityTransaction;
    }

    public UserDataSource createDataSource() {
        WebServiceApi webServiceApi = new WebServiceApiImpl();
        return new SavedUserDataSource(userHandler, webServiceApi, putListUserEntityTransaction);
    }
}
