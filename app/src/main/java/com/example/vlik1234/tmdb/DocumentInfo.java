package com.example.vlik1234.tmdb;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ASUS on 23.10.2014.
 */
public class DocumentInfo implements Parcelable{

    private String mName; // имя кота
    private String mWhiskers; // усы
    private String mPaws; // лапы
    private String mTail; // хвост

    public DocumentInfo(String name, String whiskers, String paws, String tail) {
        mName = name;
        mWhiskers = whiskers;
        mPaws = paws;
        mTail = tail;
    }

    public DocumentInfo(Parcel in) {
        String[] data = new String[4];
        in.readStringArray(data);
        mName = data[0];
        mWhiskers = data[1];
        mPaws = data[2];
        mTail = data[3];
    }

    public void setCatName(String name) {
        mName = name;
    }

    public String getCatName() {
        return mName;
    }

    public void setWhiskers(String whiskers) {
        mWhiskers = whiskers;
    }

    public String getWhiskers() {
        return mWhiskers;
    }

    public String getPaws() {
        return mPaws;
    }

    public String getTail() {
        return mTail;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] { mName, mWhiskers, mPaws, mTail });
    }

    public static final Parcelable.Creator<DocumentInfo> CREATOR = new Parcelable.Creator<DocumentInfo>() {

        @Override
        public DocumentInfo createFromParcel(Parcel source) {
            return new DocumentInfo(source);
        }

        @Override
        public DocumentInfo[] newArray(int size) {
            return new DocumentInfo[size];
        }
    };
}
