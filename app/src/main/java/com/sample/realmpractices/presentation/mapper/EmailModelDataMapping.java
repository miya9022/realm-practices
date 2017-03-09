package com.sample.realmpractices.presentation.mapper;

import com.annimon.stream.Stream;
import com.sample.realmpractices.domain.Email;
import com.sample.realmpractices.presentation.model.EmailModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by app on 3/9/17.
 */

public class EmailModelDataMapping {

    public EmailModelDataMapping() {}

    public EmailModel parse(Email email) {
        if (email == null) {
            throw new NullPointerException("email not null");
        }

        EmailModel emailModel = new EmailModel();
        emailModel.setEmail(email.getEmail());
        emailModel.setActive(email.getActive());
        return emailModel;
    }

    public List<EmailModel> parseList(Collection<Email> lsEmail) {
        List<EmailModel> emailModels = new ArrayList<>();
        Stream.of(lsEmail)
            .forEach(email -> emailModels.add(parse(email)));
        return emailModels;
    }
}
