package com.example.data.repository.datasource;

import com.example.data.cache.UserCache;
import com.example.data.entity.UserEntity;

/**
 * Created by plnc on 2017-06-09.
 */

public class DiskUserDataStore implements UserDataStore {

    private final UserCache userCache;

    public DiskUserDataStore(UserCache userCache) {
        this.userCache = userCache;
    }

    @Override
    public void getUsersEntityList(UserListCallback userListCallback) {
        throw new UnsupportedOperationException("Operation is not available!!!");
    }

    @Override
    public void getUserEntityDetails(int id, final UserDetailsCallback userDetailsCallback) {
        userCache.get(id, new UserCache.UserCacheCallback() {

            @Override
            public void onUserEntityLoaded(UserEntity userEntity) {
                userDetailsCallback.onUserEntityLoaded(userEntity);
            }

            @Override
            public void onError(Exception exception) {
                userDetailsCallback.onError(exception);
            }
        });
    }
}
