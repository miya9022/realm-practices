package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.net.WebServiceApi;
import com.sample.realmpractices.data.net.WebServiceApiImpl;

/**
 * Created by app on 3/9/17.
 */

public class UserDataSourceFactory {
    private final UserHandler userHandler;

    public UserDataSourceFactory(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    public UserDataSource createDataSource() {
        WebServiceApi webServiceApi = new WebServiceApiImpl();
        return new SavedUserDataSource(userHandler, webServiceApi);
    }
}
