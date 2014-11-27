package com.example.vlik1234.tmdb;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vlik1234.tmdb.bo.Film;
import com.example.vlik1234.tmdb.helper.DataManager;
import com.example.vlik1234.tmdb.processing.BitmapProcessor;
import com.example.vlik1234.tmdb.processing.FilmProcessor;
import com.example.vlik1234.tmdb.source.HttpDataSource;
import com.example.vlik1234.tmdb.source.TMDBDataSource;


public class DetailsActivity extends ActionBarActivity implements DataManager.Callback<Film> {

    static class ViewHolder {
        TextView title;
        TextView overview;
        ImageView poster;
    }
    ViewHolder holder = new ViewHolder();

    private FilmProcessor mFilmProcessor = new FilmProcessor();

    String id_film = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final HttpDataSource dataSource = getHttpDataSource();
        final FilmProcessor processor = getProcessor();

        MainActivity mainActivity = getIntent().getParcelableExtra("MainActivity");
        this.id_film = mainActivity.selectItemID;
        update(dataSource, processor);
        holder.poster = (ImageView) findViewById(R.id.poster);
    }
    private FilmProcessor getProcessor() {
        return mFilmProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return  new TMDBDataSource();
    }

    private void update(HttpDataSource dataSource, FilmProcessor processor) {
        DataManager.loadData(DetailsActivity.this,
                getUrl(),
                dataSource,
                processor);
    }

    private String getUrl() {
        return ApiTMDB.getMovie(id_film);
    }

    @Override
    public void onDataLoadStart() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data) {
        Film item = data;

        holder.title = (TextView) findViewById(R.id.title);
        holder.title.setText(item.getTitle());
        holder.overview = (TextView) findViewById(R.id.overview);
        if(!item.getOverview().equals("null"))holder.overview.setText(item.getOverview());

        final String url = item.getPosterPath(Film.SizePoster.w780);
        holder.poster.setImageBitmap(null);
        holder.poster.setTag(url);
        if (!TextUtils.isEmpty(url)) {
            //TODO add delay and cancel old request or create limited queue
            //TODO create sync Map to check existing request and existing callbacks
            //TODO create separate thread pool for manager
            DataManager.loadData(new DataManager.Callback<Bitmap>() {
                @Override
                public void onDataLoadStart() {

                }

                @Override
                public void onDone(Bitmap bitmap) {
                    if (url.equals(holder.poster.getTag())&&bitmap!=null) {
                        holder.poster.setImageBitmap(bitmap);
                        colorize(bitmap);
                    }
                }

                @Override
                public void onError(Exception e) {
                }
            }, url, new TMDBDataSource(), new BitmapProcessor());
        }
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    private void colorize(Bitmap image) {
        Palette palette = Palette.generate(image);
        applyPalette(palette);
    }

    private void applyPalette(Palette palette) {
        getWindow().setBackgroundDrawable(new ColorDrawable(palette.getDarkMutedColor().getRgb()));

        holder.title.setTextColor(palette.getVibrantColor().getRgb());

        holder.overview.setTextColor(palette.getLightVibrantColor().getRgb());


    }
}
