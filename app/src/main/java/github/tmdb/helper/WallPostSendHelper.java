package github.tmdb.helper;

import android.content.Context;

import github.tmdb.api.Api;
import github.tmdb.processing.WallPostProcessor;
import github.tmdb.source.VkDataSource;

/**
 @author IvanBakach
 @version on 09.02.2015
 */
public class WallPostSendHelper implements DataManager.Callback<Long> {

    private Context context;

    public WallPostSendHelper(Context context) {
        this.context = context;
    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onDone(Long data) {

    }

    @Override
    public void onDataLoadStart() {

    }

    public void send(String message) {
        DataManager.loadData(this,
                Api.sendWallPost(message),
                new VkDataSource(context),
                new WallPostProcessor());
    }
}
