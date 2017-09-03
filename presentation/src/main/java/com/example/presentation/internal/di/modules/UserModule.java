package com.example.presentation.internal.di.modules;

import com.example.domain.interactor.GetUserDetailsUseCase;
import com.example.domain.interactor.GetUserDetailsUseCaseImpl;
import com.example.domain.interactor.GetUserListUseCase;
import com.example.domain.interactor.GetUserListUseCaseImpl;
import com.example.presentation.internal.di.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1 on 2017-08-30.
 */

@Module
public class UserModule {

    @Provides
    @PerActivity
    GetUserListUseCase provideGetUserListUseCase(GetUserListUseCaseImpl getUserListUseCase) {
        return getUserListUseCase;
    }

    @Provides
    @PerActivity
    GetUserDetailsUseCase provideGetUserDetailsUseCase(GetUserDetailsUseCaseImpl getUserDetailsUseCase) {
        return getUserDetailsUseCase;
    }
}
