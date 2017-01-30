package lorentzonsolutions.rhome.googleWebApi;

import android.util.Log;

import lorentzonsolutions.rhome.utils.Resources;

/**
 * Class for calculation distance between locations.
 */

public class DistanceDurationCalculator {
    // TODO. Possibility to change modes.
    private final String TAG = "DISTANCE_CALCULATOR";
    private final String API_KEY = Resources.getInstance().getAPI_KEY_WEB_API();
    private DistanceModes mode = DistanceModes.DRIVING;

    private URLDataReceiver urlDataReceiver = new URLDataReceiver();

    public String calculateDistance(double fromLatitude, double fromLongitude, double toLatidtude, double toLongitude) {

        // TODO. Add modes to request to get duration to place. Ex mode=bicycling

        String urlString = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" +
                fromLatitude + "," +
                fromLongitude + "&destinations=" +
                toLatidtude + "," +
                toLongitude +
                "&mode=" + mode +
                "&key=" + API_KEY;

        Log.d(TAG, "Retrieving API data.");
        return urlDataReceiver.readURL(urlString);

    }

    public void setDistanceMode(DistanceModes mode) {
        this.mode = mode;
    }


}
