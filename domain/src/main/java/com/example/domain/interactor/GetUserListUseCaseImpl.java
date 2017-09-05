package com.example.domain.interactor;

import com.example.domain.DefaultSubscriber;
import com.example.domain.User;
import com.example.domain.exeception.DefaultErrorBundle;
import com.example.domain.exeception.ErrorBundle;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

import rx.Observer;

/**
 * Created by plnc on 2017-05-31.
 */

public class GetUserListUseCaseImpl implements GetUserListUseCase {

    private final UserRepository userRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    private GetUserListUseCase.Callback callback;

    @Inject
    public GetUserListUseCaseImpl(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.userRepository = userRepository;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(Callback callback) {
        if (callback == null) {
            throw new IllegalArgumentException("Interactor callback cannot be null!!!");
        }
        this.callback = callback;
        this.threadExecutor.execute(this);
    }

    @Override
    public void run() {
        // For now this is being executed in a separate thread but should be change for schedulers.
        this.userRepository.getUsers()
                .subscribe(new GetUserListSubscriber(callback, postExecutionThread));
    }

    private static class GetUserListSubscriber extends DefaultSubscriber<List<User>> {
        private final Callback callback;
        private final PostExecutionThread postExecutionThread;

        public GetUserListSubscriber(Callback callback, PostExecutionThread postExecutionThread) {
            this.callback = callback;
            this.postExecutionThread = postExecutionThread;
        }

        @Override
        public void onError(final Throwable e) {
            this.postExecutionThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onError(new DefaultErrorBundle((Exception)e));
                }
            });
        }

        @Override
        public void onNext(final List<User> users) {
            this.postExecutionThread.post(new Runnable() {
                @Override
                public void run() {
                    callback.onUserListLoaded(users);
                }
            });
        }
    }

}
