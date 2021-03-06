package com.example.data.repository.datasource;

import com.example.data.cache.UserCache;
import com.example.data.entity.UserEntity;
import com.example.data.net.RestApi;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by plnc on 2017-06-12.
 */
class CloudUserDataStore implements UserDataStore {

    private final RestApi restApi;
    private final UserCache userCache;

    CloudUserDataStore(RestApi restApi, UserCache userCache) {
        this.restApi = restApi;
        this.userCache = userCache;
    }

    @Override
    public Observable<List<UserEntity>> userEntityList() {
        return this.restApi.userEntityList();
    }

    /**
     * Get an {@link Observable} which will emit a {@link UserEntity} by its id.
     *
     * @param userId
     * @return
     */
    @Override
    public Observable<UserEntity> userEntityDetails(int userId) {
        return this.restApi.userEntityById(userId).doOnNext(CloudUserDataStore.this.userCache::put);
    }
}
