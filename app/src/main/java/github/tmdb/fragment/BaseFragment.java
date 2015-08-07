package github.tmdb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 @author IvanBakach
 @version on 31.01.2015
 */
public class BaseFragment extends AbstractFragment{

    public static final String EXTRA_KEY = "extra_key";

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
