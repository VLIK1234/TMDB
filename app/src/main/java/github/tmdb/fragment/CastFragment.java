package github.tmdb.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import by.istin.android.xcore.fragment.collection.RecyclerViewFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import by.istin.android.xcore.source.DataSourceRequest;
import github.tmdb.R;
import github.tmdb.adapter.CastAdapter;
import github.tmdb.adapter.FilmAdapter;
import github.tmdb.app.MainScreenActivity;
import github.tmdb.app.PersonActivity;
import github.tmdb.database.cursor.CastCursor;
import github.tmdb.database.model.Cast;
import github.tmdb.database.processor.CastProcessor;
import github.tmdb.listener.IClickCallback;

/**
 * @author IvanBakach
 * @version on 13.11.2015
 */
public class CastFragment extends RecyclerViewFragment<CastAdapter.ViewHolder, CastAdapter, CastCursor> implements FilmAdapter.ITouch, IClickCallback {

    private static final String MOVIE_ID_KEY = "movie_id";
    private long movieId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            movieId = bundle.getLong(MOVIE_ID_KEY);
        }
    }

    @Override
    public CastAdapter createAdapter(FragmentActivity fragmentActivity, CastCursor cursor) {
        return new CastAdapter(cursor, this);
    }

    public static Fragment newInstance(long movieId) {
        CastFragment fragment = new CastFragment();
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID_KEY, movieId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        return layoutManager;
    }

    @Override
    public void swap(CastAdapter castAdapter, CastCursor cursor) {
        castAdapter.swapCursor(cursor);
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_recycler_main;
    }

    @Override
    public long getCacheExpiration() {
        return 0L;
    }

    @Override
    public void onError(Exception exception, DataSourceRequest dataSourceRequest) {

    }

    @Override
    public CursorModel.CursorModelCreator<CastCursor> getCursorModelCreator() {
        return CastCursor.CREATOR;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getUri(Cast.class);
    }

    @Override
    public String getUrl() {
        return "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=f413bc4bacac8dff174a909f8ef535ae";
    }

    @Override
    public String getProcessorKey() {
        return CastProcessor.APP_SERVICE_KEY;
    }

    @Override
    public String getOrder() {
        return null;
    }

    @Override
    public void touchAction(long idItem) {
//        ((MainScreenActivity) getActivity()).setCurrentFragment(MovieDetailFragment.newInstance(idItem), true);
    }

    @Override
    public void onClickCallback(View view) {
        long personId = (long) view.getTag();
        ((MainScreenActivity)getActivity()).setCurrentFragment(PersonFragment.newInstance(personId), true);
//        Intent intent = new Intent(getActivity(), PersonActivity.class);
//        intent.putExtra(PersonFragment.PERSON_ID, personId);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
    }
}
