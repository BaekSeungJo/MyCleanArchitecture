package com.example.domain.repository;

import com.example.domain.User;
import com.example.domain.exeception.ErrorBundle;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by plnc on 2017-05-31.
 */

public interface UserRepository {

    interface UserDetailsCallback {
        void onUserLoaded(User user);

        void onError(ErrorBundle errorBundle);
    }

    /**
     * Get an {@link rx.Observable} which will emit a List of {@link User}
     * @return
     */
    Observable<List<User>> getUsers();

    void getUserDetail(final int userId, UserDetailsCallback userDetailCallback);
}
