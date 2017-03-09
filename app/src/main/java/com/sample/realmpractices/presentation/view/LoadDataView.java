package com.sample.realmpractices.presentation.view;

import android.content.Context;

/**
 * Created by app on 3/9/17.
 */

public interface LoadDataView {

    void showLoading();

    void hideLoading();

    void showRetry();

    void hideRetry();

    void showError(String message);

    Context getContext();
}
