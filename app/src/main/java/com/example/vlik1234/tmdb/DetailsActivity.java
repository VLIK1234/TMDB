package com.example.vlik1234.tmdb;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlik1234.tmdb.bo.DescriptionOfTheFilm;
import com.example.vlik1234.tmdb.bo.Film;
import com.example.vlik1234.tmdb.helper.DataManager;
import com.example.vlik1234.tmdb.image.ImageLoader;
import com.example.vlik1234.tmdb.processing.FilmProcessor;
import com.example.vlik1234.tmdb.source.HttpDataSource;
import com.example.vlik1234.tmdb.source.TMDBDataSource;

import org.json.JSONException;


public class DetailsActivity extends ActionBarActivity implements DataManager.Callback<Film> {

    static class ViewHolder {
        TextView title;
        TextView genres;
        TextView tagline;
        TextView overview;
        ImageView poster;
    }
    ViewHolder holder = new ViewHolder();

    private FilmProcessor mFilmProcessor = new FilmProcessor();
    private ImageLoader mImageLoader;
    private final Object mDelayedLock = new Object();

    String detailUrl;

    int finalHeight, finalWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        holder.poster = (ImageView) findViewById(R.id.poster);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final HttpDataSource dataSource = getHttpDataSource();
        final FilmProcessor processor = getProcessor();
        mImageLoader = ImageLoader.get(DetailsActivity.this);

        DescriptionOfTheFilm description = getIntent().getParcelableExtra(
                DescriptionOfTheFilm.class.getCanonicalName());
        this.detailUrl = description.getDetailsUrl();

        update(dataSource, processor);
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
        return detailUrl+Film.getAppendToResponse(Film.AppendToResponse.releases);
    }

    @Override
    public void onDataLoadStart() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data){
        holder.title = (TextView) findViewById(R.id.title);
        holder.genres = (TextView) findViewById(R.id.genres);
        holder.tagline = (TextView) findViewById(R.id.tagline);
        holder.overview = (TextView) findViewById(R.id.overview);

        final SpannableString text = new SpannableString(data.getTitle()+" "+data.getReleaseDate());
        text.setSpan(new RelativeSizeSpan(0.8f), text.length() - data.getReleaseDate().length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text.setSpan(new TypefaceSpan("serif"), text.length() - data.getReleaseDate().length(), text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.title.setText(text);

        setTitle(holder.title.getText());

        final SpannableString text_tag = new SpannableString("Tagline\n" + data.getTagline());
        text_tag.setSpan(new StyleSpan(Typeface.BOLD | Typeface.ITALIC), 0, text_tag.length() - data.getTagline().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_tag.setSpan(new TypefaceSpan("serif"), text_tag.length() - data.getTagline().length(), text_tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.tagline.setText(text_tag);

        if (!data.getOverview().equals("null")) holder.overview.setText(data.getOverview());
        try {
            holder.genres.setText(data.getGenres());
        } catch (JSONException e) {
            e.printStackTrace();//TODO do normal exepction post
        }

        final String urlPoster = data.getPosterPath(ApiTMDB.SizePoster.w342);
        holder.poster.setImageBitmap(null);
        holder.poster.setTag(urlPoster);

        mImageLoader.loadAndDisplay(urlPoster, holder.poster);

        //Bitmap bitmap = ((BitmapDrawable) holder.poster.getDrawable()).getBitmap();
        //colorize(bitmap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_internet:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, holder.title.getText());
                Toast.makeText(this, holder.title.getText(), Toast.LENGTH_LONG).show();
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
