package org.tmdb;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.tmdb.bo.DescriptionOfTheFilm;
import org.tmdb.bo.Film;
import org.tmdb.helper.DataManager;
import org.tmdb.image.ImageLoader;
import org.tmdb.processing.FilmProcessor;
import org.tmdb.source.HttpDataSource;
import org.tmdb.source.TMDBDataSource;
import org.tmdb.vlik1234.R;


public class DetailsActivity extends ActionBarActivity implements DataManager.Callback<Film>,SearchView.OnQueryTextListener{

    static class ViewHolder {
        TextView title;
        TextView date;
        TextView genres;
        TextView tagline;
        TextView overview;
        ImageView poster;
    }
    private ViewHolder holder = new ViewHolder();

    private FilmProcessor mFilmProcessor = new FilmProcessor();
    private ImageLoader mImageLoader;

    String detailUrl;

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
        return detailUrl+"?language=ru"+Film.getAppendToResponse(Film.AppendToResponse.releases);
    }

    @Override
    public void onDataLoadStart() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data){
        holder.title = (TextView) findViewById(R.id.title);
        holder.date = (TextView) findViewById(R.id.date);
        holder.genres = (TextView) findViewById(R.id.genres);
        holder.tagline = (TextView) findViewById(R.id.tagline);
        holder.overview = (TextView) findViewById(R.id.overview);

        holder.title.setText(data.getTitle());
        holder.date.setText(data.getReleaseDate());

        setTitle(holder.title.getText());

        final SpannableString text_tag = new SpannableString("Tagline\n" + data.getTagline());
        text_tag.setSpan(new StyleSpan(Typeface.BOLD | Typeface.ITALIC), 0, text_tag.length() - data.getTagline().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_tag.setSpan(new TypefaceSpan("serif"), text_tag.length() - data.getTagline().length(), text_tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (!data.getTagline().equals("")&&!data.getTagline().equals("null")) holder.tagline.setText(text_tag);

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
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search");
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_internet:
                String parameterSearch = " online html5";
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, holder.title.getText()+ parameterSearch);
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

    private void onSearch(String search){
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(search));
        Intent intent = new Intent(DetailsActivity.this, SearchActivity.class);
        intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
        startActivity(intent);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        onSearch(s);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
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
