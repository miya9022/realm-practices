package com.sample.realmpractices.domain.interactor;

import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;
import com.sample.realmpractices.domain.repository.UserRepository;
import com.sample.realmpractices.domain.User;

import java.util.List;

import rx.Observable;

/**
 * Created by app on 3/7/17.
 */

public class GetUserListUseCase extends UseCase<List<User>> {

    private UserRepository userRepository;

    public GetUserListUseCase(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    @Override
    protected Observable<List<User>> buildUseCaseObservable() {
        return userRepository.users();
    }
}
