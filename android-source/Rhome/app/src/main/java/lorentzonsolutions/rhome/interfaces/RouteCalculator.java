package lorentzonsolutions.rhome.interfaces;

import android.location.Location;

import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GooglePlace;

/**
 * Interface for RouteCalculators. Implement when building a routecalculator to calculate fastest route for a list of
 * places.
 */

public interface RouteCalculator {

    List<GooglePlace> calculateFastestRoute(List<GooglePlace> places, Location startLocation, Location endLocation);
}
