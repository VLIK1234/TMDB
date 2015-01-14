package com.example.vlik1234.tmdb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASUS on 12.01.2015.
 */
public class DescriptionOfTheFilm implements Parcelable{



    private String detailsUrl;

    public DescriptionOfTheFilm(String id) {
        this.detailsUrl = id;
    }

    public DescriptionOfTheFilm(Parcel in) {
        this.detailsUrl = in.readString();
    }

    public static final Parcelable.Creator<DescriptionOfTheFilm> CREATOR
            = new Parcelable.Creator<DescriptionOfTheFilm>() {
        public DescriptionOfTheFilm createFromParcel(Parcel in) {
            return new DescriptionOfTheFilm(in);
        }

        public DescriptionOfTheFilm[] newArray(int size) {
            return new DescriptionOfTheFilm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(this.detailsUrl);
    }

    public String getDetailsUrl() {
        return detailsUrl;
    }
}
