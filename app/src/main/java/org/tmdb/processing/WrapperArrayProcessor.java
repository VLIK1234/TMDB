package org.tmdb.processing;

import org.json.JSONArray;
import org.json.JSONObject;
import org.tmdb.bo.JSONObjectWrapper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public abstract class WrapperArrayProcessor<T extends JSONObjectWrapper> implements Processor<List<T>, InputStream> {

    @Override
    public List<T> process(InputStream inputStream) throws Exception {
        String string = new StringProcessor().process(inputStream);
        JSONArray array = new JSONArray(string);
        List<T> noteArray = new ArrayList<T>(array.length());
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            noteArray.add(createObject(jsonObject));
        }
        return noteArray;
    }

    protected abstract T createObject(JSONObject jsonObject);
}
