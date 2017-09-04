package com.example.data.net;

import com.example.data.entity.UserEntity;

import java.util.Collection;
import java.util.List;

import rx.Observable;

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

    static final String API_BASE_URL = "https://raw.githubusercontent.com/android10/Sample-Data/master/Android-CleanArchitecture/";
    static final String API_URL_GET_USER_LIST = API_BASE_URL + "users.json";
    static final String API_URL_GET_USER_DETAILS = API_BASE_URL + "user_";

    /**
     * Get a Observable which will emit a List of {@link UserEntity}
     * @return
     */
    Observable<List<UserEntity>> getUserEntityList();

    void getUserById(final int userId, final UserDetailsCallback userDetailsCallback);
}
