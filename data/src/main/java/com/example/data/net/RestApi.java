package com.example.data.net;

import com.example.data.entity.UserEntity;

import java.util.Collection;

/**
 * Created by plnc on 2017-06-08.
 */

public interface RestApi {

    interface UserListCallback {
        void onUserListLoaded(Collection<UserEntity> userCollection);

        void onError(Exception exception);
    }

    interface UserDetailsCallback {
        void onUserEntityLoaded(UserEntity userEntity);

        void onError(Exception e);
    }

    static final String API_BASE_URL = "http://www.android10.org/myapi/";
    static final String API_URL_GET_USER_LIST = API_BASE_URL + "users.json";
    static final String API_URL_GET_USER_DETAILS = API_BASE_URL + "user_";

    void getUserList(UserListCallback userListCallback);

    void getUserById(final int userId, final UserDetailsCallback userDetailsCallback);
}
