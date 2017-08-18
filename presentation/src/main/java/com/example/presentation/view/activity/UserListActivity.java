package com.example.presentation.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import com.example.presentation.R;
import com.example.presentation.model.UserModel;
import com.example.presentation.navigation.Navigator;
import com.example.presentation.view.fragment.UserListFragment;

public class UserListActivity extends BaseActivity implements UserListFragment.UserListListener {

    private Navigator navigator;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, UserListActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_user_list);

        this.initialize();
    }

    @Override
    public void onUserClicked(UserModel userModel) {
        this.navigator.navigateToUserDetails(this, userModel.getUserId());
    }

    private void initialize() {
        this.navigator = new Navigator();
    }
}
