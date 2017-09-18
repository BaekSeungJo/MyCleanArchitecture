package com.example.domain.interactor;

import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.repository.UserRepository;
import com.fernandocejas.arrow.optional.Optional;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by plnc on 2017-05-31.
 */

public class GetUserDetails extends UseCase{

    public static final String NAME = "userDetails";
    public static final String PARAM_USER_ID_KEY = "userId";

    static final int PARAM_USER_ID_DEFAULT_VALUE = -1;

    private final UserRepository userRepository;

    @Inject
    public GetUserDetails(UserRepository userRepository, ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
        this.userRepository = userRepository;
    }

    /**
     * Builds an {@link Observable} which whill be used when executing the current {@link UseCase}.
     *
     * @return
     */
    @Override
    protected Observable buildUseCaseObservable(Optional<Params> params) {
        if(params.isPresent()) {
            final int userId = params.get().getInt(PARAM_USER_ID_KEY, PARAM_USER_ID_DEFAULT_VALUE);
            return this.userRepository.user(userId);
        } else {
            return Observable.empty();
        }
    }
}
