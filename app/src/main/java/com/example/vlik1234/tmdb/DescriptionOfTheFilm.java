package com.example.vlik1234.tmdb;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASUS on 12.01.2015.
 */
public class DescriptionOfTheFilm implements Parcelable{



    private long id;

    public DescriptionOfTheFilm(Long id) {
        this.id = id;
    }

    public DescriptionOfTheFilm(Parcel in) {
        this.id = in.readLong();
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
        out.writeLong(this.id);
    }

    public long getId() {
        return id;
    }
}
