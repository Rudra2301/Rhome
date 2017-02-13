package lorentzonsolutions.rhome.googleWebApi;

import android.location.Location;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

        // TODO. Not parsing this now.
        int durationByBicycleToStartLocation = -1;
        int durationByWalkToStartLocation = -1;

        try {
            // These cannot be null
            latitude = placeObject.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
            longitude = placeObject.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
            googlePlaceID = placeObject.getString("place_id");

            // Calculates the distance to start location
            Location start = StorageUtil.INSTANCE.getSelectedStartLocation();
            if(start != null) {
                String distanceData = distanceDurationCalculator.calculateDistance(start.getLatitude(), start.getLongitude(), latitude, longitude);
                DistanceDuration distanceDuration = parseDistanceCalculationData(distanceData);
                distanceToStartLocation = distanceDuration.distance;
                durationByCarToStartLocation = distanceDuration.duration;
                System.out.println("Duration calculated: " + durationByCarToStartLocation);
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
                .minutesByCar(durationByCarToStartLocation)
                .minutesByBicycle(durationByBicycleToStartLocation)
                .minutesByWalk(durationByWalkToStartLocation)
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
}
