package com.example.presentation.presenter;

import android.support.annotation.NonNull;

import com.example.domain.User;
import com.example.domain.exeception.ErrorBundle;
import com.example.domain.interactor.GetUserDetailsUseCase;
import com.example.presentation.exception.ErrorMessageFactory;
import com.example.presentation.internal.di.PerActivity;
import com.example.presentation.mapper.UserModelDataMapper;
import com.example.presentation.model.UserModel;
import com.example.presentation.view.UserDetailsView;

import javax.inject.Inject;

/**
 * Created by plnc on 2017-06-28.
 */

@PerActivity
public class UserDetailsPresenter implements Presenter {

    private int userId;

    private UserDetailsView viewDetailsView;
    private final GetUserDetailsUseCase getUserDetailsUseCase;
    private final UserModelDataMapper userModelDataMapper;

    @Inject
    public UserDetailsPresenter(GetUserDetailsUseCase getUserDetailsUseCase, UserModelDataMapper userModelDataMapper) {
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

    public void initialize(int userId) {
        this.userId =userId;
        this.loadUserDetails();
    }

    private void loadUserDetails() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserDetails();
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
        String errorMessage = ErrorMessageFactory.create(this.viewDetailsView.getContext(), errorBundle.getException());
        this.viewDetailsView.showError(errorMessage);
    }

    private void showUserDetailsInView(User user) {
        final UserModel userModel = this.userModelDataMapper.transform(user);
        this.viewDetailsView.renderUser(userModel);
    }

    private void getUserDetails() {
        this.getUserDetailsUseCase.execute(this.userId, this.userDetailsCallback);
    }

    private final GetUserDetailsUseCase.Callback userDetailsCallback = new GetUserDetailsUseCase.Callback() {
        @Override
        public void onUserDataLoaded(User user) {
            UserDetailsPresenter.this.showUserDetailsInView(user);
            UserDetailsPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(ErrorBundle errorBundle) {
            UserDetailsPresenter.this.hideViewLoading();
            UserDetailsPresenter.this.showErrorMessage(errorBundle);
            UserDetailsPresenter.this.showViewRetry();
        }
    };
}
