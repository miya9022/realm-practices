package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.net.WebServiceApi;
import com.sample.realmpractices.data.net.WebServiceApiImpl;
import com.sample.realmpractices.data.repository.transaction.DatabaseTransaction;

/**
 * Created by app on 3/9/17.
 */

public class UserDataSourceFactory {
    private final UserHandler userHandler;
    private final DatabaseTransaction databaseTransaction;

    public UserDataSourceFactory(UserHandler userHandler,
                                 DatabaseTransaction databaseTransaction) {
        this.userHandler = userHandler;
        this.databaseTransaction = databaseTransaction;
    }

    public UserDataSource createDataSource() {
        WebServiceApi webServiceApi = new WebServiceApiImpl();
        return new UserSavedDataSource(userHandler, webServiceApi, databaseTransaction);
    }
}
