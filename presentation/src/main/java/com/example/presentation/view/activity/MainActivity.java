package com.example.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.presentation.R;
import com.example.presentation.navigation.Navigator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private Navigator navigator;
    @BindView(R.id.btn_LoadData) Button btn_LoadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        this.initialize();
    }

    private void initialize() {
        this.navigator = new Navigator();
    }

    @OnClick(R.id.btn_LoadData)
    void navigateToUserList() {
        this.navigator.navigateToUserList(this);
    }
}
