package com.sample.realmpractices.domain.interactor;

import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;
import com.sample.realmpractices.domain.repository.UserRepository;

import rx.Observable;

/**
 * Created by app on 3/14/17.
 */

public class DeleteUserUseCase extends UseCase {
    private UserRepository userRepository;
    private int userId;

    public DeleteUserUseCase(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    public void setUserId(int uid) {
        this.userId = uid;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return Observable.just(userRepository.deleteUser(userId));
    }
}
