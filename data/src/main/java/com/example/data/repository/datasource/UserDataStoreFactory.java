package com.example.data.repository.datasource;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.data.cache.UserCache;
import com.example.data.entity.mapper.UserEntityJsonMapper;
import com.example.data.net.RestApi;
import com.example.data.net.RestApiImpl;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by plnc on 2017-06-12.
 */

@Singleton
public class UserDataStoreFactory {

    private final Context context;
    private final UserCache userCache;

    @Inject
    public UserDataStoreFactory(@NonNull Context context, @NonNull UserCache userCache) {
        if(context == null || userCache == null) {
            throw new IllegalArgumentException("Constructor parameters cannot be null!!!");
        }
        this.context = context.getApplicationContext();
        this.userCache = userCache;
    }

    public UserDataStore create(int userId) {
        UserDataStore userDataStore;

        if(!userCache.isExpired() && userCache.isCached(userId)) {
            userDataStore = new DiskUserDataStore(userCache);
        } else {
            userDataStore = createCloudDataStore();
        }

        return userDataStore;
    }

    public UserDataStore createCloudDataStore() {
        UserEntityJsonMapper userEntityJsonMapper = new UserEntityJsonMapper();
        RestApi restApi = new RestApiImpl(context, userEntityJsonMapper);

        return new CloudUserDataStore(restApi, userCache);
    }
}
