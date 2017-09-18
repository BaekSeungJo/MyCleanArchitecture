package com.example.data.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.data.entity.UserEntity;
import com.example.data.entity.mapper.UserEntityJsonMapper;
import com.example.data.exception.NetworkConnectionException;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.net.MalformedURLException;
import java.util.List;

import rx.Observable;

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

    /**
     * Get a Observable which will emit a List of {@link UserEntity}
     *
     * @return
     */
    @RxLogObservable
    @Override
    public Observable<List<UserEntity>> userEntityList() {
        return Observable.create(subscriber -> {
            if(isThereInternetConnection()) {
                try {
                    String responseUserEntities = getUserEntitiesFromApi();
                    if(responseUserEntities != null) {
                        subscriber.onNext(userEntityJsonMapper.transformUserEntityCollection(responseUserEntities));
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new NetworkConnectionException());
                    }
                } catch(Exception e) {
                    subscriber.onError(new NetworkConnectionException(e.getCause()));
                }
            } else {
                subscriber.onError(new NetworkConnectionException());
            }
        });
    }

    @RxLogObservable
    @Override
    public Observable<UserEntity> userEntityById(final int userId) {
        return Observable.create(subscriber -> {
            if(isThereInternetConnection()) {
                try {
                    String responseUserDetails = getUserDetailsFromApi(userId);
                    if(responseUserDetails != null) {
                        subscriber.onNext(userEntityJsonMapper.transformUserEntity(responseUserDetails));
                        subscriber.onCompleted();
                    } else {
                        subscriber.onError(new NetworkConnectionException());
                    }
                } catch (Exception e) {
                    subscriber.onError(new NetworkConnectionException(e.getCause()));
                }
            } else {
                subscriber.onError(new NetworkConnectionException());
            }
        });
    }

    private String getUserEntitiesFromApi() throws MalformedURLException {
        return ApiConnection.createGet(API_URL_GET_USER_LIST).requestSyncCall();
    }

    private String getUserDetailsFromApi(int userId) throws MalformedURLException {
        String apiUrl = API_URL_GET_USER_DETAILS + userId + ".json";
        return ApiConnection.createGet(apiUrl).requestSyncCall();
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
