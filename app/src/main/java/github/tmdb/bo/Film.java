package github.tmdb.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import github.tmdb.api.ApiTMDB;
import github.tmdb.api.AppendToResponseForFilm;
import github.tmdb.api.Language;

public class Film extends JSONObjectWrapper {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String VOTE_COUNT = "vote_count";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String OVERVIEW = "overview";
    private static final String GENRES = "genres";
    private static final String TAGLINE = "tagline";
    private static final String TOTAL_PAGES = "total_pages";
    private static final String VIDEOS = "videos";
    private static final String RUNTIME = "runtime";
    private static final String RESULTS = "results";
    private static final String HTTPS_IMAGE_TMDB_ORG_T_P = "https://image.tmdb.org/t/p/";
    private static final String KEY = "KEY";

    public static final Parcelable.Creator<Film> CREATOR
            = new Parcelable.Creator<Film>() {
        public Film createFromParcel(Parcel in) {
            return new Film(in);
        }

        public Film[] newArray(int size) {
            return new Film[size];
        }
    };

    public Film(String jsonObject) {
        super(jsonObject);
    }

    public Film(JSONObject jsonObject) {
        super(jsonObject);
    }

    protected Film(Parcel in) {
        super(in);
    }

    public static String getAppendToResponse(AppendToResponseForFilm... appResp) {
        StringBuilder sb = new StringBuilder();
        sb.append("&append_to_response=");

        for (AppendToResponseForFilm anAppResp : appResp) {
            sb.append(String.format(anAppResp + ","));
        }
        return sb.toString();
    }

    public String getTitle() {
        return getString(TITLE);
    }

    public String getRuntime() {
        if (getString(RUNTIME).equals("null")||getString(RUNTIME).equals("0")) {
            return "";
        }
        return getString(RUNTIME) + " min";
    }

    public String getTagline() {
        if (getString(TAGLINE).equals("null")) {
            return "";
        }
        return getString(TAGLINE);
    }

    public String getOverview() {
        if (getString(OVERVIEW).equals("null")) {
            return "";
        }
        return getString(OVERVIEW);
    }

    public String getReleaseDate() {
        String date = getString(RELEASE_DATE);
        if (!date.equals("")) {
            Calendar calendar = Calendar.getInstance();
            java.sql.Date javaSqlDate = java.sql.Date.valueOf(date);
            calendar.setTime(javaSqlDate);
            date = String.valueOf(calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale(Language.getLanguage())) + " " + calendar.get(Calendar.YEAR));
        }
        return date;
    }

    public String getGenres() throws JSONException {
        List<String> array = getArray(GENRES, "name");
        StringBuilder genres = new StringBuilder();
        for (int i = 0; i < array.size(); i++) {
            genres.append(i != array.size() - 1 ? array.get(i) + " | " : array.get(i));
        }
        return genres.toString();
    }

    public List<String> getVideos() throws JSONException {
        return getInternalArray(VIDEOS, RESULTS, "key");
    }

    public String getVoteAverage() {
        return getString(VOTE_AVERAGE);
    }

    public String getVoteCount() {
        return getString(VOTE_COUNT);
    }

    public String getTotalPages() {
        return getString(TOTAL_PAGES);
    }

    public String getPosterPath(ApiTMDB.SizePoster size) {
        return HTTPS_IMAGE_TMDB_ORG_T_P + size + getString(POSTER_PATH);
    }

    public String getBackdropPath(ApiTMDB.SizePoster size) {
        return HTTPS_IMAGE_TMDB_ORG_T_P + size + getString(BACKDROP_PATH);
    }

    public void initTitle() {
        set(KEY, getTitle() + "\n" + getReleaseDate());
    }

    public Long getId() {
        return getLong(ID);
    }

}
