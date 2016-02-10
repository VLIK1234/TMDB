package github.tmdb.source;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import github.tmdb.CoreApplication;

public class CachedDataSource extends HttpDataSource {

    public static final String KEY = "CachedDataSource";

    private Context context;

    public CachedDataSource(Context context) {
        this.context = context;
    }

    public static CachedDataSource get(Context context) {
        return CoreApplication.get(context, KEY);
    }

    @Override
    public InputStream getResult(String p) throws Exception {
        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, "__cache");
        file.mkdirs();
        String path = file.getPath() + File.separator + generateFileName(p);
        File cacheFile = new File(path);
        if (cacheFile.exists()) {
            return new FileInputStream(cacheFile);
        }
        InputStream inputStream = super.getResult(p);
        try {
            copy(inputStream, cacheFile);
        } catch (Exception e) {
            cacheFile.delete();
            throw e;
        }
        return new FileInputStream(cacheFile);
    }

    private static final int DEFAULT_BUFFER_SIZE = 1024 * 8;

    public static void copy(InputStream input, File file) throws IOException {
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(file);
            copy(input, fileOutputStream);
        } finally {
            close(fileOutputStream);
        }
    }

    public static long copy(InputStream input, OutputStream output) throws IOException {
        return copy(input, output, new byte[DEFAULT_BUFFER_SIZE]);
    }

    public static long copy(InputStream input, OutputStream output, byte[] buffer) throws IOException {
        long count = 0;
        int n;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }

    private String generateFileName(String p) {
        return md5(p);
    }

    public static String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
