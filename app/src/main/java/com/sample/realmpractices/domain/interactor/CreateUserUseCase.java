package com.sample.realmpractices.domain.interactor;

import com.sample.realmpractices.data.entity.UserEntity;
import com.sample.realmpractices.domain.executor.PostExecutionThread;
import com.sample.realmpractices.domain.executor.ThreadExecutor;
import com.sample.realmpractices.domain.repository.UserRepository;

import rx.Observable;

/**
 * Created by app on 3/14/17.
 */

public class CreateUserUseCase extends UseCase {

    private UserRepository userRepository;
    private UserEntity userEntity;

    public CreateUserUseCase(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Override
    protected Observable buildUseCaseObservable() {
        return userRepository.insertUser(userEntity);
    }
}
