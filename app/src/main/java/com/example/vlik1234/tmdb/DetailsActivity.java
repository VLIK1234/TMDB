package com.example.vlik1234.tmdb;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlik1234.tmdb.bo.Film;
import com.example.vlik1234.tmdb.helper.DataManager;
import com.example.vlik1234.tmdb.image.ImageLoader;
import com.example.vlik1234.tmdb.processing.BitmapProcessor;
import com.example.vlik1234.tmdb.processing.FilmProcessor;
import com.example.vlik1234.tmdb.source.HttpDataSource;
import com.example.vlik1234.tmdb.source.TMDBDataSource;
import com.example.vlik1234.tmdb.ui.TextViewSpan;

import org.json.JSONException;
import org.w3c.dom.Text;

import java.io.InterruptedIOException;
import java.util.Objects;


public class DetailsActivity extends ActionBarActivity implements DataManager.Callback<Film> {

    static class ViewHolder {
        TextView title;
        TextView overview;
        TextView genres;
        ImageView poster;
    }
    ViewHolder holder = new ViewHolder();

    private FilmProcessor mFilmProcessor = new FilmProcessor();
    private ImageLoader mImageLoader;
    private final Object mDelayedLock = new Object();

    String id_film = "";
    int finalHeight, finalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        final HttpDataSource dataSource = getHttpDataSource();
        final FilmProcessor processor = getProcessor();
        mImageLoader = ImageLoader.get(DetailsActivity.this);
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
    public void onDone(Film data){

        holder.title = (TextView) findViewById(R.id.title);
        holder.overview = (TextView) findViewById(R.id.overview);
        holder.genres = (TextView) findViewById(R.id.genres);

        holder.title.setText(data.getTitle());
        if (!data.getOverview().equals("null")) holder.overview.setText(data.getOverview());
        try {
            holder.genres.setText(data.getGenres());
        } catch (JSONException e) {
            e.printStackTrace();//TODO do normal exeption post
        }

        final String urlPoster = data.getPosterPath(Film.SizePoster.w342);
        holder.poster.setImageBitmap(null);
        holder.poster.setTag(urlPoster);

        mImageLoader.loadAndDisplay(urlPoster, holder.poster);


        final ViewTreeObserver vto = holder.poster.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                holder.poster.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                finalHeight = holder.poster.getMeasuredHeight();
                finalWidth = holder.poster.getMeasuredWidth();
                makeSpan();
            }
        });
        //Bitmap bitmap = ((BitmapDrawable) holder.poster.getDrawable()).getBitmap();
        //colorize(bitmap);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

    private void colorize(Bitmap image) {
        Palette palette = Palette.generate(image);
        applyPalette(palette);
    }

    private void makeSpan() {
        String plainText =  holder.overview.getText().toString();
        Spanned htmlText = Html.fromHtml(plainText);
        SpannableString mSpannableString= new SpannableString(htmlText);

        int allTextStart = 0;
        int allTextEnd = htmlText.length() - 1;

        /**
         * Calculate the lines number = image height.
         * You can improve it... it is just an example
         */
        int lines;
        Rect bounds = new Rect();
        holder.overview.getPaint().getTextBounds(plainText.substring(0,10), 0, 1, bounds);

        //float textLineHeight = mTextView.getPaint().getTextSize();
        float fontSpacing = holder.overview.getPaint().getFontSpacing();
        lines = (int) (finalHeight/(fontSpacing));

        /**
         * Build the layout with LeadingMarginSpan2
         */
        TextViewSpan span = new TextViewSpan(lines, finalWidth +10 );
        mSpannableString.setSpan(span, allTextStart, allTextEnd, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        holder.overview.setText(mSpannableString);

    }

    private void applyPalette(Palette palette) {
        getWindow().setBackgroundDrawable(new ColorDrawable(palette.getDarkMutedColor().getRgb()));

        holder.title.setTextColor(palette.getVibrantColor().getRgb());

        holder.overview.setTextColor(palette.getLightVibrantColor().getRgb());


    }
}
