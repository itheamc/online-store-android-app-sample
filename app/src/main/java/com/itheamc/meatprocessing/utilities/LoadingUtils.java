package com.itheamc.meatprocessing.utilities;

import android.view.View;
import android.widget.ProgressBar;

public class LoadingUtils {

    // To show progress bar
    public static void showProgress(ProgressBar progressBar) {
        progressBar.setVisibility(View.INVISIBLE);
    }

    // to dismiss progress
    public static void dismissProgress(ProgressBar progressBar) {
        progressBar.setVisibility(View.GONE);
    }

}
