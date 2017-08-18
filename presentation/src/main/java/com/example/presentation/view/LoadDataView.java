package com.example.presentation.view;

import android.content.Context;

/**
 * Created by plnc on 2017-06-27.
 */

public interface LoadDataView {

    void showLoading();

    void hideLoading();

    void showRetry();

    void hideRetry();

    void showError(String message);

    Context getContext();
}
