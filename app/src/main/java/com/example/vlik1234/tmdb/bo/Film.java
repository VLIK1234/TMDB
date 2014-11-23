package com.example.vlik1234.tmdb.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

public class Film extends JSONObjectWrapper {

    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String RELEASE_DATE = "release_date";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String POSTER_PATH = "poster_path";


    //INTERNAL
    private static final String NAME = "NAME";

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

    public String getReleaseDate() {
        return getString(RELEASE_DATE);
    }

    public String getVoteAverage() {
        return getString(VOTE_AVERAGE);
    }

    public String getPosterPath() {
        return getString("https://image.tmdb.org/t/p/w92" + getString(POSTER_PATH));
    }

    public void initTitle() {
        set(NAME, getTitle() + " " + getReleaseDate());
    }

    public String getName() {
        return getString(NAME);
    }

    public Long getId() {
        return getLong(ID);
    }

}
