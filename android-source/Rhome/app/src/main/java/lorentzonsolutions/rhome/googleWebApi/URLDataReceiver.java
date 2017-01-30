package lorentzonsolutions.rhome.googleWebApi;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by johanlorentzon on 2017-01-25.
 */

public class URLDataReceiver {

    // Log tag
    private String TAG = "URL_DATA_RECEIVER";

    private HttpURLConnection httpURLConnection;
    private String responseData;


    public String readURL(String urlString) {
        // TODO. USE THE NEXT PAGE TOKEN TO GET MORE THAN 20 RESULTS.

        try{
            System.out.println("Connecting using url:  \n" + urlString);
            URL url = new URL(urlString);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.connect();

            try(BufferedReader br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
                StringBuilder sb = new StringBuilder();

                // Reading lines from stream
                String readLine;
                while ((readLine = br.readLine()) != null) {
                    sb.append(readLine);
                }

                responseData = sb.toString();
                Log.d(TAG, "Data downloaded: \n" + responseData);
                br.close();
                httpURLConnection.disconnect();
            }
        }
        catch (MalformedURLException e) {
            Log.e(TAG, "Malformed URL: \n" + e.getMessage());
            e.printStackTrace();
        }
        catch (IOException ioe) {
            Log.e(TAG, "Could not builderOpen connection: \n" + ioe.getMessage());
            ioe.printStackTrace();
        }
        return responseData;
    }

}
