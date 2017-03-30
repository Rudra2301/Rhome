package lorentzonsolutions.rhome.googleWebApi;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.shared.PlaceInformation;
import lorentzonsolutions.rhome.utils.StorageUtil;

/**
 * This class parses JSON data in string to a list of PlaceInformation object.
 */

public class JSONDataParser {
    private final String TAG = "JSON_DATA_PARSER";

    private DistanceDurationCalculator distanceDurationCalculator = new DistanceDurationCalculator();

    /**
     * Parses the data collected from a nearby search request against the Google Maps Web API.
     * Information about response JSON structure can be found at:
     * https://developers.google.com/places/web-service/search
     * @param nearbyRequestResponseData
     * @return
     */
    public List<PlaceInformation> parseNearbyDataToPlaceInformation(String nearbyRequestResponseData) {

        Log.i(TAG, "Attempting to parse nearby locations data.");
        JSONObject jsonObject;
        JSONArray jsonArray;

        try {
            jsonObject = new JSONObject(nearbyRequestResponseData);
            jsonArray = jsonObject.getJSONArray("results");

        } catch (JSONException e) {
            Log.e(TAG, "Parsing error");
            e.printStackTrace();
            return new ArrayList<>();
        }
        return parseNearbySearchResultJSONArray(jsonArray);
    }

    private List<PlaceInformation> parseNearbySearchResultJSONArray(JSONArray jsonArray) {
        List<PlaceInformation> listOfPlaces = new ArrayList<>();

        int length = jsonArray.length();
        for(int i = 0; i < length; i++) {
            try {
                PlaceInformation place = buildNearbyPlaceInformation((JSONObject) jsonArray.get(i));
                listOfPlaces.add(place);
                Log.i(TAG, "Place added");
            } catch (JSONException e) {
                Log.e(TAG, "Error adding place.");
                e.printStackTrace();
            }
        }

        return listOfPlaces;

    }

    private PlaceInformation buildNearbyPlaceInformation(JSONObject placeObject) {
        Log.i(TAG, "Building nearby place information.");

        String name = "N/A";
        double latitude = 0.0;
        double longitude = 0.0;
        boolean isOpen = false;
        String iconAddress = "";
        String[] types = new String[]{};
        String address = "N/A";
        String googlePlaceID = "";
        double placeRating = 0.0;

        int distanceToStartLocation = -1;
        int durationByCarToStartLocation = -1;

        int distanceToEndLocation = -1;
        int durationByCarToEndLocation = -1;

        // TODO. Not parsing this now.
        int durationByBicycleToStartLocation = -1;
        int durationByWalkToStartLocation = -1;
        int durationByBicycleToEndLocation = -1;
        int durationByWalkToEndLocation = -1;

        try {
            // These cannot be null
            latitude = placeObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            longitude = placeObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            googlePlaceID = placeObject.getString("place_id");

            // Calculates the distance to start location
            Location start = StorageUtil.INSTANCE.getSelectedStartLocation();
            Location end = StorageUtil.INSTANCE.getSelectedEndLocation();
            if(start != null) {
                String distanceData = distanceDurationCalculator.calculateDistance(start.getLatitude(), start.getLongitude(), latitude, longitude);
                DistanceDuration distanceDuration = parseDistanceCalculationData(distanceData);
                distanceToStartLocation = distanceDuration.distance;
                durationByCarToStartLocation = distanceDuration.duration;
            }
            // TODO. Calculate distance to end.
            if(end != null) {
                String distanceData = distanceDurationCalculator.calculateDistance(end.getLatitude(), end.getLongitude(), latitude, longitude);
                DistanceDuration distanceDuration = parseDistanceCalculationData(distanceData);
                distanceToEndLocation = distanceDuration.distance;
                durationByCarToEndLocation = distanceDuration.duration;
            }

            // These can be null
            if(!placeObject.isNull("name")) name = placeObject.getString("name");
            if(!placeObject.isNull("vicinity")) address = placeObject.getString("vicinity");
            if(!placeObject.isNull("opening_hours")) isOpen = (placeObject.getJSONObject("opening_hours")).getBoolean("open_now");
            if(!placeObject.isNull("rating")) placeRating = placeObject.getDouble("rating");
            if(!placeObject.isNull("icon")) iconAddress = placeObject.getString("icon");

             if(!placeObject.isNull("types")) {

                 JSONArray typesArray = placeObject.getJSONArray("types");
                 int arraysLength = typesArray.length();
                 String[] placeholderArray = new String[arraysLength];
                 for(int i = 0; i < arraysLength; i++) {
                     placeholderArray[i] = (String) typesArray.get(i);
                 }
                 types = placeholderArray;
             }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing place.");
            e.printStackTrace();
        }
        PlaceInformation result = new PlaceInformation.BuildPlace(name, latitude, longitude)
                .address(address)
                .iconAddress(iconAddress)
                .isOpen(isOpen)
                .types(types)
                .placeRating(placeRating)
                .googlePlaceID(googlePlaceID)
                .distanceToStartLocation(distanceToStartLocation)
                .minutesByCarToStartLocation(durationByCarToStartLocation)
                .minutesByBicycleToStartLocation(durationByBicycleToStartLocation)
                .minutesByWalkToStartLocation(durationByWalkToStartLocation)
                .distanceToEndLocation(distanceToEndLocation)
                .minutesByBicycleToEndLocation(durationByBicycleToEndLocation)
                .minutesByWalkToEndLocation(durationByWalkToEndLocation)
                .build();

        return result;
    }

