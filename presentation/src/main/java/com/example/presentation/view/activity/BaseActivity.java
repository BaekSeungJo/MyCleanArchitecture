package com.example.presentation.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.example.presentation.ActivityComponent;
import com.example.presentation.ActivityModule;
import com.example.presentation.DaggerActivityComponent;
import com.example.presentation.navigation.Navigator;

import javax.inject.Inject;

/**
 * Created by plnc on 2017-06-28.
 */

public class BaseActivity extends AppCompatActivity {

    ActivityComponent activityComponent;
    @Inject Navigator navigator;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.activityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule())
                .build();
        this.activityComponent.inject(this);
    }

    protected void addFragment(int containerViewId, Fragment fragment) {
        FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
        ft.add(containerViewId, fragment);
        ft.commit();
    }
}
