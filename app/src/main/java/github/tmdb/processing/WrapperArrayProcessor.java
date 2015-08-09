package github.tmdb.processing;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import github.tmdb.bo.JSONObjectWrapper;

public abstract class WrapperArrayProcessor<T extends JSONObjectWrapper> implements Processor<ArrayList<T>, InputStream> {

    @Override
    public ArrayList<T> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONArray(string);
        ArrayList<T> noteArray = new ArrayList<>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            noteArray.add(createObject(jsonObject));
        }
        return noteArray;
    }

    protected abstract T createObject(JSONObject jsonObject);
}
