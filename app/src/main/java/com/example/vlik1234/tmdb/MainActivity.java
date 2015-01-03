package com.example.vlik1234.tmdb;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlik1234.tmdb.bo.Film;
import com.example.vlik1234.tmdb.helper.DataManager;
import com.example.vlik1234.tmdb.image.ImageLoader;
import com.example.vlik1234.tmdb.processing.FilmArrayProcessor;
import com.example.vlik1234.tmdb.source.HttpDataSource;
import com.example.vlik1234.tmdb.source.TMDBDataSource;

import java.util.List;


public class MainActivity extends ActionBarActivity implements DataManager.Callback<List<Film>>, Parcelable{
    static class ViewHolder {
        TextView titlenDate;
        SwipeRefreshLayout mSwipeRefreshLayout;
        AbsListView listView;
    }
    ViewHolder holder = new ViewHolder();

    private ArrayAdapter mAdapter;
    private FilmArrayProcessor mFilmArrayProcessor = new FilmArrayProcessor();

    private ImageLoader mImageLoader;

    String selectItemID ="";

    public MainActivity(){

    }

    private MainActivity(String selectItemID){
        this.selectItemID = selectItemID;
    }

    private MainActivity(Parcel in){
        String[] data = new String[1];
        in.readStringArray(data);
        this.selectItemID = data[0];
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageLoader = ImageLoader.get(MainActivity.this);
        holder.mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        final HttpDataSource dataSource = getHttpDataSource();
        final FilmArrayProcessor processor = getProcessor();
        holder.mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update(dataSource, processor);
            }
        });
        update(dataSource, processor);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{selectItemID});
    }

    public static final Parcelable.Creator<MainActivity> CREATOR = new Parcelable.Creator<MainActivity>() {

        @Override
        public MainActivity createFromParcel(Parcel source) {
            return new MainActivity(source);
        }

        @Override
        public MainActivity[] newArray(int size) {
            return new MainActivity[size];
        }

    };

    private FilmArrayProcessor getProcessor() {
        return mFilmArrayProcessor;
    }

   private HttpDataSource getHttpDataSource() {
        return  new TMDBDataSource();
    }

    private void update(HttpDataSource dataSource, FilmArrayProcessor processor) {
        DataManager.loadData(MainActivity.this,
                getUrl(),
                dataSource,
                processor);
    }

    private String getUrl() {
        return ApiTMDB.NOW_PLAYING_GET;
    }

    @Override
    public void onDataLoadStart() {
        if (!holder.mSwipeRefreshLayout.isRefreshing()) {
            findViewById(android.R.id.progress).setVisibility(View.VISIBLE);
        }
        findViewById(android.R.id.empty).setVisibility(View.GONE);
    }

    private List<Film> mData;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(List<Film> data) {
        if (holder.mSwipeRefreshLayout.isRefreshing()) {
            holder.mSwipeRefreshLayout.setRefreshing(false);
        }
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        if (data == null || data.isEmpty()) {
            findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
        }
        holder.listView = (AbsListView) findViewById(android.R.id.list);
        if (mAdapter == null) {
            mData = data;

            mAdapter = new ArrayAdapter<Film>(this, R.layout.adapter_item, android.R.id.text1, data) {

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if (convertView == null) {
                        convertView = View.inflate(MainActivity.this, R.layout.adapter_item, null);
                    }
                    Film item = getItem(position);
                    holder.titlenDate = (TextView) convertView.findViewById(R.id.title);
                    holder.titlenDate.setText(item.getTitlenDate());

                    convertView.setTag(item.getId());
                    final ImageView poster = (ImageView) convertView.findViewById(R.id.poster);
                    final String url = item.getPosterPath(Film.SizePoster.w185);
                    mImageLoader.loadAndDisplay(url, poster);
                    return convertView;
                }

            };
            holder.listView.setAdapter(mAdapter);
            holder.listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    switch (scrollState) {
                        case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                            mImageLoader.resume();
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                            mImageLoader.pause();
                            break;
                        case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                            mImageLoader.pause();
                            break;
                    }
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
            holder.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Film item = (Film) mAdapter.getItem(position);
                    selectItemID = item.getId().toString();
                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                    intent.putExtra("MainActivity", new MainActivity(selectItemID));
                    startActivity(intent);
                }
            });
        } else {
            mData.clear();
            mData.addAll(data);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        findViewById(android.R.id.progress).setVisibility(View.GONE);
        findViewById(android.R.id.empty).setVisibility(View.GONE);
        TextView errorView = (TextView) findViewById(R.id.error);
        errorView.setVisibility(View.VISIBLE);
        errorView.setText(errorView.getText() + "\n" + e.getMessage());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
