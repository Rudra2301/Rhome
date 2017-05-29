package lorentzonsolutions.rhome.interfaces;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GooglePlace;

/**
 * Interface for interaction with a locations API.
 *
 * @author Johan Lorentzon
 *
 */

public interface WebApiUtil {
    List<GooglePlace> getNearbyLocationsList(double locationLatitude, double locationLongitude, int radius, String type);
    List<List<HashMap<String, String>>> getPolylineData(LatLng start, LatLng end);
    GooglePlace getLocationFromLatLng(LatLng location);
}
