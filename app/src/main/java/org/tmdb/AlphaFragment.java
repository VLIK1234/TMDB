package org.tmdb;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tmdb.vlik1234.R;

/**
 * Created by ASUS on 21.01.2015.
 */
public class AlphaFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String EXTRA_MESSAGE = "extra_message";

    //Empty constructor required for DialogFragment - http://android-developers.blogspot.com/2012/05/using-dialogfragments.html
    public AlphaFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_alpha, container);
        getDialog().setTitle("Choose language");
        return v;
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
