package com.example.data.net;

import com.example.data.entity.UserEntity;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by plnc on 2017-06-08.
 */

public interface RestApi {

    static final String API_BASE_URL = "https://raw.githubusercontent.com/android10/Sample-Data/master/Android-CleanArchitecture/";
    /** Api url for getting all users */
    static final String API_URL_GET_USER_LIST = API_BASE_URL + "users.json";
    static final String API_URL_GET_USER_DETAILS = API_BASE_URL + "user_";

    /**
     * Retrives an {@link rx.Observable} which will emit a List of {@link UserEntity}.
     * @return
     */
    Observable<List<UserEntity>> getUserEntityList();

    /**
     * Retrives an {@link rx.Observable} which will emit a {@link UserEntity}.
     *
     * @param userId The user id used to get user data.
     */
    Observable<UserEntity> getUserEntityById(final int userId);
}
