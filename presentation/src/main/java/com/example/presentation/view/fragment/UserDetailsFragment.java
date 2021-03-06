package com.example.presentation.view.fragment;

import android.content.Context;
import android.gesture.Prediction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.presentation.R;
import com.example.presentation.internal.di.components.UserComponent;
import com.example.presentation.model.UserModel;
import com.example.presentation.presenter.UserDetailsPresenter;
import com.example.presentation.view.UserDetailsView;
import com.example.presentation.view.component.AutoLoadImageView;
import com.fernandocejas.arrow.checks.Preconditions;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by plnc on 2017-06-28.
 */

public class UserDetailsFragment extends BaseFragment implements UserDetailsView {
    private static final String PARAM_USER_ID = "param_user_id";

    private Unbinder unbinder;

    @Inject UserDetailsPresenter userDetailsPresenter;

    @BindView(R.id.iv_cover) AutoLoadImageView iv_cover;
    @BindView(R.id.tv_fullname) TextView tv_fullname;
    @BindView(R.id.tv_email) TextView tv_email;
    @BindView(R.id.tv_followers) TextView tv_followers;
    @BindView(R.id.tv_description) TextView tv_description;
    @BindView(R.id.rl_progress) RelativeLayout rl_progress;
    @BindView(R.id.rl_retry) RelativeLayout rl_retry;
    @BindView(R.id.bt_retry) Button bt_retry;

    public UserDetailsFragment() {
        setRetainInstance(true);
    }

    public static UserDetailsFragment forUser(int userId) {
        UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(PARAM_USER_ID, userId);
        userDetailsFragment.setArguments(arguments);
        return userDetailsFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getComponent(UserComponent.class).inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);
        unbinder = ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.userDetailsPresenter.setView(this);
        if(savedInstanceState == null) {
            this.loadUserDetails();
        }
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.userDetailsPresenter.destroy();
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
    public Context context() {
        return getActivity().getApplicationContext();
    }

    private void loadUserDetails() {
        if(this.userDetailsPresenter != null) {
            this.userDetailsPresenter.initialize(currentUserId());
        }
    }

    private int currentUserId() {
        final Bundle arguments = getArguments();
        Preconditions.checkNotNull(arguments, "Fragment arguments cannot be null");
        return arguments.getInt(PARAM_USER_ID);
    }


    @OnClick(R.id.bt_retry)
    void onButtonRetryClick() {
        this.loadUserDetails();
    }
}
