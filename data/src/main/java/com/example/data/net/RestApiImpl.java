package com.example.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.data.entity.UserEntity;
import com.example.data.entity.mapper.UserEntityJsonMapper;

import java.util.Collection;

/**
 * Created by plnc on 2017-06-08.
 */

public class RestApiImpl implements RestApi {

    private final Context context;
    private final UserEntityJsonMapper userEntityJsonMapper;

    public RestApiImpl(Context context, UserEntityJsonMapper userEntityJsonMapper) {
        if(context == null || userEntityJsonMapper == null) {
            throw new IllegalArgumentException("The constructor parameters cannot be null!!!!");
        }
        this.context = context;
        this.userEntityJsonMapper = userEntityJsonMapper;
    }

    @Override
    public void getUserList(UserListCallback userListCallback) {
        if(userListCallback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }

        if(isThereInternetConnection()) {
            try {
                ApiConnection getUserListConnection = ApiConnection.createGet(RestApi.API_URL_GET_USER_LIST);
                String responseUserList = getUserListConnection.requestSyncCall();
                Collection<UserEntity> userEntityList = userEntityJsonMapper.transformUserEntityCollection(responseUserList);

                userListCallback.onUserListLoaded(userEntityList);
            } catch (Exception e) {
//                userListCallback.onError(new Network);
            }
        }
    }

    @Override
    public void getUserById(int userId, UserDetailsCallback userDetailsCallback) {
        if(userDetailsCallback == null) {
            throw new IllegalArgumentException("Callback cannot be null!!!");
        }

        if(isThereInternetConnection()) {
            try {
                String apiUrl = RestApi.API_URL_GET_USER_DETAILS + userId + ".json";
                ApiConnection getUserDetailsConnection = ApiConnection.createGet(apiUrl);
                String responseUserDetails = getUserDetailsConnection.requestSyncCall();
                UserEntity userEntity = userEntityJsonMapper.transformUserEntity(responseUserDetails);

                userDetailsCallback.onUserEntityLoaded(userEntity);
            } catch (Exception e) {
                userDetailsCallback.onError(new NetworkConnectionException(e.getCause()));
            }
        } else {
            userDetailsCallback.onError(new NetworkConnectionException());
        }
    }

    private boolean isThereInternetConnection() {
        boolean isConnected;

        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        isConnected = (networkInfo != null && networkInfo.isConnectedOrConnecting());

        return isConnected;
    }
}
