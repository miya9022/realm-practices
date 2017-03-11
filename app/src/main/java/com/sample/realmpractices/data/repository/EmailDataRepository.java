package com.sample.realmpractices.data.repository;

import com.sample.realmpractices.data.entity.mapping.EmailEntityDataMapping;
import com.sample.realmpractices.data.repository.datasource.EmailDataSourceFactory;
import com.sample.realmpractices.domain.Email;
import com.sample.realmpractices.domain.repository.EmailRepository;

import java.util.List;

/**
 * Created by app on 3/10/17.
 */

public class EmailDataRepository implements EmailRepository {
    private final EmailDataSourceFactory emailDataSourceFactory;
    private final EmailEntityDataMapping emailEntityDataMapping;

    public EmailDataRepository(EmailDataSourceFactory emailDataSourceFactory,
                               EmailEntityDataMapping emailEntityDataMapping) {
        this.emailDataSourceFactory = emailDataSourceFactory;
        this.emailEntityDataMapping = emailEntityDataMapping;
    }

    @Override
    public List<Email> emailsByUser(int uid) {
        return emailEntityDataMapping.replicateEmailsFromRealm(
                emailDataSourceFactory.createDataSource().getEmailByUser(uid));
    }
}
