package github.tmdb.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.util.List;

import github.tmdb.R;
import github.tmdb.api.ApiTMDB;
import github.tmdb.api.AppendToResponseForFilm;
import github.tmdb.api.Language;
import github.tmdb.app.DetailsActivity;
import github.tmdb.bo.Film;
import github.tmdb.helper.DataManager;
import github.tmdb.helper.ErrorHelper;
import github.tmdb.helper.WallPostSendHelper;
import github.tmdb.image.ImageLoader;
import github.tmdb.processing.FilmProcessor;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;

/**
 * Created by ASUS on 21.01.2015.
 */
public class DetailFragment extends Fragment implements DataManager.Callback<Film>, View.OnClickListener {

    static class ViewHolder {
        TextView title;
        TextView date;
        TextView genres;
        TextView runtime;
        TextView rating;
        TextView ratingText;
        TextView tagline;
        TextView overview;
        ImageView poster;
        Button trailerButton;
        Button postButton;
    }

    public static final String EXTRA_LANG = "extra_lang";
    private ViewHolder holder = new ViewHolder();
    private FilmProcessor filmProcessor = new FilmProcessor();
    private ImageLoader imageLoader;
    private String detailUrl;
    private String language;

    private Activity activity;

    private List<String> videosKey;
    private String postMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        holder.poster = (ImageView) v.findViewById(R.id.poster);
        holder.title = (TextView) v.findViewById(R.id.title);
        holder.date = (TextView) v.findViewById(R.id.date);
        holder.genres = (TextView) v.findViewById(R.id.genres);
        holder.runtime = (TextView) v.findViewById(R.id.runtime);
        holder.rating = (TextView) v.findViewById(R.id.rating_pic);
        holder.ratingText = (TextView) v.findViewById(R.id.rating_pic_text);
        holder.tagline = (TextView) v.findViewById(R.id.tagline);
        holder.overview = (TextView) v.findViewById(R.id.overview);
        holder.trailerButton = (Button) v.findViewById(R.id.trailer_button);
        holder.postButton = (Button) v.findViewById(R.id.post_button);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if ((activity = getActivity()) != null) {
            imageLoader = ImageLoader.get(activity.getApplicationContext());
        }
        final HttpDataSource dataSource = getHttpDataSource();
        final FilmProcessor processor = getProcessor();

        this.detailUrl = getLanguage();
        language = Language.getLanguage();
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
        return filmProcessor;
    }

    private HttpDataSource getHttpDataSource() {
        return new TMDBDataSource();
    }

    private void update(HttpDataSource dataSource, FilmProcessor processor) {
        DataManager.loadData(this,
                getUrl(),
                dataSource,
                processor);
    }

    private String getUrl() {
        StringBuilder controlUrl = new StringBuilder(detailUrl);
        controlUrl.append(ApiTMDB.getLanguage(controlUrl.toString())).append(language);
        controlUrl.append(Film.getAppendToResponse(AppendToResponseForFilm.releases,AppendToResponseForFilm.videos));
        return controlUrl.toString();
    }

    @Override
    public void onDataLoadStart() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data) {
        try {
            videosKey = data.getVideos();
            if (videosKey.size()>=1) {
                ((DetailsActivity)getActivity()).getVideosKey(videosKey.get(0));
                holder.trailerButton.setVisibility(View.VISIBLE);
            }
            else{
                ((DetailsActivity)getActivity()).getVideosKey("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ErrorHelper.showDialog(getString(R.string.json_exсept) + e.getMessage(),
                    getActivity().getSupportFragmentManager().beginTransaction());
        }

        holder.title.setText(data.getTitle());
        holder.date.setText(data.getReleaseDate());
        holder.rating.setText(data.getVoteAverage());
        holder.ratingText.setText(activity.getString(R.string.rating) + data.getVoteAverage()+ activity.getString(R.string.from) + data.getVoteCount());

        if ((activity = getActivity()) != null) {
            ((DetailsActivity) activity).setActionBarTitle(holder.title.getText().toString());
        }
        final SpannableString text_tag;
        if (!data.getTagline().equals("") && data.getTagline() != null) {
            text_tag = new SpannableString(getString(R.string.tagline) + data.getTagline());
            text_tag.setSpan(new StyleSpan(Typeface.BOLD | Typeface.ITALIC), 0, text_tag.length() - data.getTagline().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text_tag.setSpan(new TypefaceSpan(getString(R.string.serif)), text_tag.length() - data.getTagline().length(), text_tag.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tagline.setText(text_tag);
        }

        if (data.getOverview() != null) {
            holder.overview.setText(data.getOverview());
        }

        try {
            holder.genres.setText(data.getGenres());
        } catch (JSONException e) {
            ErrorHelper.showDialog(activity.getString(R.string.json_exсept) + e.getMessage(),
                    getActivity().getSupportFragmentManager().beginTransaction());
        }

        if (!data.getRuntime().equals("")) {
            holder.runtime.setText(data.getRuntime() + activity.getString(R.string.min));
        }
        else{
            holder.runtime.setText(data.getRuntime());
        }

        final String urlPoster = data.getPosterPath(ApiTMDB.SizePoster.w342);
        holder.poster.setImageBitmap(null);
        holder.poster.setTag(urlPoster);

        imageLoader.loadAndDisplay(urlPoster, holder.poster);
        holder.postButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        WallPostSendHelper wallPostSend = new WallPostSendHelper(activity.getApplicationContext());
        wallPostSend.send(postMessage);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        ErrorHelper.showDialog(activity.getString(R.string.some_exception) + e.getMessage(),
                getActivity().getSupportFragmentManager().beginTransaction());
    }

}
