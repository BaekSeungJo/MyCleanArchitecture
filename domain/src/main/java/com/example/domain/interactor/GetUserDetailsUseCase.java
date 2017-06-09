package com.example.domain.interactor;

import com.example.domain.User;
import com.example.domain.exeception.ErrorBundle;

/**
 * Created by plnc on 2017-05-31.
 */

public interface GetUserDetailsUseCase extends Interactor {

    interface Callback {
        void onUserDataLoaded(User user);
        void onError(ErrorBundle errorBundle);
    }

    void execute(int userId, Callback callback);

}
