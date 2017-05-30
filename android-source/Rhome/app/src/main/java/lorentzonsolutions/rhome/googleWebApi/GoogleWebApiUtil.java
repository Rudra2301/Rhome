package lorentzonsolutions.rhome.googleWebApi;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.interfaces.WebApiResponseJsonParser;
import lorentzonsolutions.rhome.interfaces.WebApiUtil;
import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.UrlDataReceiver;

/**
 * Implementation of WebApiUtil that retrieves information from Googles Web API.
 *
 * @author Johan Lorentzon
 *
 */

// Class not covered in test suite. Serves only as a middlehand between urlDataReviever and GoogleJsonParser.

public class GoogleWebApiUtil implements WebApiUtil {

    private static final String WEB_API_KEY = Resources.getInstance().getAPI_KEY_WEB_API();
    private UrlDataReceiver urlDataReceiver = new UrlDataReceiver();
    private WebApiResponseJsonParser googleJsonParser = new GoogleJsonParser();

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
    public List<GooglePlace> getNearbyLocationsList(double locationLatitude, double locationLongitude, int radius, String type) {
        String url = buildNearbySearchUrl(locationLatitude, locationLongitude, radius, type);
        String data = urlDataReceiver.readURL(url);
        return googleJsonParser.parseNearbySearchData(data);
    }

    /**
     * Uses Googles directions API to retrieve information about directions data between two LatLng points.
     * @param start
     * @param end
     * @return
     */
    @Override
    public List<List<HashMap<String, String>>> getPolylineData(LatLng start, LatLng end) {
        String directionsData = urlDataReceiver.readURL(buildDirectionsForRouteUrl(start, end));

        return googleJsonParser.parseDirectionsData(directionsData);
    }

    /**
     * Uses Google Geocode API to reverse Geodata for LatLng to location data.
     * @param location
     * @return
     */
    @Override
    public GooglePlace getLocationFromLatLng(LatLng location) {
        String locationData = urlDataReceiver.readURL(buildLocationByLatLngUrl(location.latitude, location.longitude));
        return googleJsonParser.parseReverseGeocodeDataToPlaceInformation(locationData);
    }


    // ---------------------- URL BUILDERS --------------------------- //

    // GOOGLE PLACE WEB API
    private static String buildNearbySearchUrl(double locationLatitude, double locationLongitude, int radius, String type) {

        return "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                locationLatitude + "," +
                locationLongitude +
                "&radius=" + radius +
                "&types=" + type +
                "&key=" + WEB_API_KEY;
    }

    // GOOGLE DIRECTIONS WEB API
    private static String buildDirectionsForRouteUrl(LatLng start, LatLng end) {

        String start_url = "origin=" + start.latitude + "," + start.longitude;
        String end_url = "destination=" + end.latitude + "," + end.longitude;
        String sensor = "sensor=false";

        String params = start_url + "&" + end_url + "&" + sensor;
        String output = "json";

        return "https://maps.googleapis.com/maps/api/directions/" + output + "?" + params;
    }

    // GOOGLE GEOCODE API
    private static String buildLocationByLatLngUrl(double latitude, double longitude) {

        return "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +
                latitude + "," +
                longitude +
                "&key=" + WEB_API_KEY;
    }

    // GOOGLE DISTANCE MATRIX API
    static String buildDistanceMatrixUrl(double fromLatitude, double fromLongitude, double toLatidtude, double toLongitude, GoogleDistanceModes mode) {

        return "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=" +
                fromLatitude + "," +
                fromLongitude + "&destinations=" +
                toLatidtude + "," +
                toLongitude +
                "&mode=" + mode +
                "&key=" + WEB_API_KEY;
    }
}
