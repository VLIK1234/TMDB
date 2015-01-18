package org.tmdb.bo;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JSONObjectWrapper implements Parcelable {

    private JSONObject mJO;

    public JSONObjectWrapper(String jsonObject) {
        try {
            mJO = new JSONObject(jsonObject);
        } catch (JSONException e) {
            throw new IllegalArgumentException("invalid json string");
        }
    }

    protected JSONObjectWrapper(Parcel in) {
        readFromParcel(in);
    }

    public JSONObjectWrapper(JSONObject jsonObject) {
        mJO = jsonObject;
    }

    protected String getString(String key) {
        return mJO.optString(key);
    }

    protected List<String> getArray(String key, String... keyArray)throws JSONException{
        JSONArray array = mJO.getJSONArray(key);
        List<String> arrayReturn = new ArrayList<String>();
        for (int i =0;i<array.length();i++){
            JSONObject jsonObject = array.getJSONObject(i);
            for (int j =0;j<keyArray.length;j++) {
                arrayReturn.add(jsonObject.getString(keyArray[j]));
            }
        }
        return  arrayReturn;
    }

    protected Boolean getBoolean(String key) {
        return mJO.optBoolean(key);
    }

    protected Long getLong(String id) {
        return mJO.optLong(id);
    }

    protected Double getDouble(String key) {
        return mJO.optDouble(key);
    }

    protected void set(String key, String value) {
        try {
            mJO.put(key, value);
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String toString() {
        return mJO.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mJO.toString());
    }

    /**
     * Read from parcel.
     *
     * @param in
     *            the in
     */
    protected void readFromParcel(final Parcel in) {
        String string = in.readString();
        try {
            mJO = new JSONObject(string);
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid parcel");
        }
    }
}
