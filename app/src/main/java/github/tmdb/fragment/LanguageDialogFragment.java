package github.tmdb.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import github.tmdb.R;
import github.tmdb.api.Language;
import github.tmdb.utils.PreferenceUtil;

/**
 * @author IvanBakach
 * @version on 05.02.2015
 */
public class LanguageDialogFragment extends DialogFragment implements RadioGroup.OnCheckedChangeListener {
    //TODO Rewrite on settings
    private RadioGroup radioGroup;
    private Activity activity;
    private final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    private static final String LAST_INDEX_CHECK = "LAST_INDEX_CHECK";

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_language_setting, null);
        radioGroup = (RadioGroup) v.findViewById(R.id.rg_language);
        radioGroup.setOnCheckedChangeListener(this);
        activity = getActivity();
        loadPreferences();

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.language_setting))
                .setCancelable(true)
                .setView(v)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.rb_english:
                                PreferenceUtil.putString(getString(R.string.key_language), "en");
                                break;
                            case R.id.rb_russian:
                                PreferenceUtil.putString(getString(R.string.key_language), "ru");
                                break;
                        }
                        Language.setLanguage(Language.getLanguage());
                        dialog.cancel();
                    }
                })
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        return adb.create();
    }


    private void savePreferences(String key, int value) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LAST_INDEX_CHECK, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    private void loadPreferences() {
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LAST_INDEX_CHECK, Context.MODE_PRIVATE);
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
        RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(checkedId);
        String language = checkedRadioButton.getText().toString();
        PreferenceUtil.putString(getString(R.string.key_language), language);
        int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);
        savePreferences(KEY_SAVED_RADIO_BUTTON_INDEX, checkedIndex);
    }
}
