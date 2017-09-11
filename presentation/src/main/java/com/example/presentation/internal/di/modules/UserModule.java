package com.example.presentation.internal.di.modules;

import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.interactor.GetUserDetailsUseCase;
import com.example.domain.interactor.GetUserListUseCase;
import com.example.domain.interactor.UseCase;
import com.example.domain.repository.UserRepository;
import com.example.presentation.internal.di.PerActivity;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Dagger module that provides user related collaborators.
 */
@Module
public class UserModule {

    private int userId = -1;

    public UserModule() {}

    public UserModule(int userId) {
        this.userId = userId;
    }

    @Provides
    @PerActivity
    @Named("userList")
    UseCase provideGetUserListUseCase(GetUserListUseCase getUserListUseCase) {
        return getUserListUseCase;
    }

    @Provides
    @PerActivity
    @Named("userDetails")
    UseCase provideGetUserDetailsUseCase(UserRepository userRepository, ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        return new GetUserDetailsUseCase(userId, userRepository, threadExecutor, postExecutionThread);
    }
}
