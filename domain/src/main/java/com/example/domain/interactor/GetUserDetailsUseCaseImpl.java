package com.example.domain.interactor;

import com.example.domain.User;
import com.example.domain.exeception.ErrorBundle;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.repository.UserRepository;

/**
 * Created by plnc on 2017-05-31.
 */

public class GetUserDetailsUseCaseImpl implements GetUserDetailsUseCase {

    private final UserRepository userRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    private int userId = -1;
    private GetUserDetailsUseCase.Callback callback;

    public GetUserDetailsUseCaseImpl(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        if (userRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        this.userRepository = userRepository;
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
    }

    @Override
    public void execute(int userId, Callback callback) {
        if (userId < 0 || callback == null) {
            throw new IllegalArgumentException("Invalid parameter!!!");
        }
        this.userId = userId;
        this.callback = callback;
        this.threadExecutor.execute(this);
    }

    @Override
    public void run() {
        this.userRepository.getUserDetail(userId, repositoryCallback);
    }

    private UserRepository.UserDetailsCallback repositoryCallback =
            new UserRepository.UserDetailsCallback() {
                @Override
                public void onUserLoaded(User user) {
                    notifyGetUserDetailsSuccessfully(user);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {
                    notifyError(errorBundle);
                }
            };

    private void notifyGetUserDetailsSuccessfully(final User user) {
        postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onUserDataLoaded(user);
            }
        });
    }

    private void notifyError(final ErrorBundle errorBundle) {
        postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(errorBundle);
            }
        });
    }
}
