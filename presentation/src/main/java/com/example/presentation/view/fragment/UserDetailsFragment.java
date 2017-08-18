package com.example.presentation.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.presentation.R;
import com.example.presentation.model.UserModel;
import com.example.presentation.presenter.UserDetailsPresenter;
import com.example.presentation.view.UserDetailsView;

/**
 * Created by plnc on 2017-06-28.
 */

public class UserDetailsFragment extends BaseFragment implements UserDetailsView {

    private static final String ARGUMENT_KEY_USER_ID = "org.android10.ARGUMENT_USER_ID";

    private int userId;
    private UserDetailsPresenter userDetailsPresenter;

    private TextView tv_fullname;
    private TextView tv_email;
    private TextView tv_followers;
    private TextView tv_description;
    private RelativeLayout rl_progress;
    private RelativeLayout rl_retry;
    private Button bt_retry;

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

    private void initialize() {
        this.userId = getArguments().getInt(ARGUMENT_KEY_USER_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user_details, container, false);

        this.iv_
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    void initializePresenter() {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showRetry() {

    }

    @Override
    public void hideRetry() {

    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void renderUser(UserModel userModel) {

    }
}
