package com.example.domain.interactor;

import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.repository.UserRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by plnc on 2017-05-31.
 */

public class GetUserListUseCase extends UseCase {

    private final UserRepository userRepository;

    /**
     * Constructor of the class
     *
     * @param userRepository A {@link UserRepository} as a source for retrieving data.
     * @param threadExecutor A {@link ThreadExecutor} used to execute this use case in a background thread.
     * @param postExecutionThread A {@link PostExecutionThread} used to post updates when the use case has been executed.
     */
    @Inject
    public GetUserListUseCase(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    /**
     * Builds an {@link Observable} which whill be used when executing the current {@link UseCase}.
     *
     * @return
     */
    @Override
    protected Observable buildUseCaseObservable() {
        return this.userRepository.getUsers();
    }
}
