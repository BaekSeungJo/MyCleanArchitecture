package com.example.data.repository.datasource;

import com.example.data.entity.UserEntity;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by plnc on 2017-06-09.
 */

public interface UserDataStore {

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link UserEntity}
     * @return
     */
    Observable<List<UserEntity>> userEntityList();


    /**
     * Get an {@link rx.Observable} which will emit a {@link UserEntity} by its id.
     *
     * @param userId
     * @return
     */
    Observable<UserEntity> userEntityDetails(final  int userId);

}
