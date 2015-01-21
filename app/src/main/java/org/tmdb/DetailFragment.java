package org.tmdb;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.tmdb.bo.Film;
import org.tmdb.helper.DataManager;
import org.tmdb.image.ImageLoader;
import org.tmdb.processing.FilmProcessor;
import org.tmdb.source.HttpDataSource;
import org.tmdb.source.TMDBDataSource;
import org.tmdb.vlik1234.R;

import java.util.Locale;

/**
 * Created by ASUS on 21.01.2015.
 */
public class DetailFragment extends Fragment implements DataManager.Callback<Film>{

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

    private String detailUrl;
    public static final String EXTRA_LANG = "extra_lang";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);

        holder.poster = (ImageView) v.findViewById(R.id.poster);
        holder.title = (TextView) v.findViewById(R.id.title);
        holder.date = (TextView) v.findViewById(R.id.date);
        holder.genres = (TextView) v.findViewById(R.id.genres);
        holder.tagline = (TextView) v.findViewById(R.id.tagline);
        holder.overview = (TextView) v.findViewById(R.id.overview);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final HttpDataSource dataSource = getHttpDataSource();
        final FilmProcessor processor = getProcessor();
        mImageLoader = ImageLoader.get(getActivity().getApplicationContext());

        this.detailUrl = getLanguage();

        update(dataSource, processor);
    }

    public static Fragment newInstance(String language) {
        DetailFragment fragmentPart = new DetailFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_LANG, language);
        fragmentPart.setArguments(args);
        return fragmentPart;
    }

    private String getLanguage() {
        return getArguments().getString(EXTRA_LANG);
    }

    private FilmProcessor getProcessor() {
        return mFilmProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return  new TMDBDataSource();
    }

    private void update(HttpDataSource dataSource, FilmProcessor processor) {
        DataManager.loadData(this,
                getUrl(),
                dataSource,
                processor);
    }

    private String getUrl() {
        return detailUrl+"?language="+ Locale.getDefault().getLanguage()+Film.getAppendToResponse(AppendToResponseForFilm.releases);
    }

    @Override
    public void onDataLoadStart() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data){

        holder.title.setText(data.getTitle());
        holder.date.setText(data.getReleaseDate());

        ((DetailsActivity) getActivity()).setActionBarTitle(holder.title.getText().toString());

        final SpannableString text_tag = new SpannableString("Tagline\n" + data.getTagline());
        text_tag.setSpan(new StyleSpan(Typeface.BOLD | Typeface.ITALIC), 0, text_tag.length() - data.getTagline().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        text_tag.setSpan(new TypefaceSpan("serif"), text_tag.length() - data.getTagline().length(), text_tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (!data.getTagline().equals("")&&!data.getTagline().equals("null")) holder.tagline.setText(text_tag);

        if (!data.getOverview().equals("null")) holder.overview.setText(data.getOverview());
        try {
            holder.genres.setText(data.getGenres());
        } catch (JSONException e) {
            Toast.makeText(getActivity().getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }

        final String urlPoster = data.getPosterPath(ApiTMDB.SizePoster.w342);
        holder.poster.setImageBitmap(null);
        holder.poster.setTag(urlPoster);

        mImageLoader.loadAndDisplay(urlPoster, holder.poster);

        //Bitmap bitmap = ((BitmapDrawable) holder.poster.getDrawable()).getBitmap();
        //colorize(bitmap);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
    }

}
