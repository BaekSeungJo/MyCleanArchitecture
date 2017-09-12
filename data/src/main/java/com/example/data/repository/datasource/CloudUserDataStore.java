package com.example.data.repository.datasource;

import com.example.data.cache.UserCache;
import com.example.data.entity.UserEntity;
import com.example.data.net.RestApi;

import java.util.Collection;
import java.util.List;

import rx.Observable;
import rx.functions.Action;
import rx.functions.Action1;

/**
 * Created by plnc on 2017-06-12.
 */
public class CloudUserDataStore implements UserDataStore {

    private final RestApi restApi;
    private final UserCache userCache;

    private final Action1<UserEntity> saveToCacheAction =
            userEntity -> {
                if(userEntity != null) {
                    CloudUserDataStore.this.userCache.put(userEntity);
                }
            };

    public CloudUserDataStore(RestApi restApi, UserCache userCache) {
        this.restApi = restApi;
        this.userCache = userCache;
    }

    @Override
    public Observable<List<UserEntity>> getUserEntityList() {
        return this.restApi.getUserEntityList();
    }

    /**
     * Get an {@link Observable} which will emit a {@link UserEntity} by its id.
     *
     * @param userId
     * @return
     */
    @Override
    public Observable<UserEntity> getUserEntityDetails(int userId) {
        return this.restApi.getUserEntityById(userId).
                doOnNext(saveToCacheAction);
    }
}
