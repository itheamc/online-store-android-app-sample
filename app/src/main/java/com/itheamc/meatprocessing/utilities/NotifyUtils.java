package com.itheamc.meatprocessing.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.NavController;

import com.google.android.material.snackbar.Snackbar;

public class NotifyUtils {

    // Method to show snackBars
    public static void showSnackBar(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(view);
        snackbar.setBackgroundTint(Color.BLUE);
        snackbar.setTextColor(Color.WHITE);
        snackbar.show();
    }

    //Method to show snackbar without setting anchorView
    public static void showSnackBarWithoutAnchorView(View view, String message) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }

    // Method to show snackBars along with onClick Action for navhost
    public static void showSnackBarWithAction(NavController navController, int resId, View buttonView, String actionText, String message) {
        Snackbar snackbar = Snackbar.make(buttonView, message, Snackbar.LENGTH_SHORT);
        snackbar.setAnchorView(buttonView)
        .setAction(actionText, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(resId);
            }
        });
        snackbar.show();
    }

    // Method to show snackBars along with onClick Action
    public static void showSnackBarWithAction(View view, String actionText, String message, String intentAction) {
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);
        snackbar.setAction(actionText, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.getContext().startActivity(new Intent(intentAction));
                    }
                });
        snackbar.show();
    }


    // Method to show Toast Message
    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    // Method to show log.d
    public static void logDebug(String tag, String message) {
        Log.d(tag, "logDebug: "+ message);
    }

    // Method to show Log.e
    public static void logError(String tag, String functionName, Exception e) {
        Log.e(tag, functionName +": ", e.fillInStackTrace());
    }
}
