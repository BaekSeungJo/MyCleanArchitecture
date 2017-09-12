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
    public Observable<List<User>> getUsers() {
        final UserDataStore userDataStore = userDataStoreFactory.createCloudDataStore();
        return userDataStore.getUserEntityList().map(userEntities -> userEntityDataMapper.transform(userEntities));
    }

    @Override
    public Observable<User> getUserDetail(int userId) {
        UserDataStore userDataStore = userDataStoreFactory.create(userId);
        return userDataStore.getUserEntityDetails(userId).map(userEntity -> userEntityDataMapper.transform(userEntity));
    }
}
