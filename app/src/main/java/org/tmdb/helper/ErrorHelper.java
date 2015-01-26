package org.tmdb.helper;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

import org.tmdb.ErrorDialog;

/**
 * Created by ASUS on 26.01.2015.
 */
public class ErrorHelper{

    public static void showDialog(String message, FragmentTransaction fragmentTransaction) {
        DialogFragment errorDialog;
        errorDialog = ErrorDialog.newInstance(message);
        if (fragmentTransaction!=null) {
            errorDialog.show(fragmentTransaction, "alpha_fragment");
        }
    }
}
