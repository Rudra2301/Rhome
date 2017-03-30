package lorentzonsolutions.rhome.googleWebApi;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by johanlorentzon on 2017-03-22.
 */

public class Directions {
    private final String TAG = Directions.class.toString();

    public String directionsUrlForRoute(LatLng start, LatLng end) {

        String start_url = "origin=" + start.latitude + "," + start.longitude;
        String end_url = "destination=" + end.latitude + "," + end.longitude;
        String sensor = "sensor=false";

        String params = start_url + "&" + end_url + "&" + sensor;
        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
    }

    public String downloadDirectionsData(String url) {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection connection = null;

        try {
            URL downloadUrl = new URL(url);

            connection = (HttpURLConnection) downloadUrl.openConnection();
            connection.connect();

            inputStream = connection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line=br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        }
        catch (Exception e) {
            Log.d(TAG, "Error downloading directions data.  \n" + e.getMessage());
            e.printStackTrace();
        }
        finally {
                try {
                inputStream.close();
                connection.disconnect();
            } catch (Exception e) {
                Log.d(TAG, "Error closing resources when downloading directions data.");
            }
        }

        return data;
    }
}
