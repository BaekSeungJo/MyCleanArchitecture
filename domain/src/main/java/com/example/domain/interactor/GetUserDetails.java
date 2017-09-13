package com.example.domain.interactor;

import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.repository.UserRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by plnc on 2017-05-31.
 */

public class GetUserDetails extends UseCase{

    private final int userId;
    private final UserRepository userRepository;

    @Inject
    public GetUserDetails(int userId, UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.userId = userId;
        this.userRepository = userRepository;
    }

    /**
     * Builds an {@link Observable} which whill be used when executing the current {@link UseCase}.
     *
     * @return
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return this.userRepository.user(userId);
    }
}
