package com.example.presentation.internal.di.modules;

import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.interactor.GetUserDetails;
import com.example.domain.interactor.GetUserList;
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

    public UserModule() {}

    @Provides
    @PerActivity
    @Named(GetUserList.NAME)
    UseCase provideGetUserListUseCase(GetUserList getUserList) {
        return getUserList;
    }

    @Provides
    @PerActivity
    @Named(GetUserDetails.NAME)
    UseCase provideGetUserDetailsUseCase(UserRepository userRepository, ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread) {
        return new GetUserDetails(userRepository, threadExecutor, postExecutionThread);
    }
}
