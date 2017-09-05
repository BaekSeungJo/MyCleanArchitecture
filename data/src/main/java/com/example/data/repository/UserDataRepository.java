package com.example.data.repository;

import com.example.data.entity.UserEntity;
import com.example.data.entity.mapper.UserEntityDataMapper;
import com.example.data.exception.RepositoryErrorBundle;
import com.example.data.exception.UserNotFoundException;
import com.example.data.repository.datasource.UserDataStore;
import com.example.data.repository.datasource.UserDataStoreFactory;
import com.example.domain.User;
import com.example.domain.repository.UserRepository;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;

/**
 * Created by plnc on 2017-06-08.
 */

@Singleton
public class UserDataRepository implements UserRepository {

    private final UserDataStoreFactory userDataStoreFactory;
    private final UserEntityDataMapper userEntityDataMapper;

    private final Func1<List<UserEntity>, List<User>> userEntityMapper =
            new Func1<List<UserEntity>, List<User>>() {
                @Override
                public List<User> call(List<UserEntity> userEntities) {
                    return userEntityDataMapper.transform(userEntities);
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
        return userDataStore.getUserEntityList().map(userEntityMapper);
    }

    @Override
    public void getUserDetail(int userId, final UserDetailsCallback userDetailCallback) {
        UserDataStore userDataStore = userDataStoreFactory.create(userId);
        userDataStore.getUserEntityDetails(userId, new UserDataStore.UserDetailsCallback() {
            @Override
            public void onUserEntityLoaded(UserEntity userEntity) {
                User user = userEntityDataMapper.transform(userEntity);
                if(user != null) {
                    userDetailCallback.onUserLoaded(user);
                } else {
                    userDetailCallback.onError(new RepositoryErrorBundle(new UserNotFoundException()));
                }
            }

            @Override
            public void onError(Exception exception) {
                userDetailCallback.onError(new RepositoryErrorBundle(exception));
            }
        });
    }
}
