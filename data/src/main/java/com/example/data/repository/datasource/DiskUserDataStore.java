package com.example.data.repository.datasource;

import com.example.data.cache.UserCache;
import com.example.data.entity.UserEntity;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created by plnc on 2017-06-09.
 */

class DiskUserDataStore implements UserDataStore {

    private final UserCache  userCache;

    DiskUserDataStore(UserCache userCache) {
        this.userCache = userCache;
    }

    @Override
    public Observable<List<UserEntity>> userEntityList() {
        throw new UnsupportedOperationException("Operation is not available!!!");
    }

    @Override
    public Observable<UserEntity> userEntityDetails(final int userId) {
        return this.userCache.get(userId);
    }
}
