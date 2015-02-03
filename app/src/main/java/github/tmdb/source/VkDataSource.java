package github.tmdb.source;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.io.InputStream;

import github.tmdb.CoreApplication;
import github.tmdb.api.Api;
import github.tmdb.auth.VkOAuthHelper;

public class VkDataSource extends CachedDataSource {

    public static final String KEY = "VkDataSource";

    private Context context;

    public VkDataSource(Context context) {
        super(context);
        this.context = context;
    }

    public static VkDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        String signUrl = VkOAuthHelper.sign(p);
        String versionValue = Uri.parse(signUrl).getQueryParameter(Api.VERSION_PARAM);
        if (TextUtils.isEmpty(versionValue)) {
            signUrl = signUrl + "&" + Api.VERSION_PARAM + "=" + Api.VERSION_VALUE;
        }
        return super.getResult(signUrl);
    }

}
