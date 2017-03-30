package lorentzonsolutions.rhome.utils;

import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import lorentzonsolutions.rhome.shared.PlaceInformation;
import lorentzonsolutions.rhome.shared.RouteObject;

/**
 * Calculates the route for a list of places.
 */

public class RouteCalculator {
    private final String TAG = "ROUTE_CALCULATOR";
    private Location startLocation = StorageUtil.INSTANCE.getSelectedStartLocation();
    private PlaceInformation startPlace = new PlaceInformation.BuildPlace("Start location", startLocation.getLatitude(), startLocation.getLongitude()).build();

    private Location endLocation = StorageUtil.INSTANCE.getSelectedEndLocation();
    private PlaceInformation endPlace = new PlaceInformation.BuildPlace("End location", endLocation.getLatitude(), endLocation.getLongitude()).build();


    // TODO. Check - Ant Colony Optimization
    // Now using the nearest neighbour algorithm

    public List<PlaceInformation> calculateFastestTime(List<PlaceInformation> places, boolean startFromEnd) {
        List<PlaceInformation> route = new ArrayList<>(places);

        // Going backwards from end location
        if(startFromEnd) {
            Log.d(TAG, "Finding fastest route going from end position.");
            // Using anonymous inner comparator to sort list and the get the place nearest to our end location
            Collections.sort(route, new Comparator<PlaceInformation>() {
                @Override
                public int compare(PlaceInformation o1, PlaceInformation o2) {
                    return o1.distanceToEndLocation - o2.distanceToEndLocation;
                }
            });
        }
        // Going forward from start location
        else {
            Log.d(TAG, "Finding fastest route going from start position.");
            // Using anonymous inner comparator to sort list and the get the place nearest to our start location
            Collections.sort(route, new Comparator<PlaceInformation>() {
                @Override
                public int compare(PlaceInformation o1, PlaceInformation o2) {
                    return o1.distanceToStartLocation - o2.distanceToStartLocation;
                }
            });
        }

        List<PlaceInformation> fastestRoute = new ArrayList<>();

        //Adding first nearest neighbour
        fastestRoute.add(route.get(0));
        Log.d(TAG, "Nearest neighbour from end position is " + route.get(0).name + "at " + route.get(0).distanceToEndLocation + " from end.");
        route.remove(0);

        // We have a next place to visit as long as the route list contains places.
        while (route.size() > 0) {
            PlaceInformation from = fastestRoute.get(fastestRoute.size()-1);

            PlaceInformation nearestNeighbour = null;
            double distance = Double.MAX_VALUE;

            // Go through the remaining places in our original route to find the next nearest place.
            for(PlaceInformation place : route) {
                double neighbourDistance = distance(from.latitude, from.longitude, place.latitude, place.longitude);
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
        List<PlaceInformation> returnList = new ArrayList<>();

        // Check if the calculation is done with beginning from end or not
        if(startFromEnd) {
            Collections.reverse(fastestRoute);
        }

        returnList.add(startPlace);
        returnList.addAll(fastestRoute);
        returnList.add(endPlace);

        Log.d(TAG, "Route calculated: ");
        Log.d(TAG, RouteObject.printRoute(returnList));
        return returnList;


    }

    public double calculateTotalRouteDistance(List<PlaceInformation> places) {
        List<PlaceInformation> route = new ArrayList<>(places);
        PlaceInformation from = route.get(0);
        route.remove(0);
        double distance = 0;

        Iterator iterator = route.iterator();

        while(iterator.hasNext()) {
            PlaceInformation to = (PlaceInformation) iterator.next();
            distance += distance(from.latitude, from.longitude, to.latitude, to.longitude);
            from = to;
        }
        return distance;
    }

    public static double distance(double fromLat, double fromLng, double toLat, double toLng) {

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

}