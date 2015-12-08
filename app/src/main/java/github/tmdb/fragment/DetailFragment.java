package github.tmdb.fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.graphics.PaletteItem;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import github.tmdb.R;
import github.tmdb.adapter.CastAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.api.AppendToResponseForFilm;
import github.tmdb.api.Language;
import github.tmdb.app.DetailsActivity;
import github.tmdb.bo.Cast;
import github.tmdb.bo.Film;
import github.tmdb.helper.DataManager;
import github.tmdb.helper.ErrorHelper;
import github.tmdb.helper.WallPostSendHelper;
import github.tmdb.processing.FilmProcessor;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;

/**
 * Created by ASUS on 21.01.2015.
 */
public class DetailFragment extends Fragment implements DataManager.Callback<Film>, View.OnClickListener {

    private static final String TAG = "DetailFragment";

    private RecyclerView mRvCrewsList;
    private CastAdapter mCastAdapter;

    static class ViewHolder {
        LinearLayout root;
        TextView title;
        TextView date;
        TextView genres;
        TextView runtime;
        TextView rating;
        TextView ratingText;
        TextView tagline;
        TextView overview;
        TextView castLabel;
        ImageView poster;
        Button trailerButton;
        Button postButton;
    }

    public static final String EXTRA_LANG = "extra_lang";
    private ViewHolder holder = new ViewHolder();
    private FilmProcessor filmProcessor = new FilmProcessor();
    private String detailUrl;
    private String language;

    private List<String> videosKey;
    private String postMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail, container, false);
        holder.root = (LinearLayout) v.findViewById(R.id.ll_root);
        holder.poster = (ImageView) v.findViewById(R.id.poster);
        holder.title = (TextView) v.findViewById(R.id.title);
        holder.date = (TextView) v.findViewById(R.id.date);
        holder.genres = (TextView) v.findViewById(R.id.genres);
        holder.runtime = (TextView) v.findViewById(R.id.runtime);
        holder.rating = (TextView) v.findViewById(R.id.rating_pic);
        holder.ratingText = (TextView) v.findViewById(R.id.rating_pic_text);
        holder.tagline = (TextView) v.findViewById(R.id.tagline);
        holder.overview = (TextView) v.findViewById(R.id.overview);
        holder.castLabel = (TextView) v.findViewById(R.id.tv_cast_label);
        holder.trailerButton = (Button) v.findViewById(R.id.trailer_button);
        holder.postButton = (Button) v.findViewById(R.id.post_button);
        mRvCrewsList = (RecyclerView) v.findViewById(R.id.rv_cast_list);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        mRvCrewsList.setLayoutManager(linearLayoutManager);
        ArrayList<Cast> casts = new ArrayList<>();
        mCastAdapter = new CastAdapter(getContext(), casts);
        mRvCrewsList.setAdapter(mCastAdapter);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        controlUrl.append(Film.getAppendToResponse(AppendToResponseForFilm.releases, AppendToResponseForFilm.videos, AppendToResponseForFilm.credits));
        return controlUrl.toString();
    }

    @Override
    public void onDataLoadStart() {

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    public void onDone(Film data) {
        final FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        try {
            videosKey = data.getVideos();
            if (videosKey.size()>=1) {
                ((DetailsActivity)activity).getVideosKey(videosKey.get(0));
                holder.trailerButton.setVisibility(View.VISIBLE);
            }
            else{
                ((DetailsActivity)activity).getVideosKey("");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ErrorHelper.showDialog(getString(R.string.json_exсept) + e.getMessage(),
                    activity.getSupportFragmentManager().beginTransaction());
        }

        holder.title.setText(data.getTitle());
        holder.date.setText(data.getReleaseDate());
        holder.rating.setText(data.getVoteAverage());
        String ratingTemplate = "%s %s %s %s";
        String rating = String.format(ratingTemplate, getContext().getString(R.string.rating),
                data.getVoteAverage(),getContext().getString(R.string.from), data.getVoteCount());
        holder.ratingText.setText(rating);
        holder.ratingText.setText(String.format("%s%s%s%s", getContext().getString(R.string.rating), data.getVoteAverage(), getContext().getString(R.string.from), data.getVoteCount()));

        ((DetailsActivity) activity).setActionBarTitle(holder.title.getText().toString());
        final SpannableString text_tag;
        if (data.getTagline() != null&&!data.getTagline().equals("")) {
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
            ErrorHelper.showDialog(getContext().getString(R.string.json_exсept) + e.getMessage(),
                    activity.getSupportFragmentManager().beginTransaction());
        }

        if (!data.getRuntime().equals("")) {
            holder.runtime.setText(String.format("%s%s", data.getRuntime(), getContext().getString(R.string.min)));
        }
        else{
            holder.runtime.setText(data.getRuntime());
        }

        final String urlPoster = data.getPosterPath(ApiTMDB.SizePoster.w342);
        Picasso.with(getContext())
                .load(urlPoster)
                .into(holder.poster, new Callback() {
                    @Override
                    public void onSuccess() {
                        Bitmap bitmap = ((BitmapDrawable) holder.poster.getDrawable()).getBitmap();

                        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(final Palette palette) {
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (palette != null) {
                                            for (PaletteItem item : palette.getPallete()) {
                                                Log.d(TAG, "run() returned: " + item);

                                            }
                                            palette.getPallete();
                                            if (palette.getDarkMutedColor() != null) {
                                                holder.root.setBackgroundColor(palette.getDarkMutedColor().getRgb());
                                                setPrimaryTextColor(palette.getLightMutedColor().getRgb());
                                                setSecondTextColor(palette.getMutedColor().getRgb());
                                            } else {
                                                holder.root.setBackgroundColor(palette.getDarkVibrantColor().getRgb());
                                                setPrimaryTextColor(palette.getLightVibrantColor().getRgb());
                                                setSecondTextColor(palette.getVibrantColor().getRgb());
                                            }
                                        }
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onError() {

                    }
                });

        holder.postButton.setOnClickListener(this);
        try {
            mCastAdapter = new CastAdapter(getContext(), data.getCasts());
            mRvCrewsList.setAdapter(mCastAdapter);
            mCastAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        WallPostSendHelper wallPostSend = new WallPostSendHelper(getContext());
        wallPostSend.send(postMessage);
    }

    @Override
    public void onError(Exception e) {
        e.printStackTrace();
        ErrorHelper.showDialog(getContext().getString(R.string.some_exception) + e.getMessage(),
                getActivity().getSupportFragmentManager().beginTransaction());
    }

    private void setPrimaryTextColor(int rgbColor) {
        holder.title.setTextColor(rgbColor);
        holder.overview.setTextColor(rgbColor);
        holder.runtime.setTextColor(rgbColor);
        holder.rating.setTextColor(rgbColor);
        holder.tagline.setTextColor(rgbColor);
        holder.ratingText.setTextColor(rgbColor);
        holder.castLabel.setTextColor(rgbColor);
    }

    private void setSecondTextColor(int rgbColor) {
        holder.date.setTextColor(rgbColor);
        holder.genres.setTextColor(rgbColor);
    }

}
