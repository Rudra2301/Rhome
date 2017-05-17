package lorentzonsolutions.rhome.routeCalculators;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import lorentzonsolutions.rhome.interfaces.RouteCalculator;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.utils.StorageUtil;

/**
 * This class creates an object to help with calculating a route from a to b with stop on N places in between.
 */

public class NearestNeighbourRouteCalculator implements RouteCalculator {

    private final String TAG = NearestNeighbourRouteCalculator.class.toString();

    private Location startLocation = StorageUtil.INSTANCE.getSelectedStartLocation();
    private GooglePlace startPlace = new GooglePlace.BuildPlace("Start location", startLocation.getLatitude(), startLocation.getLongitude()).build();

    private Location endLocation = StorageUtil.INSTANCE.getSelectedEndLocation();
    private GooglePlace endPlace = new GooglePlace.BuildPlace("End location", endLocation.getLatitude(), endLocation.getLongitude()).build();

    /**
     * Takes a list of place objects and calculates a fastest route using a nearest neighbour algoritm. Returns a list with
     * the route in order starting at first location to visit at index 0.
     * @param places
     * @return
     */
    @Override
    public List<GooglePlace> calculateFastestRoute(List<GooglePlace> places) {
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
                double neighbourDistance = distanceBetween(from.latitude, from.longitude, place.latitude, place.longitude);
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

    @Override
    public double calculateTotalRouteDistance(List<GooglePlace> places) {
        List<GooglePlace> route = new ArrayList<>(places);
        GooglePlace from = route.get(0);
        route.remove(0);
        double distance = 0;

        Iterator iterator = route.iterator();

        while(iterator.hasNext()) {
            GooglePlace to = (GooglePlace) iterator.next();
            distance += distanceBetween(from.latitude, from.longitude, to.latitude, to.longitude);
            from = to;
        }
        return distance;
    }

    public static double distanceBetween(double fromLat, double fromLng, double toLat, double toLng) {

        double theta = fromLng - toLng;
        double dist = Math.sin(deg2rad(fromLat)) * Math.sin(deg2rad(toLat)) + Math.cos(deg2rad(fromLat)) * Math.cos(deg2rad(toLat)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
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