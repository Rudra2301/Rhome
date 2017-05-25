package lorentzonsolutions.rhome.routeCalculators;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GooglePlace;

/**
 * Helper class for distance calculation.
 *
 * @author Johan Lorentzon
 * @since 22/5 - 2017
 */

public class DistanceCalculatorUtil {

    /**
     * Calculates the total distance to travel for a specified route.
     *
     * @param places
     * @return the total distance.
     */
    public static double calculateTotalRouteDistance(List<GooglePlace> places) {
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

}
