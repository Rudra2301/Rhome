package lorentzonsolutions.rhome.googleWebApi;

import lorentzonsolutions.rhome.utils.Resources;

/**
 * Class for reverse geocoding against the Google Web API.
 */

public class LocationByLatLngSearcher {
    private final String TAG = "LOCATION_BY_LAT_LNG_SEARCHER";
    private final String API_KEY = Resources.getInstance().getAPI_KEY_WEB_API();

    private URLDataReceiver urlDataReceiver = new URLDataReceiver();

    public String retrievePlaceInformation(double latitude, double longitude) {

        String urlString = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                latitude + "," +
                longitude +
                "&key=" + API_KEY;

        return urlDataReceiver.readURL(urlString);

    }
}
