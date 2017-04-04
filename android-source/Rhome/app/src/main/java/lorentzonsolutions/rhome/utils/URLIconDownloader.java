package lorentzonsolutions.rhome.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * This class can fetch icons from a URL to display as an drawable.
 */

public class URLIconDownloader {
    private static String TAG = "IMAGE_DOWNLOADER";

    public static Drawable loadImageFromUrl(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, null);

        }
        // TODO. Return error and catch in code.
        catch (MalformedURLException e) {
            Log.e(TAG, "Bad URL: \n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        catch (IOException e) {
            Log.e(TAG, "Could not open connection: \n" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
