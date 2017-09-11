package com.example.presentation.presenter;

import android.support.annotation.NonNull;

import com.example.domain.User;
import com.example.domain.exeception.DefaultErrorBundle;
import com.example.domain.exeception.ErrorBundle;
import com.example.domain.interactor.DefaultSubscriber;
import com.example.domain.interactor.GetUserListUseCase;
import com.example.domain.interactor.UseCase;
import com.example.presentation.exception.ErrorMessageFactory;
import com.example.presentation.internal.di.PerActivity;
import com.example.presentation.mapper.UserModelDataMapper;
import com.example.presentation.model.UserModel;
import com.example.presentation.view.UserListView;

import java.util.Collection;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Created by plnc on 2017-06-26.
 */

@PerActivity
public class UserListPresenter extends DefaultSubscriber<List<User>> implements Presenter {

    private UserListView userListView;
    private final UseCase getUserListUseCase;
    private final UserModelDataMapper userModelDataMapper;

    @Inject
    public UserListPresenter(@Named("userList") UseCase getUserListUseCase, UserModelDataMapper userModelDataMapper) {
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

    @Override
    public void destroy() {
        this.getUserListUseCase.unsubscribe();
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
        this.getUserListUseCase.execute(this);
    }

    @Override
    public void onCompleted() {
        this.hideViewLoading();
    }

    @Override
    public void onError(Throwable e) {
        this.hideViewLoading();
        this.showErrorMessage(new DefaultErrorBundle((Exception) e));
        this.showViewRetry();
    }

    @Override
    public void onNext(List<User> users) {
        this.showUsersCollectionView(users);
    }
}
