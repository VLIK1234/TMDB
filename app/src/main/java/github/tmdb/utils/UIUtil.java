package github.tmdb.utils;

import github.tmdb.CoreApplication;

/**
 * @author Ivan Bakach
 * @version on 09.08.2015
 */
public class UIUtil {
    public static int getOrientation() {
        return CoreApplication.getAppContext().getResources().getConfiguration().orientation;
    }
}
