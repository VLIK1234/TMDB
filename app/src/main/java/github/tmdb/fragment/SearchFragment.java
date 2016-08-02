package github.tmdb.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import by.istin.android.xcore.fragment.collection.RecyclerViewFragment;
import by.istin.android.xcore.model.CursorModel;
import by.istin.android.xcore.provider.ModelContract;
import github.tmdb.R;
import github.tmdb.adapter.SearchAdapter;
import github.tmdb.database.cursor.CastCursor;
import github.tmdb.database.cursor.SearchCursor;
import github.tmdb.database.model.SearchItem;
import github.tmdb.database.processor.SearchProcessor;

/**
 * @author Ivan Bakach
 * @version on 04.06.2016
 */
public class SearchFragment extends RecyclerViewFragment<RecyclerView.ViewHolder, SearchAdapter, SearchCursor> {

    public static final String KEY_URL = "Url";

    public static SearchFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString(KEY_URL, url);
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public SearchAdapter createAdapter(FragmentActivity fragmentActivity, SearchCursor cursor) {
        return new SearchAdapter(cursor);
    }

    @Override
    protected RecyclerView.LayoutManager createLayoutManager() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(OrientationHelper.VERTICAL);
        return layoutManager;
    }

    @Override
    public void swap(SearchAdapter searchAdapter, SearchCursor cursor) {
        searchAdapter.swapCursor(cursor);
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_recycler_main;
    }

    @Override
    public Uri getUri() {
        return ModelContract.getUri(SearchItem.class);
    }

    @Override
    public String getUrl() {
        return getArguments().getString(KEY_URL);
    }

    @Override
    public String getProcessorKey() {
        return SearchProcessor.APP_SERVICE_KEY;
    }

    @Override
    public CursorModel.CursorModelCreator<SearchCursor> getCursorModelCreator() {
        return SearchCursor.CREATOR;
    }
}
