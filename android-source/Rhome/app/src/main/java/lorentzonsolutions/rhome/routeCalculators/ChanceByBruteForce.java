package lorentzonsolutions.rhome.routeCalculators;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.interfaces.RouteCalculator;

/**
 * Algorithm to calculate fastest route for visiting a list of places. Calculates up to a maximum of 1000 different routes by brute force.
 *
 * @author Johan Lorentzon
 * @since 22/5 - 2017
 *
 */

public class ChanceByBruteForce implements RouteCalculator {

    private final String TAG = ChanceByBruteForce.class.toString();


    @Override
    public List<GooglePlace> calculateFastestRoute(List<GooglePlace> places, Location startLocation, Location endLocation) {

        GooglePlace startPlace = new GooglePlace.BuildPlace("Start location", startLocation.getLatitude(), startLocation.getLongitude()).build();
        GooglePlace endPlace = new GooglePlace.BuildPlace("End location", endLocation.getLatitude(), endLocation.getLongitude()).build();

        // Total number of maximum unique routes to find. Is now set to the max of 7! (7*6*5*4*3*2*1) .
        int maxRoutes = calculateFactorial(places.size());
        maxRoutes = maxRoutes > 5040 ? 5040 : maxRoutes;

        // Maximum iterations to find a new unique route.
        int maxTriesForUniqueRoute = 500;

        List<List<GooglePlace>> uniqueRoutes = findUniques(places, startPlace, endPlace, maxRoutes, maxTriesForUniqueRoute);

        Log.d(TAG, "Number of unique routes found: " + uniqueRoutes.size());

        return findFastestRoute(uniqueRoutes);
    }

    /**
     * Sorts through a list of lists of routes to find the shortest.
     *
     * @param uniqueRoutes
     * @return a list of {@link GooglePlace} representing the fastest route found.
     */
    private List<GooglePlace> findFastestRoute(List<List<GooglePlace>> uniqueRoutes) {
        List<GooglePlace> fastest = new ArrayList<>();
        double shortestDistance = Double.MAX_VALUE;
        for(List<GooglePlace> route : uniqueRoutes) {
            double routeDistance = DistanceCalculatorUtil.calculateTotalRouteDistance(route);
            if(routeDistance<shortestDistance) {
                shortestDistance = routeDistance;
                fastest = route;
            }
        }
        Log.d(TAG, "Found fastest route: " + fastest);
        //System.out for test output
        System.out.println(TAG + ": Distance: " + shortestDistance);
        Log.d(TAG, "Distance: " + shortestDistance);

        return fastest;
    }

    /**
     * Takes a list of places and shuffles it to randomize the order in which the appear in the list. Adds the startplace to the beginning and end
     * to the end of the list. Then returns a new list.
     *
     * @param places
     * @param startPlace
     * @param endPlace
     * @return a shuffled list of places with a start and end fixed at beginning and end of the list.
     */
    private List<GooglePlace> calculateRandomRoute(List<GooglePlace> places, GooglePlace startPlace, GooglePlace endPlace) {
        List<GooglePlace> randomRoute = new ArrayList<>(places);
        Collections.shuffle(randomRoute);
        List<GooglePlace> returnList = new ArrayList<>();
        returnList.add(startPlace); returnList.addAll(randomRoute); returnList.add(endPlace);
        return returnList;
    }

    /**
     * Tries for the given value of maxTriesForUniqueRoute to find a new route that has not been discovered. Either it finds all unique possible routes (maxRoutes)
     * or hits the limit for number of tries. When either of the above occurs the method returns a list containing the found unique routes.
     *
     * @param places
     * @param startPlace
     * @param endPlace
     * @param maxRoutes
     * @param maxTriesForUniqueRoute
     * @return a list of lists with unique routes.
     */
    public List<List<GooglePlace>> findUniques(List<GooglePlace> places, GooglePlace startPlace, GooglePlace endPlace, int maxRoutes, int maxTriesForUniqueRoute) {

        List<List<GooglePlace>> uniqueRoutes = new ArrayList<>();

        while(uniqueRoutes.size() < maxRoutes) {
            int triesToFindUnique = 0;
            while(triesToFindUnique <= maxTriesForUniqueRoute){
                List<GooglePlace> newRoute = calculateRandomRoute(places, startPlace, endPlace);
                if(!uniqueRoutes.contains(newRoute)) {
                    uniqueRoutes.add(newRoute);
                    break;
                }
                else {
                    triesToFindUnique++;
                }
            }
            if(triesToFindUnique >= maxTriesForUniqueRoute) break;
        }
        return uniqueRoutes;
    }

    /**
     * Returns the factorial of n.
     *
     * @param n
     * @return the result of n!
     */
    public int calculateFactorial(int n) {
        int result;
        if(n==1) return 1;
        result = calculateFactorial(n-1)*n;
        return result;
    }

}
