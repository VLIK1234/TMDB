package github.tmdb.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import github.tmdb.R;
import github.tmdb.adapter.CastAdapter;
import github.tmdb.adapter.CrewAdapter;
import github.tmdb.api.ApiTMDB;
import github.tmdb.api.DeveloperKey;
import github.tmdb.api.Language;
import github.tmdb.app.DetailsActivity;
import github.tmdb.bo.Cast;
import github.tmdb.bo.Crew;
import github.tmdb.bo.Film;
import github.tmdb.helper.DataManager;
import github.tmdb.helper.ErrorHelper;
import github.tmdb.helper.WallPostSendHelper;
import github.tmdb.processing.FilmProcessor;
import github.tmdb.source.HttpDataSource;
import github.tmdb.source.TMDBDataSource;
import github.tmdb.utils.BitmapDisplayOptions;
import github.tmdb.utils.UIUtil;

/**
 * Created by ASUS on 21.01.2015.
 */
public class DetailFragment extends Fragment implements DataManager.Callback<Film>, View.OnClickListener {

    private static final String TAG = "DetailFragment";
    public static final int BACKGROUND_ROOT_ALPHA = 200;

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;

    private CastAdapter mCastAdapter;
    private CrewAdapter mCrewAdapter;

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
        TextView crewLabel;
        ImageView poster;
        ImageView backdrop;
        Button trailerButton;
        Button postButton;
        RecyclerView castList;
        RecyclerView crewList;
    }

    public static final String EXTRA_LANG = "extra_lang";
    private ViewHolder holder = new ViewHolder();
    private FilmProcessor filmProcessor = new FilmProcessor();
    private String detailUrl;
    private String language;

    private List<String> videosKey;
    private String postMessage;
    private String videoKey;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        holder.root = (LinearLayout) view.findViewById(R.id.ll_root);
        holder.poster = (ImageView) view.findViewById(R.id.poster);
        holder.backdrop = (ImageView) view.findViewById(R.id.backdrop);
        holder.title = (TextView) view.findViewById(R.id.title);
        holder.date = (TextView) view.findViewById(R.id.date);
        holder.genres = (TextView) view.findViewById(R.id.genres);
        holder.runtime = (TextView) view.findViewById(R.id.runtime);
        holder.rating = (TextView) view.findViewById(R.id.rating_pic);
        holder.ratingText = (TextView) view.findViewById(R.id.rating_pic_text);
        holder.tagline = (TextView) view.findViewById(R.id.tagline);
        holder.overview = (TextView) view.findViewById(R.id.overview);
        holder.castLabel = (TextView) view.findViewById(R.id.tv_cast_label);
        holder.crewLabel = (TextView) view.findViewById(R.id.tv_crew_label);
        holder.trailerButton = (Button) view.findViewById(R.id.trailer_button);
        holder.trailerButton.setOnClickListener(this);
        holder.postButton = (Button) view.findViewById(R.id.post_button);
        holder.castList = (RecyclerView) view.findViewById(R.id.rv_cast_list);
        holder.crewList = (RecyclerView) view.findViewById(R.id.rv_crew_list);
        final LinearLayoutManager castLayoutManager = new LinearLayoutManager(getContext());
        castLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        final LinearLayoutManager crewLayoutManager = new LinearLayoutManager(getContext());
        crewLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        holder.castList.setLayoutManager(castLayoutManager);
        holder.crewList.setLayoutManager(crewLayoutManager);
        ArrayList<Cast> casts = new ArrayList<>();
        mCastAdapter = new CastAdapter(casts);
        holder.castList.setAdapter(mCastAdapter);
        ArrayList<Crew> crews = new ArrayList<>();
        mCrewAdapter = new CrewAdapter(crews);
        return view;
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
        controlUrl.append(Film.getAppendToResponse(ApiTMDB.APPEND_TO_RESPONSE_RELEASES, ApiTMDB.APPEND_TO_RESPONSE_VIDEOS, ApiTMDB.APPEND_TO_RESPONSE_CREDITS));
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
            if (videosKey.size() >= 1) {
                videoKey = videosKey.get(0);
                holder.trailerButton.setVisibility(View.VISIBLE);
            } else {
                videoKey = "";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            ErrorHelper.showDialog(getString(R.string.json_exсept) + e.getMessage(),
                    activity.getSupportFragmentManager().beginTransaction());
        }
        final String urlBackdrop = data.getBackdropPath(ApiTMDB.POSTER_1000X1500_BACKDROP_1000X563);
        ImageLoader.getInstance().displayImage(urlBackdrop, holder.backdrop);
        holder.title.setText(data.getTitle());
        holder.date.setText(data.getReleaseDate());
        holder.rating.setText(data.getVoteAverage());
        String ratingTemplate = "%s %s %s %s";
        String rating = String.format(ratingTemplate, getContext().getString(R.string.rating),
                data.getVoteAverage(), getContext().getString(R.string.from), data.getVoteCount());
        holder.ratingText.setText(rating);
        holder.ratingText.setText(String.format("%s%s%s%s", getContext().getString(R.string.rating), data.getVoteAverage(), getContext().getString(R.string.from), data.getVoteCount()));

        activity.setTitle(holder.title.getText().toString());
        final SpannableString text_tag;
        if (data.getTagline() != null && !data.getTagline().equals("")) {
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
        } else {
            holder.runtime.setText(data.getRuntime());
        }

        final String urlPoster = data.getPosterPath(ApiTMDB.POSTER_342X513_BACKDROP_342X192);
        ImageLoader.getInstance().displayImage(urlPoster, holder.poster, BitmapDisplayOptions.IMAGE_OPTIONS_EMPTY_PH, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                if (loadedImage != null) {
                    Palette.generateAsync(loadedImage, new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(final Palette palette) {
                            if (palette != null) {
                                if (palette.getDarkMutedColor() != null && palette.getLightMutedColor() != null &&
                                        palette.getMutedColor() != null) {
                                    holder.root.setBackgroundColor(palette.getDarkMutedColor().getRgb());
                                    Drawable background = holder.root.getBackground();
                                    background.setAlpha(BACKGROUND_ROOT_ALPHA);
                                    UIUtil.setBackgroundCompact(holder.root, background);
                                    setPrimaryTextColor(palette.getLightMutedColor().getRgb());
                                    setSecondTextColor(palette.getMutedColor().getRgb());
                                } else {
                                    holder.root.setBackgroundColor(palette.getDarkVibrantColor().getRgb());
                                    Drawable background = holder.root.getBackground();
                                    background.setAlpha(BACKGROUND_ROOT_ALPHA);
                                    UIUtil.setBackgroundCompact(holder.root, background);
                                    setPrimaryTextColor(palette.getLightVibrantColor().getRgb());
                                    setSecondTextColor(palette.getVibrantColor().getRgb());
                                }
                            }
                        }
                    });
                }
            }
        });

        holder.postButton.setOnClickListener(this);
        try {
            mCastAdapter = new CastAdapter(data.getCasts());
            holder.castList.setAdapter(mCastAdapter);
            mCastAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            mCrewAdapter = new CrewAdapter(data.getCrews());
            holder.crewList.setAdapter(mCrewAdapter);
            mCrewAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.post_button:
                WallPostSendHelper wallPostSend = new WallPostSendHelper(getContext());
                wallPostSend.send(postMessage);
                break;
            case R.id.trailer_button:
                if (videoKey != null) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(
                            getActivity(), DeveloperKey.DEVELOPER_KEY, videoKey);
                    if (intent != null) {
                        if (canResolveIntent(intent)) {
                            startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
                        } else {
                            YouTubeInitializationResult.SERVICE_MISSING
                                    .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
                        }
                    }
                }
                break;
        }
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
//        holder.rating.setTextColor(rgbColor);
        holder.tagline.setTextColor(rgbColor);
        holder.ratingText.setTextColor(rgbColor);
        holder.castLabel.setTextColor(rgbColor);
        holder.crewLabel.setTextColor(rgbColor);
        mCastAdapter.setCharterLabelColor(rgbColor);
        mCastAdapter.notifyDataSetChanged();
        mCrewAdapter.setCharterLabelColor(rgbColor);
        mCrewAdapter.notifyDataSetChanged();
    }

    private void setSecondTextColor(int rgbColor) {
        holder.date.setTextColor(rgbColor);
        holder.genres.setTextColor(rgbColor);
    }

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }
}
