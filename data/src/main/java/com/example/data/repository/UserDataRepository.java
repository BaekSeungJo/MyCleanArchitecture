package com.example.data.repository;

import com.example.data.entity.UserEntity;
import com.example.data.entity.mapper.UserEntityDataMapper;
import com.example.data.repository.datasource.UserDataStore;
import com.example.data.repository.datasource.UserDataStoreFactory;
import com.example.domain.User;
import com.example.domain.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by plnc on 2017-06-08.
 */

@Singleton
public class UserDataRepository implements UserRepository {

    private final UserDataStoreFactory userDataStoreFactory;
    private final UserEntityDataMapper userEntityDataMapper;

    private final Func1<List<UserEntity>, List<User>> userListEntityMapper =
            new Func1<List<UserEntity>, List<User>>() {
                @Override
                public List<User> call(List<UserEntity> userEntities) {
                    return userEntityDataMapper.transform(userEntities);
                }
            };

    private final Func1<UserEntity, User> userDetailsEntityMapper =
            new Func1<UserEntity, User>() {
                @Override
                public User call(UserEntity userEntity) {
                    return userEntityDataMapper.transform(userEntity);
                }
            };

    @Inject
    public UserDataRepository(UserDataStoreFactory userDataStoreFactory, UserEntityDataMapper userEntityDataMapper) {
        this.userDataStoreFactory = userDataStoreFactory;
        this.userEntityDataMapper = userEntityDataMapper;
    }

    @Override
    public Observable<List<User>> getUsers() {
        final UserDataStore userDataStore = userDataStoreFactory.createCloudDataStore();
        return userDataStore.getUserEntityList().map(userListEntityMapper);
    }

    @Override
    public Observable<User> getUserDetail(int userId) {
        UserDataStore userDataStore = userDataStoreFactory.create(userId);
        return userDataStore.getUserEntityDetails(userId).map(userDetailsEntityMapper);
    }
}
