package com.example.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.data.cache.FileManager;
import com.example.data.cache.UserCache;
import com.example.data.cache.UserCacheImpl;
import com.example.data.cache.serializer.JsonSerializer;
import com.example.data.entity.mapper.UserEntityDataMapper;
import com.example.data.executor.JobExecutor;
import com.example.data.repository.UserDataRepository;
import com.example.data.repository.datasource.UserDataStoreFactory;
import com.example.domain.executor.PostExecutionThread;
import com.example.domain.executor.ThreadExecutor;
import com.example.domain.interactor.GetUserDetailsUseCase;
import com.example.domain.interactor.GetUserDetailsUseCaseImpl;
import com.example.domain.repository.UserRepository;
import com.example.presentation.R;
import com.example.presentation.UIThread;
import com.example.presentation.mapper.UserModelDataMapper;
import com.example.presentation.model.UserModel;
import com.example.presentation.presenter.UserDetailsPresenter;
import com.example.presentation.view.UserDetailsView;
import com.example.presentation.view.component.AutoLoadImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by plnc on 2017-06-28.
 */

public class UserDetailsFragment extends BaseFragment implements UserDetailsView {

    private static final String ARGUMENT_KEY_USER_ID = "org.android10.ARGUMENT_USER_ID";

    private int userId;
    private UserDetailsPresenter userDetailsPresenter;

    @BindView(R.id.iv_cover) AutoLoadImageView iv_cover;
    @BindView(R.id.tv_fullname) TextView tv_fullname;
    @BindView(R.id.tv_email) TextView tv_email;
    @BindView(R.id.tv_followers) TextView tv_followers;
    @BindView(R.id.tv_description) TextView tv_description;
    @BindView(R.id.rl_progress) RelativeLayout rl_progress;
    @BindView(R.id.rl_retry) RelativeLayout rl_retry;
    @BindView(R.id.bt_retry) Button bt_retry;

    public UserDetailsFragment() {
        super();
    }

    public static UserDetailsFragment newInstanace(int userId) {
        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();

        Bundle argumentsBundle = new Bundle();
        argumentsBundle.putInt(ARGUMENT_KEY_USER_ID, userId);
        userDetailsFragment.setArguments(argumentsBundle);

        return userDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initialize();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);
        ButterKnife.bind(this, fragmentView);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.loadUserDetails();
    }

    @Override
    public void onResume() {
        super.onResume();
        this.userDetailsPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.userDetailsPresenter.pause();
    }

    @Override
    void initializePresenter() {
        ThreadExecutor threadExecutor = JobExecutor.getInstance();
        PostExecutionThread postExecutionThread = UIThread.getInstance();

        JsonSerializer userCacheSerializer = new JsonSerializer();
        UserCache userCache = UserCacheImpl.getInstance(getActivity(), userCacheSerializer, FileManager.getInstance(), threadExecutor);
        UserDataStoreFactory userDataStoreFactory =
                new UserDataStoreFactory(this.getContext(), userCache);
        UserEntityDataMapper userEntityDataMapper = new UserEntityDataMapper();
        UserRepository userRepository = UserDataRepository.getInstance(userDataStoreFactory, userEntityDataMapper);

        GetUserDetailsUseCase getUserDetailsUseCase = new GetUserDetailsUseCaseImpl(userRepository, threadExecutor, postExecutionThread);
        UserModelDataMapper userModelDataMapper = new UserModelDataMapper();

        this.userDetailsPresenter =
                new UserDetailsPresenter(this, getUserDetailsUseCase, userModelDataMapper);
    }

    @Override
    public void renderUser(UserModel user) {
        if (user != null) {
            this.iv_cover.setImageUrl(user.getCoverUrl());
            this.tv_fullname.setText(user.getFullName());
            this.tv_email.setText(user.getEmail());
            this.tv_followers.setText(String.valueOf(user.getFollowers()));
            this.tv_description.setText(user.getDescription());
        }
    }

    @Override
    public void showLoading() {
        this.rl_progress.setVisibility(View.VISIBLE);
        this.getActivity().setProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void hideLoading() {
        this.rl_progress.setVisibility(View.GONE);
        this.getActivity().setProgressBarIndeterminateVisibility(false);
    }

    @Override
    public void showRetry() {
        this.rl_retry.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRetry() {
        this.rl_retry.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        this.showToastMessage(message);
    }

    @Override
    public Context getContext() {
        return getActivity().getApplicationContext();
    }

    private void initialize() {
        this.userId = getArguments().getInt(ARGUMENT_KEY_USER_ID);
    }

    private void loadUserDetails() {
        if(this.userDetailsPresenter != null) {
            this.userDetailsPresenter.initialize(this.userId);
        }
    }

    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        this.loadUserDetails();
    }
}
