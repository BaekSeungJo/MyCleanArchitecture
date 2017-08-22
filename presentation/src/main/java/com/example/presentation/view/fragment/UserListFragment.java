package com.example.presentation.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

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
import com.example.domain.interactor.GetUserListUseCase;
import com.example.domain.interactor.GetUserListUseCaseImpl;
import com.example.domain.repository.UserRepository;
import com.example.presentation.R;
import com.example.presentation.UIThread;
import com.example.presentation.mapper.UserModelDataMapper;
import com.example.presentation.model.UserModel;
import com.example.presentation.presenter.UserListPresenter;
import com.example.presentation.view.UserListView;
import com.example.presentation.view.adapter.UserAdapter;

import java.util.Collection;

/**
 * Created by plnc on 2017-06-27.
 */

public class UserListFragment extends BaseFragment implements UserListView {

    public interface UserListListener {
        void onUserClicked(final UserModel userModel);
    }

    private UserListPresenter userListPresenter;

    private ListView lv_users;
    private RelativeLayout rl_progress;
    private RelativeLayout rl_retry;
    private Button bt_retry;

    private UserAdapter userAdapter;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_user_list, container, true);

        this.lv_users = (ListView) fragmentView.findViewById(R.id.lv_users);
        this.lv_users.setOnItemClickListener(this.userOnItemClickListener);
        this.rl_progress = (RelativeLayout) fragmentView.findViewById(R.id.rl_progress);
        this.rl_retry = (RelativeLayout) fragmentView.findViewById(R.id.rl_retry);
        this.bt_retry = (Button) fragmentView.findViewById(R.id.bt_retry);
        this.bt_retry.setOnClickListener(this.retryOnClickListener);

        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        userListPresenter.initialize();
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
    protected void initializePresenter() {
        ThreadExecutor threadExecutor = JobExecutor.getInstance();
        PostExecutionThread postExecutionThread = UIThread.getInstance();

        JsonSerializer userCacheSerializer = new JsonSerializer();
        UserCache userCache = UserCacheImpl.getInstance(getActivity(), userCacheSerializer, FileManager.getInstance(), threadExecutor);
        UserDataStoreFactory userDataStoreFactory = new UserDataStoreFactory(this.getContext(), userCache);
        UserEntityDataMapper userEntityDataMapper = new UserEntityDataMapper();
        UserRepository userRepository = UserDataRepository.getInstance(userDataStoreFactory, userEntityDataMapper);

        GetUserListUseCase getUserListUseCase = new GetUserListUseCaseImpl(userRepository, threadExecutor, postExecutionThread);
        UserModelDataMapper userModelDataMapper = new UserModelDataMapper();

        this.userListPresenter = new UserListPresenter(this, getUserListUseCase, userModelDataMapper);
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
                this.userAdapter = new UserAdapter(getActivity(), userModelCollection);
            } else {
                this.userAdapter.setUsersCollection(userModelCollection);
            }
            this.lv_users.setAdapter(userAdapter);
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
        if(this.userListPresenter != null) {
            this.userListPresenter.initialize();
        }
    }

    private void onUserClicked(UserModel userModel) {
        if(this.userListPresenter != null) {
            this.userListPresenter.onUserClicked(userModel);
        }
    }


    private final View.OnClickListener retryOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            UserListFragment.this.loadUserList();
        }
    };

    private final AdapterView.OnItemClickListener userOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UserModel userModel = (UserModel) UserListFragment.this.userAdapter.getItem(position);
            UserListFragment.this.onUserClicked(userModel);
        }
    };


}
