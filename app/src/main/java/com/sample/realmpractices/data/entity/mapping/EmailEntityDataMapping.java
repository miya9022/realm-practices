package com.sample.realmpractices.data.entity.mapping;

import com.annimon.stream.Stream;
import com.sample.realmpractices.data.entity.EmailEntity;
import com.sample.realmpractices.domain.Email;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by app on 3/9/17.
 */

public class EmailEntityDataMapping {
    public EmailEntityDataMapping(){}

    public Email replicateEmailFromRealm(EmailEntity emailEntity) {
        Email email = null;
        if (emailEntity.isLoaded() && emailEntity.isValid()) {
            email = new Email();
            email.setEmail(emailEntity.getEmail());
            email.setActive(emailEntity.getActive());
        }
        return email;
    }

    public List<Email> replicateEmailsFromRealm(List<EmailEntity> entities) {
        List<Email> emails = new ArrayList<>();
        Stream.of(entities)
                .forEach(emailEntity -> emails.add(replicateEmailFromRealm(emailEntity)));
        return emails;
    }
}
