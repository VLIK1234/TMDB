package com.example.vlik1234.tmdb.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Film extends JSONObjectWrapper {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String OVERVIEW = "overview";

    //Poster size PSIZE_(approximate size height in ppi)
    public enum SizePoster{
        w92,
        w154,
        w185,
        w342,
        w500,
        w780,
        w1000,
        original
    }

    private static final String TITLEnDATE = "TITLE&DATE";

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

    public String getTitle() {
        return getString(TITLE);
    }
    public String getOverview() {
        return getString(OVERVIEW);
    }

    public String getReleaseDate() {
        String date = getString(RELEASE_DATE);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
        Date testDate = null;
        try {
            testDate = sdf.parse(date);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        sdf = new SimpleDateFormat("dd/mm/yyyy");
        String newFormat = sdf.format(testDate);

        return newFormat;
    }

    public String getVoteAverage() {
        return getString(VOTE_AVERAGE);
    }

    public String getPosterPath(SizePoster size) {
        return "https://image.tmdb.org/t/p/"+ size + getString(POSTER_PATH);
    }
    public String getBackdropPath(String size) {
        return "https://image.tmdb.org/t/p/"+ size + getString(BACKDROP_PATH);
    }

    public void initTitle() {
        set(TITLEnDATE, getTitle() + "\n" + getReleaseDate());
    }

    public String getTitlenDate() {
        return getString(TITLEnDATE);
    }

    public Long getId() {
        return getLong(ID);
    }

}
