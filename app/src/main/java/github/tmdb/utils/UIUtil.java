package github.tmdb.utils;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import github.tmdb.CoreApplication;

/**
 * @author Ivan Bakach
 * @version on 09.08.2015
 */
public class UIUtil {
    public static int getOrientation() {
        return CoreApplication.getAppContext().getResources().getConfiguration().orientation;
    }

    public static void setBackgroundCompact(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }
    public static int adjustAlpha(int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }
}
