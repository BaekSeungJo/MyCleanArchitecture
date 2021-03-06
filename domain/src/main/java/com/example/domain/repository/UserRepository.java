package com.example.domain.repository;

import com.example.domain.User;

import java.util.List;

import io.reactivex.Observable;


/**
 * Created by plnc on 2017-05-31.
 */

public interface UserRepository {

    /**
     * Get an {@link Observable} which will emit a List of {@link User}
     * @return
     */
    Observable<List<User>> users();

    /**
     * Get an {@link Observable} which will emit a {@link User}
     * @param userId
     * @return
     */
    Observable<User> user(final int userId);
}
