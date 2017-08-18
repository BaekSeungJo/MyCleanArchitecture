package com.example.presentation.view.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.presentation.R;
import com.example.presentation.navigation.Navigator;

public class MainActivity extends BaseActivity {

    private Navigator navigator;
    private Button btn_LoadData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.mapGUI();
        this.initialize();
    }

    private void mapGUI() {
        btn_LoadData = (Button) findViewById(R.id.btn_LoadData);
        btn_LoadData.setOnClickListener(loadDataOnClickListener);
    }

    private void initialize() {
        this.navigator = new Navigator();
    }

    private final View.OnClickListener loadDataOnClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            navigateToUserList();
        }
    };

    private void navigateToUserList() {
        this.navigator.navigateToUserList(this);
    }

}
