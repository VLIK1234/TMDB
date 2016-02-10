package github.tmdb.core.provider;

import java.util.List;

import by.istin.android.xcore.XCoreHelper;
import by.istin.android.xcore.provider.DBContentProvider;
import github.tmdb.Application;

public class ContentProvider extends DBContentProvider {

    @Override
    protected List<Class<? extends XCoreHelper.Module>> getModules() {
        return Application.APP_MODULES;
    }

}
