package com.sample.realmpractices.data.repository.datasource;

import com.sample.realmpractices.data.entity.EmailEntity;

import java.util.List;

/**
 * Created by app on 3/10/17.
 */

public interface EmailDataSource {

    List<EmailEntity> getEmailByUser(final int userId);
}
