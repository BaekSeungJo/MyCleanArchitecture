package com.example.data.repository.datasource;

import com.example.data.entity.UserEntity;

import java.util.Collection;

/**
 * Created by plnc on 2017-06-09.
 */

public interface UserDataStore {

    interface UserListCallback {
        void onUserListLoaded(Collection<UserEntity> usersCollection);

        void onError(Exception exception);
    }

    interface UserDetailsCallback {
        void onUserEntityLoaded(UserEntity userEntity);

        void onError(Exception exception);
    }

    void getUsersEntityList(UserListCallback userListCallback);

    void getUserEntityDetails(int id, UserDetailsCallback userDetailsCallback);

}
