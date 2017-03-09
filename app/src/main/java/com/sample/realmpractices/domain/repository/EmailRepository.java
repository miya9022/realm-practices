package com.sample.realmpractices.domain.repository;

import com.sample.realmpractices.domain.Email;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/7/17.
 */

public interface EmailRepository {

    Observable<List<Email>> emailsByUser(final int uid);
}
