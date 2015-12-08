package github.tmdb.app;

import android.annotation.TargetApi;
import android.app.SearchManager;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.squareup.picasso.Picasso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.api.DeveloperKey;
import github.tmdb.api.Language;
import github.tmdb.bo.DescriptionOfTheFilm;
import github.tmdb.bo.Film;
import github.tmdb.fragment.DetailFragment;
import github.tmdb.helper.DataManager;
import github.tmdb.helper.ErrorHelper;
import github.tmdb.image.ImageLoaderIstin;
import github.tmdb.processing.FilmProcessor;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;


public class DetailsActivity extends AbstractActivity implements DataManager.Callback<Film>, SearchView.OnQueryTextListener {

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    private ImageView backdrop;
    private ImageView posterExternal;
    private FilmProcessor filmProcessor = new FilmProcessor();

    private String detailUrl;
    private String videoKey;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mProgressBar = (ProgressBar) findViewById(android.R.id.progress);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
        }
        final HttpDataSource dataSource = getHttpDataSource();
        final FilmProcessor processor = getProcessor();

        DescriptionOfTheFilm description = getIntent().getParcelableExtra(
                DescriptionOfTheFilm.class.getCanonicalName());
        detailUrl = description.getDetailsUrl();

        backdrop = (ImageView) findViewById(R.id.backdrop);
        posterExternal = (ImageView) findViewById(R.id.poster);
        FragmentTransaction fragmentTransaction;
        Fragment fragment;

        if (savedInstanceState == null) {
            fragment = DetailFragment.newInstance(this.detailUrl);
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.frame_detail, fragment);
            fragmentTransaction.commit();
        }
        update(dataSource, processor);
    }

    public void getVideosKey(String videoKey) {
        this.videoKey = videoKey;
    }

    private FilmProcessor getProcessor() {
        return filmProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return new TMDBDataSource();
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
        mProgressBar.setVisibility(View.VISIBLE);
    }



    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data) {
        final String urlBackdrop = data.getBackdropPath(ApiTMDB.SizePoster.w1280);
        backdrop.post(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(urlBackdrop)) {
                    Picasso.with(getBaseContext()).load(urlBackdrop).into(backdrop);
//                    ImageLoader.getInstance().displayImage(urlBackdrop, backdrop);
                    mProgressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    public void setActionBarTitle(String title) {
        setTitle(title);
    }

    public void onClick(View view) {
        if (videoKey != null) {

            Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                    this, DeveloperKey.DEVELOPER_KEY, videoKey);

            if (intent != null) {
                if (canResolveIntent(intent)) {
                    startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                } else {
                    YouTubeInitializationResult.SERVICE_MISSING
                            .getErrorDialog(this, REQ_RESOLVE_SERVICE_MISSING).show();
                }
            }
        }
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint(getString(R.string.search_hint));
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_internet:
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getSupportActionBar().getTitle() + getString(R.string.parameter_search));
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
                return true;
            case R.id.language_setting:
                Intent intentSetting = new Intent(DetailsActivity.this, SettingsActivity.class);
                startActivity(intentSetting);
//                Language.getLanguageDialog(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSearch(String search) throws UnsupportedEncodingException {
        DescriptionOfTheFilm description = new DescriptionOfTheFilm(ApiTMDB.getSearchMovie(URLEncoder.encode(search, getString(R.string.utf_8))), search);
        Intent intent = new Intent(DetailsActivity.this, SearchActivity.class);
        intent.putExtra(DescriptionOfTheFilm.class.getCanonicalName(), description);
        startActivity(intent);
    }


    @Override
    public boolean onQueryTextSubmit(String s) {
        try {
            onSearch(s);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            ErrorHelper.showDialog(getString(R.string.unsup_encod_exсept) + e.getMessage(),
                    getSupportFragmentManager().beginTransaction());
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        return true;
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        mProgressBar.setVisibility(View.GONE);
        ErrorHelper.showDialog(getString(R.string.some_exception) + e.getMessage(),
                getSupportFragmentManager().beginTransaction());
    }
}
