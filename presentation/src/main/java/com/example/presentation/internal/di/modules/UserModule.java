package com.example.presentation.internal.di.modules;

import android.content.Context;

import com.example.data.cache.FileManager;
import com.example.data.cache.UserCache;
import com.example.data.cache.UserCacheImpl;
import com.example.data.cache.serializer.JsonSerializer;
import com.example.data.entity.mapper.UserEntityDataMapper;
import com.example.data.executor.JobExecutor;
import com.example.data.repository.UserDataRepository;
import com.example.data.repository.datasource.UserDataStoreFactory;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.interactor.GetUserDetailsUseCase;
import com.example.domain.interactor.GetUserDetailsUseCaseImpl;
import com.example.domain.interactor.GetUserListUseCase;
import com.example.domain.interactor.GetUserListUseCaseImpl;
import com.example.domain.repository.UserRepository;
import com.example.presentation.UIThread;
import com.example.presentation.mapper.UserModelDataMapper;
import com.example.presentation.presenter.UserDetailsPresenter;
import com.example.presentation.presenter.UserListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by 1 on 2017-08-30.
 */

@Module
public class UserModule {

    @Provides
    ThreadExecutor provideThreadExecutor() {
        return JobExecutor.getInstance();
    }

    @Provides
    PostExecutionThread providePostExecutionThread() {
        return UIThread.getInstance();
    }

    @Provides
    JsonSerializer provideJsonSerializer() {
        return new JsonSerializer();
    }

    @Provides
    FileManager provideFileManager() {
        return FileManager.getInstance();
    }

    @Provides
    UserCache provideUserCache(Context context, JsonSerializer jsonSerializer,
                               FileManager fileManager, ThreadExecutor threadExecutor) {
        return UserCacheImpl.getInstance(context, jsonSerializer, fileManager, threadExecutor);
    }

    @Provides
    UserDataStoreFactory provideUserDataStoreFactory(Context context, UserCache userCache) {
        return new UserDataStoreFactory(context, userCache);
    }

    @Provides
    UserEntityDataMapper provideUserEntityDataMapper() {
        return new UserEntityDataMapper();
    }

    @Provides
    UserRepository provideUserRepository(UserDataStoreFactory userDataStoreFactory, UserEntityDataMapper userEntityDataMapper) {
        return UserDataRepository.getInstance(userDataStoreFactory, userEntityDataMapper);
    }

    @Provides
    GetUserListUseCase provideGetUserListUseCase(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetUserListUseCaseImpl(userRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    UserModelDataMapper provideUserModelDataMapper() {
        return new UserModelDataMapper();
    }

    @Provides
    UserListPresenter provideUserListPresenter(GetUserListUseCase getUserListUseCase, UserModelDataMapper userModelDataMapper) {
        return new UserListPresenter(getUserListUseCase, userModelDataMapper);
    }

    @Provides
    GetUserDetailsUseCase provideGetUserDetailsUseCase(UserRepository userRepository, ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        return new GetUserDetailsUseCaseImpl(userRepository, threadExecutor, postExecutionThread);
    }

    @Provides
    UserDetailsPresenter provideUserDetailsPresenter(GetUserDetailsUseCase getUserDetailsUseCase, UserModelDataMapper userModelDataMapper) {
        return new UserDetailsPresenter(getUserDetailsUseCase, userModelDataMapper);
    }
}
