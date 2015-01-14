package com.example.vlik1234.tmdb.bo;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.vlik1234.tmdb.ApiTMDB;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

public class Film extends JSONObjectWrapper {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String OVERVIEW = "overview";
    private static final String GENRES = "genres";
    private static final String TAGLINE = "tagline";

    public enum AppendToResponse{
        alternative_titles,
        credits,
        images,
        keywords,
        releases,
        videos,
        translations,
        similar,
        reviews,
        lists
    }

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

    public static String getAppendToResponse(AppendToResponse... appResp){
        StringBuilder sb = new StringBuilder();
        sb.append("&append_to_response=");

        if (appResp.length>1) {
            for (int i = 0; i < appResp.length; i++) {
                sb.append((i <= appResp.length - 1)? sb.append(appResp[i]) : sb.append(String.format(appResp[i] + ",")));
            }
        }else sb.append(appResp[0]);

        return sb.toString();
    }
    public String getTitle() {
        return getString(TITLE);
    }
    public String getTagline() {
        return getString(TAGLINE);
    }
    public String getOverview() {
        return getString(OVERVIEW);
    }

    public String getReleaseDate() {
        String date = getString(RELEASE_DATE);
        Calendar calendar = Calendar.getInstance();
        java.sql.Date javaSqlDate = java.sql.Date.valueOf(date);
        calendar.setTime(javaSqlDate);
        return "("+String.valueOf(calendar.get(Calendar.YEAR))+")";
    }

    public String getGenres() throws JSONException {
        List<String> array = getArray(GENRES, "name");
        StringBuilder genres = new StringBuilder();
        for (int i = 0; i <array.size(); i++) {
            genres.append(i!=array.size()-1?array.get(i)+" | ":array.get(i));
        }
        return genres.toString();
    }

    public String getVoteAverage() {
        return getString(VOTE_AVERAGE);
    }

    public String getPosterPath(ApiTMDB.SizePoster size) {
        return "https://image.tmdb.org/t/p/"+ size + getString(POSTER_PATH);
    }
    public String getBackdropPath(ApiTMDB.SizePoster size) {
        return "https://image.tmdb.org/t/p/"+ size + getString(BACKDROP_PATH);
    }

    public void initTitle() {
        set(KEY, getTitle() + "\n" + getReleaseDate());
    }

    public Long getId() {
        return getLong(ID);
    }

}
