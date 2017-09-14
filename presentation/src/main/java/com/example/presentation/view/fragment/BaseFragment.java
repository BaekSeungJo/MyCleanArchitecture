package com.example.presentation.view.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.example.presentation.internal.di.HasComponent;

/**
 * Created by plnc on 2017-06-27.
 */

public abstract class BaseFragment extends Fragment {

    protected void showToastMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    protected <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }
}
