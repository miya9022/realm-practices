package com.sample.realmpractices.domain.interactor;

import com.sample.realmpractices.domain.Email;
import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;
import com.sample.realmpractices.domain.repository.EmailRepository;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/7/17.
 */

public class GetUserEmailListUseCase extends UseCase<List<Email>> {
    private EmailRepository emailRepository;
    private int uid = 0;

    public GetUserEmailListUseCase(EmailRepository emailRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.emailRepository = emailRepository;
    }

    public GetUserEmailListUseCase setId(int uid) {
        this.uid = uid;
        return this;
    }

    @Override
    protected Observable<List<Email>> buildUseCaseObservable() {
        return Observable.create(subscriber -> {
            subscriber.onNext(emailRepository.emailsByUser(uid));
        });
    }
}
