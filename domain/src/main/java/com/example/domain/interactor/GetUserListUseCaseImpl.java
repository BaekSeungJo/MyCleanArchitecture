package com.example.domain.interactor;

import com.example.domain.User;
import com.example.domain.exeception.ErrorBundle;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.repository.UserRepository;

import java.util.Collection;

/**
 * Created by plnc on 2017-05-31.
 */

public class GetUserListUseCaseImpl implements GetUserListUseCase {

    private final UserRepository userRepository;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    private GetUserListUseCase.Callback callback;

    public GetUserListUseCaseImpl(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        if (userRepository == null || threadExecutor == null || postExecutionThread == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
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
        userRepository.getUserList(repositoryCallback);
    }

    private final UserRepository.UserListCallback repositoryCallback =
            new UserRepository.UserListCallback() {
                @Override
                public void onUserListLoaded(Collection<User> usersCollection) {
                    notifyGetUserListSuccessfully(usersCollection);
                }

                @Override
                public void onError(ErrorBundle errorBundle) {
                    notifyError(errorBundle);
                }
            };

    private void notifyGetUserListSuccessfully(final Collection<User> usersCollection) {
        postExecutionThread.post(new Runnable() {
            @Override
            public void run() {
                callback.onUserListLoaded(usersCollection);
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
