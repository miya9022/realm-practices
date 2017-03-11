package com.sample.realmpractices.domain.repository;

import com.sample.realmpractices.domain.Email;

import java.util.List;

/**
 * Created by app on 3/7/17.
 */

public interface EmailRepository {

    List<Email> emailsByUser(final int uid);
}
