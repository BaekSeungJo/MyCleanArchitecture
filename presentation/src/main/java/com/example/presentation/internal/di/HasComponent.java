package com.example.presentation.internal.di;

/**
 * Created by 1 on 2017-09-04.
 */

/**
 *
 * Interface representing a contract for clients that contais a component for dependency injection.
 */
public interface HasComponent<C> {
    C getComponent();
}
