package lorentzonsolutions.rhome.interfaces;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.shared.GooglePlaceInformation;

/**
 * Created by johanlorentzon on 2017-05-09.
 */

public interface WebApiUtil {
    List<GooglePlaceInformation> getNearbyLocationsList(double locationLatitude, double locationLongitude, int radius, String type);
    List<List<HashMap<String, String>>> getPolylineData(LatLng start, LatLng end);
    GooglePlaceInformation getLocationFromLatLng(LatLng location);
}
