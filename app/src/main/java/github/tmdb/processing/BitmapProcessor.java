package github.tmdb.processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

import github.tmdb.source.HttpDataSource;

public class BitmapProcessor implements Processor<Bitmap, InputStream> {

    @Override
    public Bitmap process(InputStream inputStream) throws Exception {
        try {
            return BitmapFactory.decodeStream(inputStream);
        } finally {
            HttpDataSource.close(inputStream);
        }
    }

}
