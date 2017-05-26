package lorentzonsolutions.rhome.routeCalculators;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import lorentzonsolutions.rhome.interfaces.RouteCalculator;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;

/**
 * This class creates an object to help with calculating a route from a to b with stop on N places in between.
 *
 * @author Johan Lorentzon
 */

public class NearestNeighbourRouteCalculator implements RouteCalculator {

    private final String TAG = NearestNeighbourRouteCalculator.class.toString();


    /**
     * Takes a list of place objects along with a start and end place and calculates a fastest route using a nearest neighbour algoritmh. Returns a list with
     * the route in order starting at first location to visit at index 0.
     *
     * @param places
     * @param startLocation
     * @param endLocation
     * @return a list of places in the sorted order for the fastest possible route starting from index 0.
     */
    @Override
    public List<GooglePlace> calculateFastestRoute(List<GooglePlace> places, Location startLocation, Location endLocation) {
        GooglePlace startPlace = new GooglePlace.BuildPlace("Start location", startLocation.getLatitude(), startLocation.getLongitude()).build();
        GooglePlace endPlace = new GooglePlace.BuildPlace("End location", endLocation.getLatitude(), endLocation.getLongitude()).build();

        List<GooglePlace> route = new ArrayList<>(places);

        Log.d(TAG, "Finding fastest route going from end position.");
        // Using anonymous inner comparator to sort list and the get the place nearest to our end location
        Collections.sort(route, new Comparator<GooglePlace>() {
            @Override
            public int compare(GooglePlace o1, GooglePlace o2) {
                return o1.distanceToEndLocation - o2.distanceToEndLocation;
            }
        });

        List<GooglePlace> fastestRoute = new ArrayList<>();

        //Adding first nearest neighbour
        fastestRoute.add(route.get(0));
        Log.d(TAG, "Nearest neighbour from end position is " + route.get(0).name + "at " + route.get(0).distanceToEndLocation + " from end.");
        route.remove(0);

        // We have a next place to visit as long as the route list contains places.
        while (route.size() > 0) {
            GooglePlace from = fastestRoute.get(fastestRoute.size()-1);

            GooglePlace nearestNeighbour = null;
            double distance = Double.MAX_VALUE;

            // Go through the remaining places in our original route to find the next nearest place.
            for(GooglePlace place : route) {
                double neighbourDistance = DistanceCalculatorUtil.distanceBetween(from.latitude, from.longitude, place.latitude, place.longitude);
                if(neighbourDistance < distance) {
                    distance = neighbourDistance;
                    nearestNeighbour = place;
                }
            }
            Log.d(TAG, "Found nearest neighbour (" + nearestNeighbour.name + ") at " + distance + " from previous place.");

            // We now have the next nearest neighbour. Add to the new route.
            fastestRoute.add(nearestNeighbour);
            // Remove the added place from the route, we have visited it.
            route.remove(nearestNeighbour);
        }

        // fastestRoute now contains all the places to visit in the order of nearest neighbour with the beginning at start position.
        List<GooglePlace> returnList = new ArrayList<>();

        Collections.reverse(fastestRoute);


        returnList.add(startPlace);
        returnList.addAll(fastestRoute);
        returnList.add(endPlace);

        Log.d(TAG, "Route calculated: ");
        Log.d(TAG, printRoute(returnList));
        return returnList;
    }

    public static String printRoute(List<GooglePlace> route) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < route.size(); i++) {
            GooglePlace place = route.get(i);
            sb.append((i+1) + ". Name: " + place.name
                    + "\nDistance to end: " + place.distanceToEndLocation
                    + "\nDistance to start:" + place.distanceToStartLocation
                    + "\nAddress: " + place.address
                    + "\n----------------------------------------------------\n");
        }

        return sb.toString();
    }

}