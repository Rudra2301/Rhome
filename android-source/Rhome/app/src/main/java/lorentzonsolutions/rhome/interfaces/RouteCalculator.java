package lorentzonsolutions.rhome.interfaces;

import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GooglePlace;

/**
 * Interface for RouteCalculators. Implement when building a routecalculator to calculate fastest route for a list of
 * places.
 */

public interface RouteCalculator {

    List<GooglePlace> calculateFastestRoute(List<GooglePlace> places);
    double calculateTotalRouteDistance(List<GooglePlace> route);
}
