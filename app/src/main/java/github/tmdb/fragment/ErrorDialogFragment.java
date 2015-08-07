package github.tmdb.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import github.tmdb.R;

/**
 @author IvanBakach
 @version on 21.01.2015
 */
public class ErrorDialogFragment extends DialogFragment implements DialogInterface.OnClickListener, DialogInterface.OnCancelListener {

    private static final String EXTRA_MESSAGE = "extra_message";

    public ErrorDialogFragment() {

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String textMessage = getMessage();
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.error_title_dialog))
                .setPositiveButton(getString(R.string.ok_button), this)
                .setNegativeButton(getString(R.string.cancel_button), this)
                .setMessage(textMessage);
        return adb.create();
    }

    public static DialogFragment newInstance(String message) {
        ErrorDialogFragment errorDialog = new ErrorDialogFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
        errorDialog.setArguments(args);
        return errorDialog;
    }

    private String getMessage() {
        return getArguments().getString(EXTRA_MESSAGE);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onCancel(dialog);
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }
}
