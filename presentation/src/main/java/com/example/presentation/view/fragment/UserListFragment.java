package com.example.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.presentation.R;
import com.example.presentation.internal.di.components.DaggerUserComponent;
import com.example.presentation.internal.di.components.UserComponent;
import com.example.presentation.internal.di.modules.ActivityModule;
import com.example.presentation.internal.di.modules.UserModule;
import com.example.presentation.model.UserModel;
import com.example.presentation.presenter.UserListPresenter;
import com.example.presentation.view.UserListView;
import com.example.presentation.view.adapter.UsersAdapter;
import com.example.presentation.view.adapter.UsersLayoutManager;

import java.util.Collection;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by plnc on 2017-06-27.
 */

public class UserListFragment extends BaseFragment implements UserListView {

    public interface UserListListener {
        void onUserClicked(final UserModel userModel);
    }

    @Inject UserListPresenter userListPresenter;

    @BindView(R.id.rv_users) RecyclerView rv_users;
    @BindView(R.id.rl_progress) RelativeLayout rl_progress;
    @BindView(R.id.rl_retry) RelativeLayout rl_retry;
    @BindView(R.id.bt_retry) Button bt_retry;

    private UsersAdapter userAdapter;
    private UsersLayoutManager usersLayoutManager;

    private UserListListener userListListener;

    public UserListFragment() {
        super();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof  UserListListener) {
            userListListener = (UserListListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerUserComponent.builder()
                .applicationComponent(getApplication().getApplicationComponent())
                .activityModule(new ActivityModule(getActivity()))
                .userModule(new UserModule())
                .build()
                .inject(this);

        this.initialize();
    }

    private void initialize() {
        this.userListPresenter.setView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user_list, container, true);
        ButterKnife.bind(this, fragmentView);
        setupUI();

        return fragmentView;
    }

    private void setupUI() {
        this.usersLayoutManager = new UsersLayoutManager(getActivity());
        this.rv_users.setLayoutManager(usersLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.loadUserList();
    }

    @Override
    public void onResume() {
        super.onResume();
        userListPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        userListPresenter.pause();
    }

    @Override
    public void showLoading() {
        rl_progress.setVisibility(View.VISIBLE);
        getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        rl_progress.setVisibility(View.GONE);
        getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetry() {
        rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        rl_retry.setVisibility(View.GONE);
    }

    @Override
    public void renderUserList(Collection<UserModel> userModelCollection) {
        if(userModelCollection != null) {
            if(this.userAdapter == null) {
                this.userAdapter = new UsersAdapter(getActivity(), userModelCollection);
            } else {
                this.userAdapter.setUsersCollection(userModelCollection);
            }
            this.userAdapter.setOnItemClickListener(onItemClickListener);
            this.rv_users.setAdapter(userAdapter);
        }
    }

    @Override
    public void viewUser(UserModel userModel) {
        if(this.userListListener != null) {
            this.userListListener.onUserClicked(userModel);
        }
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return this.getActivity().getApplicationContext();
    }

    private void loadUserList() {
        this.userListPresenter.initialize();
    }

    @OnClick(R.id.bt_retry) void onButtonRetryClick() {
        UserListFragment.this.loadUserList();
    }

    private UsersAdapter.OnItemClickListener onItemClickListener =
            new UsersAdapter.OnItemClickListener() {
        @Override
        public void onUserItemClicked(UserModel userModel) {
            if(UserListFragment.this.userListPresenter != null && userModel != null) {
                UserListFragment.this.userListPresenter.onUserClicked(userModel);
            }
        }
    };
}
