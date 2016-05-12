package github.tmdb.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import github.tmdb.R;

/**
 * @author Ivan Bakach
 * @version on 26.03.2016
 */
public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction().add(R.id.movies_container,
                Fragment.instantiate(getContext(), HomeMoviesFragment.class.getName())).commit();
        fragmentManager.beginTransaction().add(R.id.series_container,
                Fragment.instantiate(getContext(), HomeSeriesFragment.class.getName())).commit();
        fragmentManager.beginTransaction().add(R.id.people_container,
                Fragment.instantiate(getContext(), HomeCastFragment.class.getName())).commit();

    }
}
