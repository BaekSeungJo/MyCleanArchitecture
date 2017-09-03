package com.example.presentation.presenter;

import android.support.annotation.NonNull;

import com.example.domain.User;
import com.example.domain.exeception.ErrorBundle;
import com.example.domain.interactor.GetUserListUseCase;
import com.example.presentation.exception.ErrorMessageFactory;
import com.example.presentation.internal.di.PerActivity;
import com.example.presentation.mapper.UserModelDataMapper;
import com.example.presentation.model.UserModel;
import com.example.presentation.view.UserListView;

import java.util.Collection;

import javax.inject.Inject;

/**
 * Created by plnc on 2017-06-26.
 */

@PerActivity
public class UserListPresenter implements Presenter {

    private UserListView userListView;
    private final GetUserListUseCase getUserListUseCase;
    private final UserModelDataMapper userModelDataMapper;

    @Inject
    public UserListPresenter(GetUserListUseCase getUserListUseCase, UserModelDataMapper userModelDataMapper) {
        this.getUserListUseCase = getUserListUseCase;
        this.userModelDataMapper = userModelDataMapper;
    }

    public void setView(@NonNull UserListView userListView) {
        this.userListView = userListView;
    }

    @Override
    public void resume() {

    }

    @Override
    public void pause() {

    }

    public void initialize() {
        this.loadUserList();
    }

    private void loadUserList() {
        this.hideViewRetry();
        this.showViewLoading();
        this.getUserList();
    }

    public void onUserClicked(UserModel userModel) {
        this.userListView.viewUser(userModel);
    }

    private void showViewLoading() {
        this.userListView.showLoading();
    }

    private void hideViewLoading() {
        this.userListView.hideLoading();
    }

    private void showViewRetry() {
        this.userListView.showRetry();
    }

    private void hideViewRetry() {
        this.userListView.hideRetry();
    }

    private void showErrorMessage(ErrorBundle errorBundle) {
        String errorMessage = ErrorMessageFactory.create(this.userListView.getContext(), errorBundle.getException());
        this.userListView.showError(errorMessage);
    }

    private void showUsersCollectionView(Collection<User> usersCollection) {
        final Collection<UserModel> userModelCollection = this.userModelDataMapper.transform(usersCollection);
        this.userListView.renderUserList(userModelCollection);
    }

    private void getUserList() {
        this.getUserListUseCase.execute(userListCallback);
    }

    private final GetUserListUseCase.Callback userListCallback = new GetUserListUseCase.Callback() {
        @Override
        public void onUserListLoaded(Collection<User> userCollection) {
            UserListPresenter.this.showUsersCollectionView(userCollection);
            UserListPresenter.this.hideViewLoading();
        }

        @Override
        public void onError(ErrorBundle errorBundle) {
            UserListPresenter.this.hideViewLoading();
            UserListPresenter.this.showErrorMessage(errorBundle);
            UserListPresenter.this.showViewRetry();
        }
    };
}
