package github.tmdb.helper;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;

import github.tmdb.fragment.ErrorDialogFragment;

/**
 @author IvanBakach
 @version on 26.01.2015
 */
public class ErrorHelper {

    public static void showDialog(String message, FragmentTransaction fragmentTransaction) {
        DialogFragment errorDialog;
        errorDialog = ErrorDialogFragment.newInstance(message);
        if (fragmentTransaction != null) {
            errorDialog.show(fragmentTransaction, message);
        }
        if (fragmentTransaction != null) {
            fragmentTransaction.commit();
        }
    }
}
