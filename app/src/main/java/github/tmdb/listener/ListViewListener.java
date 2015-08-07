package github.tmdb.listener;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;
import java.util.Locale;

import github.tmdb.api.ApiTMDB;
import github.tmdb.processing.FilmArrayProcessor;
import github.tmdb.processing.Processor;
import github.tmdb.source.DataSource;
import github.tmdb.source.TMDBDataSource;

/**
 @author IvanBakach
 @version on 02.02.2015
 */

public class ListViewListener extends AbstractScrollListener {

    public ListViewListener(Context context, ListView listView, List data, ArrayAdapter adapter, String url) {
        super.context = context;
        super.listView = listView;
        super.data = data;
        super.adapter = adapter;
        super.url = url;
    }

    @Override
    public String getUrl(int page) {
        StringBuilder controlUrl = new StringBuilder(url);
        controlUrl.append(ApiTMDB.getPage(controlUrl.toString(), page));
        controlUrl.append(ApiTMDB.getLanguage(controlUrl.toString())).append(Locale.getDefault().getLanguage());
        return controlUrl.toString();
    }

    @Override
    public DataSource getDataSource() {
        return new TMDBDataSource();
    }

    @Override
    public Processor getProcessor() {
        return new FilmArrayProcessor();
    }
}
