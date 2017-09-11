package com.example.data.repository.datasource;

import com.example.data.cache.UserCache;
import com.example.data.entity.UserEntity;

import java.util.List;

import rx.Observable;

/**
 * Created by plnc on 2017-06-09.
 */

public class DiskUserDataStore implements UserDataStore {

    private final UserCache  userCache;

    public DiskUserDataStore(UserCache userCache) {
        this.userCache = userCache;
    }

    @Override
    public Observable<List<UserEntity>> getUserEntityList() {
        throw new UnsupportedOperationException("Operation is not available!!!");
    }

    @Override
    public Observable<UserEntity> getUserEntityDetails(final int userId) {
        return this.userCache.get(userId);
    }
}
