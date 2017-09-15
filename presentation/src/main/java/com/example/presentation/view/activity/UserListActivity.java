package com.example.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.presentation.R;
import com.example.presentation.internal.di.HasComponent;
import com.example.presentation.internal.di.components.DaggerUserComponent;
import com.example.presentation.internal.di.components.UserComponent;
import com.example.presentation.model.UserModel;
import com.example.presentation.view.fragment.UserListFragment;

public class UserListActivity extends BaseActivity implements HasComponent<UserComponent>,
        UserListFragment.UserListListener {

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, UserListActivity.class);
    }

    private UserComponent userComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_layout);

        this.initializeInjector();

        if(savedInstanceState == null) {
            addFragment(R.id.fragmentContainer, new UserListFragment());
        }
    }

    private void initializeInjector() {
        this.userComponent = DaggerUserComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
    }

    @Override
    public void onUserClicked(UserModel userModel) {
        this.navigator.navigateToUserDetails(this, userModel.getUserId());
    }

    @Override
    public UserComponent getComponent() {
        return userComponent;
    }

}
