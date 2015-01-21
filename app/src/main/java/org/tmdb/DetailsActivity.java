package org.tmdb;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tmdb.bo.DescriptionOfTheFilm;
import org.tmdb.bo.Film;
import org.tmdb.helper.DataManager;
import org.tmdb.image.ImageLoader;
import org.tmdb.processing.FilmProcessor;
import org.tmdb.source.HttpDataSource;
import org.tmdb.source.TMDBDataSource;
import org.tmdb.vlik1234.R;


public class DetailsActivity extends ActionBarActivity implements DataManager.Callback<Film>,SearchView.OnQueryTextListener{

    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;
    private ImageView imageView;

    private FilmProcessor mFilmProcessor = new FilmProcessor();
    private ImageLoader mImageLoader;

    private String detailUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mImageLoader = ImageLoader.get(DetailsActivity.this);
        final HttpDataSource dataSource = getHttpDataSource();
        final FilmProcessor processor = getProcessor();

        DescriptionOfTheFilm description = getIntent().getParcelableExtra(
                DescriptionOfTheFilm.class.getCanonicalName());
        this.detailUrl = description.getDetailsUrl();

        update(dataSource, processor);

        imageView = (ImageView) findViewById(R.id.backdrop);

        scaleContents(findViewById(R.id.frame_detail));
        if (savedInstanceState == null) {
            this.fragment = DetailFragment.newInstance(this.detailUrl);

            this.fragmentTransaction = getFragmentManager().beginTransaction();
            this.fragmentTransaction.add(R.id.frame_detail, fragment);
            this.fragmentTransaction.commit();
        }
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
        return detailUrl;
    }

    @Override
    public void onDataLoadStart() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data){
        final String urlPoster = data.getBackdropPath(ApiTMDB.SizePoster.original);
        imageView.setImageBitmap(null);
        imageView.setTag(urlPoster);

        mImageLoader.loadAndDisplay(urlPoster, imageView);
    }
    private void scaleContents(View rootView){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        float correlation = width/height;

        scaleViewAndChildren(rootView, correlation);
    }

    public static void scaleViewAndChildren(View root, float correlation){
        ViewGroup.LayoutParams layoutParams;
        layoutParams = root.getLayoutParams();

        if (layoutParams.width != ViewGroup.LayoutParams.FILL_PARENT &&
                layoutParams.width != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            layoutParams.width *= correlation;
        }
        if (layoutParams.height != ViewGroup.LayoutParams.FILL_PARENT &&
                layoutParams.height != ViewGroup.LayoutParams.WRAP_CONTENT)
        {
            layoutParams.height *= correlation;
        }


        if (layoutParams instanceof ViewGroup.MarginLayoutParams)
        {
            ViewGroup.MarginLayoutParams marginParams =
                    (ViewGroup.MarginLayoutParams)layoutParams;
            marginParams.leftMargin *= correlation;
            marginParams.rightMargin *= correlation;
            if (correlation<1) {
                marginParams.topMargin *= correlation+1.3;
                marginParams.bottomMargin *= correlation;
            }
        }

        root.setPadding(
                (int)(root.getPaddingLeft() * correlation),
                (int)(root.getPaddingTop() * correlation),
                (int)(root.getPaddingRight() * correlation),
                (int)(root.getPaddingBottom() * correlation));

        if (root instanceof TextView)
        {
            TextView textView = (TextView)root;
            textView.setTextSize(textView.getTextSize() * correlation);
        }

        if (root instanceof ViewGroup)
        {
            ViewGroup groupView = (ViewGroup)root;
            for (int cnt = 0; cnt < groupView.getChildCount(); ++cnt)
                scaleViewAndChildren(groupView.getChildAt(cnt), correlation);
        }
    }

    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
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
                String parameterSearch = " online";
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle()+ parameterSearch);
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
        //Toast.makeText(DetailsActivity.this,ApiTMDB.getSearchMovie(search),Toast.LENGTH_LONG).show();
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(search), search);
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
}
