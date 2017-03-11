package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;

/**
 * Created by app on 3/10/17.
 */

public class EmailDataSourceFactory {
    private final UserHandler userHandler;

    public EmailDataSourceFactory(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    public EmailDataSource createDataSource() {
        return new EmailSavedDataSource(userHandler);
    }
}
