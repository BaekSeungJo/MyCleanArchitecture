package com.example.data.net;

import com.example.data.entity.UserEntity;
import java.util.List;

import io.reactivex.Observable;


/**
 * Created by plnc on 2017-06-08.
 */

public interface RestApi {

    String API_BASE_URL = "https://raw.githubusercontent.com/android10/Sample-Data/master/Android-CleanArchitecture/";
    /** Api url for getting all users */
    String API_URL_GET_USER_LIST = API_BASE_URL + "users.json";
    String API_URL_GET_USER_DETAILS = API_BASE_URL + "user_";

    /**
     * Retrives an {@link Observable} which will emit a List of {@link UserEntity}.
     * @return
     */
    Observable<List<UserEntity>> userEntityList();

    /**
     * Retrives an {@link Observable} which will emit a {@link UserEntity}.
     *
     * @param userId The user id used to get user data.
     */
    Observable<UserEntity> userEntityById(final int userId);
}
