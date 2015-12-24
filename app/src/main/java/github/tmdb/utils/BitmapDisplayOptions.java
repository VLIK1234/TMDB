package github.tmdb.utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;

import github.tmdb.R;
import github.tmdb.fragment.ViewUtils;

public final class BitmapDisplayOptions {

    private BitmapDisplayOptions() {
    }

    public static final int IMG_CACHE_SIZE = 20 * 1024 * 1024;
    public static final DisplayImageOptions EPG_GRID_OPTIONS = new DisplayImageOptions.Builder()
            .cacheInMemory(true)
            .cacheOnDisc(true)
            .extraForDownloader(false)
            .handler(new Handler(Looper.getMainLooper())) // default
            .build();
    private static final BitmapDisplayer FADE_IN_BITMAP_DISPLAYER = new FadeInBitmapDisplayer(300) {

        @Override
        public void display(final Bitmap bitmap, final ImageAware imageAware, final LoadedFrom loadedFrom) {
            if (loadedFrom == LoadedFrom.NETWORK) {
                super.display(bitmap, imageAware, loadedFrom);
            } else {
                imageAware.setImageBitmap(bitmap);
            }
        }

    };
//    public static final DisplayImageOptions DEFAULT_BITMAP_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
//            .showImageForEmptyUri(R.drawable.no_image_vertical)
//            .showImageOnFail(R.drawable.no_image_vertical)
//            .resetViewBeforeLoading(true)
//            .delayBeforeLoading(100)
//            .cacheInMemory(true)
//            .extraForDownloader(true)
//            .cacheOnDisc(true)
//            .displayer(FADE_IN_BITMAP_DISPLAYER) // default
//            .build();
    public static final DisplayImageOptions IMAGE_OPTIONS_EMPTY_PH = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(android.R.color.transparent)
            .showImageOnFail(android.R.color.transparent)
            .resetViewBeforeLoading(true)
            .delayBeforeLoading(100)
            .cacheInMemory(true)
            .extraForDownloader(false)
            .cacheOnDisk(true)
            .displayer(FADE_IN_BITMAP_DISPLAYER)
            .build();
    private static final BitmapDisplayer FADE_IN_BLUR_BITMAP_DISPLAYER = new FadeInBitmapDisplayer(300) {

        @Override
        public void display(final Bitmap bitmap, final ImageAware imageAware, final LoadedFrom loadedFrom) {
            final Bitmap blurred = ViewUtils.fastblur(bitmap, 8);

            if (loadedFrom == LoadedFrom.NETWORK) {
                super.display(blurred, imageAware, loadedFrom);
            } else {
                imageAware.setImageBitmap(blurred);
            }
        }

    };
//    public static final DisplayImageOptions BLURRED_BITMAP_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
//            .showImageForEmptyUri(R.drawable.no_image_vertical)
//            .showImageOnFail(R.drawable.no_image_vertical)
//            .resetViewBeforeLoading(true)
//            .delayBeforeLoading(100)
//            .cacheInMemory(true)
//            .extraForDownloader(true)
//            .cacheOnDisc(true)
//            .displayer(FADE_IN_BLUR_BITMAP_DISPLAYER) // default
//            .build();
//
// public static final DisplayImageOptions ROUNDED_BITMAP_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
//            .showImageOnLoading(android.R.color.transparent)
//            .showImageForEmptyUri(R.drawable.ic_placeholder_profile)
//            .showImageOnFail(R.drawable.ic_placeholder_profile)
//            .preProcessor(new BitmapProcessor() {
//                @Override
//                public Bitmap process(Bitmap bitmap) {
//                    return CircleImageHelper.circleDimAround(bitmap,
//                            CircleImageHelper.createCircle(bitmap.getWidth(), bitmap.getHeight()));
//                }
//            })
//            .cacheInMemory(true)
//            .cacheOnDisc(true)
//            .bitmapConfig(Bitmap.Config.RGB_565).build();
//
    public static final DisplayImageOptions PORTRAIT_BITMAP_DISPLAY_OPTIONS = new DisplayImageOptions.Builder()
            .showImageForEmptyUri(R.drawable.no_avatar)
            .showImageOnFail(R.drawable.no_avatar)
            .resetViewBeforeLoading(true)
            .delayBeforeLoading(100)
            .cacheInMemory(true)
            .cacheOnDisk(true)
            .extraForDownloader(true)
            .displayer(FADE_IN_BITMAP_DISPLAYER) // default
            .build();
}