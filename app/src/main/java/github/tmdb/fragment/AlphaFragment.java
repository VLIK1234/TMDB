package github.tmdb.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import github.tmdb.CoreApplication;
import github.tmdb.R;
import github.tmdb.api.Language;
import github.tmdb.app.AbstractActivity;
import github.tmdb.utils.PreferenceUtil;

/**
 @author IvanBakach
 @version on 21.01.2015
 */
public class AlphaFragment extends DialogFragment implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup radioGroup;
    private Activity activity;
    private final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_language_setting,null);
        radioGroup = (RadioGroup) v.findViewById(R.id.rg_language);
        radioGroup.setOnCheckedChangeListener(this);
        activity = getActivity();
        LoadPreferences();

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.language_setting))
                .setCancelable(true)
                .setView(v)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return adb.create();
    }


    private void SavePreferences(String key, int value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("MY_SHARED_PREF", activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void LoadPreferences() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("MY_SHARED_PREF", activity.MODE_PRIVATE);
        int savedRadioIndex = sharedPreferences.getInt(KEY_SAVED_RADIO_BUTTON_INDEX, 0);
        RadioButton savedCheckedRadioButton = (RadioButton) radioGroup.getChildAt(savedRadioIndex);
        savedCheckedRadioButton.setChecked(true);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
        String language = checkedRadioButton.getText().toString();
        PreferenceUtil.putString(getString(R.string.key_language), language);
        int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);
        SavePreferences(KEY_SAVED_RADIO_BUTTON_INDEX, checkedIndex);
    }
}
