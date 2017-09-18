package com.example.data.repository;

import com.example.data.entity.mapper.UserEntityDataMapper;
import com.example.data.repository.datasource.UserDataStore;
import com.example.data.repository.datasource.UserDataStoreFactory;
import com.example.domain.User;
import com.example.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by plnc on 2017-06-08.
 */

@Singleton
public class UserDataRepository implements UserRepository {

    private final UserDataStoreFactory userDataStoreFactory;
    private final UserEntityDataMapper userEntityDataMapper;

    @Inject
    public UserDataRepository(UserDataStoreFactory userDataStoreFactory, UserEntityDataMapper userEntityDataMapper) {
        this.userDataStoreFactory = userDataStoreFactory;
        this.userEntityDataMapper = userEntityDataMapper;
    }

    @Override
    public Observable<List<User>> users() {
        final UserDataStore userDataStore = userDataStoreFactory.createCloudDataStore();
        return userDataStore.userEntityList().map(this.userEntityDataMapper::transform);
    }

    @Override
    public Observable<User> user(int userId) {
        UserDataStore userDataStore = userDataStoreFactory.create(userId);
        return userDataStore.userEntityDetails(userId).map(this.userEntityDataMapper::transform);
    }
}
