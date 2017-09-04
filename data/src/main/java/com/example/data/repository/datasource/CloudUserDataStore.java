package com.example.data.repository.datasource;

import com.example.data.cache.UserCache;
import com.example.data.entity.UserEntity;
import com.example.data.net.RestApi;

import java.util.Collection;

/**
 * Created by plnc on 2017-06-12.
 */
public class CloudUserDataStore implements UserDataStore {

    private final RestApi restApi;
    private final UserCache userCache;

    public CloudUserDataStore(RestApi restApi, UserCache userCache) {
        this.restApi = restApi;
        this.userCache = userCache;
    }

    @Override
    public void getUsersEntityList(final UserListCallback userListCallback) {
        restApi.getUserList(new RestApi.UserListCallback() {
            @Override
            public void onUserListLoaded(Collection<UserEntity> userCollection) {
                userListCallback.onUserListLoaded(userCollection);
            }

            @Override
            public void onError(Exception exception) {
                userListCallback.onError(exception);
            }
        });
    }

    @Override
    public void getUserEntityDetails(int id, final UserDetailsCallback userDetailsCallback) {
        restApi.getUserById(id, new RestApi.UserDetailsCallback() {
            @Override
            public void onUserEntityLoaded(UserEntity userEntity) {
                userDetailsCallback.onUserEntityLoaded(userEntity);
                putUserEntityInCache(userEntity);
            }

            @Override
            public void onError(Exception e) {
                userDetailsCallback.onError(e);
            }
        });
    }

    private void putUserEntityInCache(UserEntity userEntity) {
        if(userEntity != null) {
            userCache.put(userEntity);
        }
    }
}
