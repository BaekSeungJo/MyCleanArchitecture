package com.example.data.cache;

import com.example.data.entity.UserEntity;

/**
 * Created by plnc on 2017-06-09.
 */

public interface UserCache {

    interface UserCacheCallback {
        void onUserEntityLoaded(UserEntity userEntity);

        void onError(Exception exception);
    }

    void get(final int userId, final UserCacheCallback callback);

    void put(UserEntity userEntity);

    boolean isCached(final int userId);

    boolean isExpired();

    void evictAll();
}
