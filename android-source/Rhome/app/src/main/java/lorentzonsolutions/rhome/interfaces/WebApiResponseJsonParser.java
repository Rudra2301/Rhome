package lorentzonsolutions.rhome.interfaces;

import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GoogleDistanceDuration;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;

/**
 * An interface to implement for classes that should be able to parse data from a specific API's responses.
 *
 * @author Johan Lorentzon
 *
 */

public interface WebApiResponseJsonParser {

    List<GooglePlace> parseNearbySearchData(String nearbyRequestResponseData);
    GooglePlace parseReverseGeocodeDataToPlaceInformation(String reverseGeocodeData);
    GoogleDistanceDuration parseDistanceCalculationData(String distanceData);
    List<List<HashMap<String,String>>> parseDirectionsData(String directionsData);

}
