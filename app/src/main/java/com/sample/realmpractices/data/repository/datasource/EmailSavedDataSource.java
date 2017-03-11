package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.database.UserHandler;
import com.sample.realmpractices.data.entity.EmailEntity;

import java.util.List;

/**
 * Created by app on 3/10/17.
 */

class EmailSavedDataSource implements EmailDataSource {
    private final UserHandler userHandler;

    public EmailSavedDataSource(UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @Override
    public List<EmailEntity> getEmailByUser(int userId) {
        return userHandler.getEmailsByUserId(userId);
    }
}
