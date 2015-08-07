package github.tmdb.processing;

import org.json.JSONObject;

import java.io.InputStream;

/**
 @author IvanBakach
 @version on 09.02.2015
 */
public class WallPostProcessor implements Processor<Long, InputStream> {

    @Override
    public Long process(InputStream inputStream) throws Exception {
        String stream = new StringProcessor().process(inputStream);
        return new JSONObject(stream).getJSONObject("response").getLong("post_id");
    }
}
