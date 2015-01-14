package com.example.vlik1234.tmdb.processing;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.vlik1234.tmdb.source.HttpDataSource;

import java.io.InputStream;

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