    public PlaceInformation parseReverseGeocodeDataToPlaceInformation(String reverseGeocodeData) {

        PlaceInformation place = null;

        String name = "Location";
        double latitude = 0.0;
        double longitude = 0.0;
        boolean isOpen = false;
        String iconAddress = "";
        String[] types = new String[]{};
        String address = "Not available";
        String googlePlaceID = "";
        double placeRating = -1;

        Log.i(TAG, "Attempting to parse reverse geocode data.");
        JSONObject jsonObject;
        JSONArray jsonArray;

        try {
            jsonObject = new JSONObject(reverseGeocodeData);
            jsonArray = jsonObject.getJSONArray("results");

            Log.i(TAG, "Building reverse geocode place information.");
            if(jsonArray != null && jsonArray.length() != 0) {
                JSONObject firstLocationInfo = (JSONObject) jsonArray.get(0);
                if(!firstLocationInfo.isNull("formatted_address")) address = firstLocationInfo.getString("formatted_address");

                if(!firstLocationInfo.isNull("types")) {

                    JSONArray typesArray = firstLocationInfo.getJSONArray("types");
                    int arraysLength = typesArray.length();
                    String[] placeholderArray = new String[arraysLength];
                    for(int i = 0; i < arraysLength; i++) {
                        placeholderArray[i] = (String) typesArray.get(i);
                    }
                    types = placeholderArray;
                }
                if(!firstLocationInfo.isNull("place_id")) googlePlaceID = firstLocationInfo.getString("place_id");

                latitude = firstLocationInfo.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                longitude = firstLocationInfo.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            }

        } catch (JSONException e) {
            Log.e(TAG, "Parsing error");
            e.printStackTrace();
            return null;
        }

        PlaceInformation result = new PlaceInformation.BuildPlace(name, latitude, longitude)
                .address(address)
                .iconAddress(iconAddress)
                .isOpen(isOpen)
                .types(types)
                .placeRating(placeRating)
                .googlePlaceID(googlePlaceID)
                .build();


        return result;

    }

    public DistanceDuration parseDistanceCalculationData(String distanceData) {
        int distance = -1;
        int duration = -1;
        // Info at: https://developers.google.com/maps/documentation/distance-matrix/intro#DistanceMatrixRequests
        try {
            // Row object
            JSONObject object = new JSONObject(distanceData);
            JSONArray jsonArray = object.getJSONArray("rows");

            // Element object
            object = jsonArray.getJSONObject(0);
            jsonArray = object.getJSONArray("elements");
            object = jsonArray.getJSONObject(0);

            // Check the object array to find the closest.
            // TODO. Determine if duration or distance should be used. Now using duration (fastest way)
            int fastestTime = jsonArray.getJSONObject(0).getJSONObject("duration").getInt("value");
            JSONObject fastest = jsonArray.getJSONObject(0);
            for(int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject element = jsonArray.getJSONObject(i);
                    int tryTime = element.getJSONObject("duration").getInt("value");
                    if(tryTime < fastestTime) {
                        fastest = element;
                    }
                }
                catch (JSONException e) {
                    Log.d(TAG, "Error parsing duration data.");
                }
            } //End of loop. Checking for fastest route.

            duration = fastest.getJSONObject("duration").getInt("value");
            distance = fastest.getJSONObject("distance").getInt("value");

            Log.i(TAG, "Distance calculated: " + distance);
            Log.i(TAG, "Duration calculated: " + duration);


        } catch (JSONException e) {
            Log.e(TAG, "Error parsing distance/duration data.");
            e.printStackTrace();
        }
        return new DistanceDuration(distance, duration);
    }

    // TODO. Throw wrapped exception
    public List<List<HashMap<String,String>>> parseDirectionsData(String directionsData) throws JSONException {


        JSONObject jObject = new JSONObject(directionsData);

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String,String>>>() ;
        JSONArray jRoutes = null;
        JSONArray jLegs = null;
        JSONArray jSteps = null;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for(int i=0;i<jRoutes.length();i++){
                jLegs = ( (JSONObject)jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for(int j=0;j<jLegs.length();j++){
                    jSteps = ( (JSONObject)jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for(int k=0;k<jSteps.length();k++){
                        String polyline = "";
                        polyline = (String)((JSONObject)((JSONObject)jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for(int l=0;l<list.size();l++){
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString(((LatLng)list.get(l)).latitude) );
                            hm.put("lng", Double.toString(((LatLng)list.get(l)).longitude) );
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }catch (Exception e){
        }

        return routes;
    }

    /**
     * Method to decode polyline points
     * Courtesy : http://jeffreysambells.com/2010/05/27/decoding-polylines-from-google-maps-direction-api-with-java
     * */
    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
