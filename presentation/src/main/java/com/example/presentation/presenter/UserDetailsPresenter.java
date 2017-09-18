package com.example.presentation.presenter;

import android.support.annotation.NonNull;

import com.example.domain.User;
import com.example.domain.exeception.DefaultErrorBundle;
import com.example.domain.exeception.ErrorBundle;
import com.example.domain.interactor.DefaultObserver;
import com.example.domain.interactor.GetUserDetails;
import com.example.domain.interactor.Params;
import com.example.domain.interactor.UseCase;
import com.example.presentation.exception.ErrorMessageFactory;
import com.example.presentation.internal.di.PerActivity;
import com.example.presentation.mapper.UserModelDataMapper;
import com.example.presentation.model.UserModel;
import com.example.presentation.view.UserDetailsView;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by plnc on 2017-06-28.
 */

@PerActivity
public class UserDetailsPresenter implements Presenter {

    private UserDetailsView viewDetailsView;
    private final UseCase getUserDetailsUseCase;
    private final UserModelDataMapper userModelDataMapper;

    @Inject
    public UserDetailsPresenter(@Named(GetUserDetails.NAME) UseCase getUserDetailsUseCase, UserModelDataMapper userModelDataMapper) {
        this.getUserDetailsUseCase = getUserDetailsUseCase;
        this.userModelDataMapper = userModelDataMapper;
    }

    public void setView(@NonNull UserDetailsView viewDetailsView) {
        this.viewDetailsView = viewDetailsView;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void destroy() {
        this.getUserDetailsUseCase.unsubscribe();
        this.viewDetailsView = null;
    }

    public void initialize(int userId) {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserDetails(userId);
    }

    private void showViewLoading() {
        this.viewDetailsView.showLoading();
    }

    private void hideViewLoading() {
        this.viewDetailsView.hideLoading();
    }

    private void showViewRetry() {
        this.viewDetailsView.showRetry();
    }

    private void hideViewRetry() {
        this.viewDetailsView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.viewDetailsView.context(), errorBundle.getException());
        this.viewDetailsView.showError(errorMessage);
    }

    private void showUserDetailsInView(User user) {
        final UserModel userModel = this.userModelDataMapper.transform(user);
        this.viewDetailsView.renderUser(userModel);
    }

    private void getUserDetails(int userId) {
        final Params params = Params.create();
        params.putInt(GetUserDetails.PARAM_USER_ID_KEY, userId);
        this.getUserDetailsUseCase.execute(new UserDetailsObserver(), params);
    }

    private final class UserDetailsObserver extends DefaultObserver<User> {
        @Override
        public void onCompleted() {
            UserDetailsPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(Throwable e) {
            UserDetailsPresenter.this.hideViewLoading();
            UserDetailsPresenter.this.showErrorMessage(new DefaultErrorBundle((Exception) e));
            UserDetailsPresenter.this.showViewRetry();
        }

        @Override
        public void onNext(User user) {
            UserDetailsPresenter.this.showUserDetailsInView(user);
        }
    }
}
