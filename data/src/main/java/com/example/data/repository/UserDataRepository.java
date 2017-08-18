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

/**
 * Created by plnc on 2017-06-08.
 */

public class UserDataRepository implements UserRepository {

    private static UserDataRepository INSTANCE;

    public static synchronized UserDataRepository getInstance(UserDataStoreFactory dataStoreFactory,
                                                              UserEntityDataMapper userEntityDataMapper) {
        if(INSTANCE == null) {
            INSTANCE = new UserDataRepository(dataStoreFactory, userEntityDataMapper);
        }
        return INSTANCE;
    }

    private final UserDataStoreFactory userDataStoreFactory;
    private final UserEntityDataMapper userEntityDataMapper;

    protected UserDataRepository(UserDataStoreFactory userDataStoreFactory, UserEntityDataMapper userEntityDataMapper) {
        if(userDataStoreFactory == null || userEntityDataMapper == null) {
            throw new IllegalArgumentException("Invalid null parameters in constructor!!!");
        }
        this.userDataStoreFactory = userDataStoreFactory;
        this.userEntityDataMapper = userEntityDataMapper;
    }

    @Override
    public void getUserList(final UserListCallback userListCallback) {
        final UserDataStore userDataStore = userDataStoreFactory.createCloudDataStore();
        userDataStore.getUsersEntityList(new UserDataStore.UserListCallback() {
            @Override
            public void onUserListLoaded(Collection<UserEntity> usersCollection) {
                Collection<User> users = userEntityDataMapper.transform(usersCollection);
                userListCallback.onUserListLoaded(users);
            }

            @Override
            public void onError(Exception exception) {
                userListCallback.onError(new RepositoryErrorBundle(exception));
            }
        });
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
