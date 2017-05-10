package lorentzonsolutions.rhome.googleWebApi;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.interfaces.WebApiUtil;
import lorentzonsolutions.rhome.shared.GooglePlaceInformation;
import lorentzonsolutions.rhome.utils.Resources;

/**
 * Implementation of WebApiUtil that retrieves information from Googles Web API.
 */

public class GoogleWebApiUtil implements WebApiUtil {

    private static final String WEB_API_KEY = Resources.getInstance().getAPI_KEY_WEB_API();
    private URLDataReceiver urlDataReceiver = new URLDataReceiver();
    private JSONDataParser jsonDataParser = new JSONDataParser();

    /**
     * Takes the information about a locations and search places nearby with the given type. Search radius is determined by the radius parameter.
     * This parameter can be maximum 50000 m.
     * @param locationLatitude
     * @param locationLongitude
     * @param radius
     * @param type
     * @return
     */
    @Override
    public List<GooglePlaceInformation> getNearbyLocationsList(double locationLatitude, double locationLongitude, int radius, String type) {
        String url = buildNearbySearchUrl(locationLatitude, locationLongitude, radius, type);
        String data = urlDataReceiver.readURL(url);
        return jsonDataParser.parseNearbySearchData(data);
    }

    /**
     * Uses googles directions API to retrieve information about directions data between two LatLng points.
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<List<HashMap<String, String>>> getPolylineData(LatLng start, LatLng end) {
        String directionsData = urlDataReceiver.readURL(buildDirectionsForRouteUrl(start, end));
        try {
            return jsonDataParser.parseDirectionsData(directionsData);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


    // ---------------------- URL BUILDERS --------------------------- //

    // GOOGLE PLACE WEB API
    private String buildNearbySearchUrl(double locationLatitude, double locationLongitude, int radius, String type) {

        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                locationLatitude + "," +
                locationLongitude +
                "&radius=" + radius +
                "&types=" + type +
                "&key=" + WEB_API_KEY;
    }

    // GOOGLE DIRECTIONS WEB API
    private String buildDirectionsForRouteUrl(LatLng start, LatLng end) {

        String start_url = "origin=" + start.latitude + "," + start.longitude;
        String end_url = "destination=" + end.latitude + "," + end.longitude;
        String sensor = "sensor=false";

        String params = start_url + "&" + end_url + "&" + sensor;
        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
    }

}
