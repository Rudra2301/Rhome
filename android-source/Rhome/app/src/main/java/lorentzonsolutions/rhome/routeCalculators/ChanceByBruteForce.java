package lorentzonsolutions.rhome.routeCalculators;

import android.location.Location;

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

        int maxNrOfDifferentRoutes = calculateFactorial(places.size());

        return null;
    }

    public int calculateFactorial(int n) {
        // TODO.
        return 0;

    }

}
