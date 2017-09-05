package com.example.data.cache;

import com.example.data.entity.UserEntity;

import rx.Observable;

/**
 * Created by plnc on 2017-06-09.
 */

public interface UserCache {

    /**
     * Get an {@link rx.Observable} which will emit a {@link UserEntity}.
     *
     * @param userId The user id  to retrive data
     */
    Observable<UserEntity> get(final int userId);


    void put(UserEntity userEntity);

    boolean isCached(final int userId);

    boolean isExpired();

    void evictAll();
}
