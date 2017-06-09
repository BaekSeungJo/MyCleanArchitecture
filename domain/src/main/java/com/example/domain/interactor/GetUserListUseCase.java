package com.example.domain.interactor;

import com.example.domain.User;
import com.example.domain.exeception.ErrorBundle;

import java.util.Collection;

/**
 * Created by plnc on 2017-05-31.
 */

public interface GetUserListUseCase extends Interactor {

    interface Callback {
        void onUserListLoaded(Collection<User> userCollection);
        void onError(ErrorBundle errorBundle);
    }

    void execute(Callback callback);
}
