package com.example.vlik1234.tmdb;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;

import com.example.vlik1234.tmdb.bo.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by ASUS on 04.11.2014.
 */
public class UpdateTask extends AsyncTask<String, Void, JSONObject> implements Parcelable {
    Context context;
    JSONArray jsonArray;

    public UpdateTask(Context context){
        super();
        this.context = context;
    }

    public UpdateTask(Parcel in) throws JSONException{
        //JSONParser jsonParser = new JSONParser(new JSONArray(jsonArray));
        //in.readStringArray(JSONParser);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected JSONObject doInBackground(String... urls) {
        return null;
    }

    public JSONObject loadJSON(String url){
        JSONParser jsonParser = new JSONParser();
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("my_param","param_value"));
        JSONObject json = jsonParser.makeHttpRequest(url,"GET",params);

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonData) {
        super.onPostExecute(jsonData);
        Log.d("myLogs","Start get");
        if (jsonData != null){
            super.onPostExecute(jsonData);
            String res = "";
            try {
                res = jsonData.getString("original_title");
                //Что-то происходит
                //jsonArray = new JSONArray(loadJSON("http://api.themoviedb.org/3/movie/550?api_key=f413bc4bacac8dff174a909f8ef535ae"));
            }catch (JSONException e){
                e.printStackTrace();
            }
        }else{
            //Что-то происходит
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(String.valueOf(jsonArray));
    }
}
