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

    public String getPosterPath() {
        return "https://image.tmdb.org/t/p/w185" + getString(POSTER_PATH);
    }

    public void initTitle() {
        set(NAME, getTitle() + "\n" + getReleaseDate());
    }

    public String getName() {
        return getString(NAME);
    }

    public Long getId() {
        return getLong(ID);
    }

}
