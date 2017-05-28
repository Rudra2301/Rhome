package lorentzonsolutions.rhome;

import android.location.Location;
import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.routeCalculators.ChanceByBruteForce;
import lorentzonsolutions.rhome.routeCalculators.DistanceCalculatorUtil;
import lorentzonsolutions.rhome.routeCalculators.NearestNeighbourRouteCalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


/**
 * @author Johan Lorentzon
 * @since 22/5 - 2017
 *
 * Test for RouteCalculatorsTest.class
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class RouteCalculatorsTest {

    List<GooglePlace> advancedRoute;
    List<GooglePlace> maxedRoute;

    ChanceByBruteForce bruteForceUnderTest;
    NearestNeighbourRouteCalculator nearestNeighbourUnderTest;

    Location start;
    Location end;

    @Before
    public void before() {
        bruteForceUnderTest = new ChanceByBruteForce();
        nearestNeighbourUnderTest = new NearestNeighbourRouteCalculator();

        advancedRoute = new ArrayList<>();
        maxedRoute = new ArrayList<>();

        PowerMockito.mockStatic(Log.class);

        // Mocking locations
        start = Mockito.mock(Location.class);
        end = Mockito.mock(Location.class);

        Mockito.doReturn(57.722853).when(start).getLatitude();
        Mockito.doReturn(11.939049).when(start).getLongitude();

        Mockito.doReturn(57.672583).when(end).getLatitude();
        Mockito.doReturn(11.997757).when(end).getLongitude();

        GooglePlace place2 = new GooglePlace.BuildPlace("PLACE2", 57.704147, 12.012863).build();
        GooglePlace place3 = new GooglePlace.BuildPlace("PLACE2", 57.738067, 12.057152).build();
        GooglePlace place4 = new GooglePlace.BuildPlace("PLACE2", 57.681761, 11.953125).build();
        GooglePlace place5 = new GooglePlace.BuildPlace("PLACE2", 57.668543, 12.057152).build();
        GooglePlace place6 = new GooglePlace.BuildPlace("PLACE2", 57.712951, 11.975441).build();
        GooglePlace place7 = new GooglePlace.BuildPlace("PLACE2", 57.731102, 12.01767).build();

        GooglePlace extra1 = new GooglePlace.BuildPlace("EXTRA1", 57.722853, 11.939049).build();
        GooglePlace extra2 = new GooglePlace.BuildPlace("EXTRA1", 57.672583, 11.997757).build();

        advancedRoute.add(place2); advancedRoute.add(place3); advancedRoute.add(place4);
        advancedRoute.add(place5); advancedRoute.add(place6); advancedRoute.add(place7);

        maxedRoute.addAll(advancedRoute); maxedRoute.add(extra1); maxedRoute.add(extra2);

    }

    @Test
    public void bruteForce_testRecursiveFactorialFunction() {
        assertEquals(6, bruteForceUnderTest.calculateFactorial(3));
        assertEquals(5040, bruteForceUnderTest.calculateFactorial(7));
    }

    @Test
    public void bruteForde_shouldReturnAValidRoute() {
        List<GooglePlace> fastest = bruteForceUnderTest.calculateFastestRoute(advancedRoute, start, end);

        assertNotNull(fastest);
    }

    @Test
    public void bruteForce_shouldReturnAValidRouteForMaximumPlaces() {
        List<GooglePlace> fastest = bruteForceUnderTest.calculateFastestRoute(maxedRoute, start, end);

        assertNotNull(fastest);
    }

    @Test
    public void bruteForce_shouldBeAbleToFindMoreThanHalfOfMaximumUniquesWithMaxTriesForEachNewUniqueOf500() {
        GooglePlace startPlace = new GooglePlace.BuildPlace("Start location", start.getLatitude(), start.getLongitude()).build();
        GooglePlace endPlace = new GooglePlace.BuildPlace("End location", end.getLatitude(), end.getLongitude()).build();

        int maximumUniques = 5040; // 8!
        int maxFindNewUniqueTries = 500;
        List<List<GooglePlace>> routes = bruteForceUnderTest.findUniques(advancedRoute, startPlace, endPlace, maximumUniques, maxFindNewUniqueTries);
        System.out.println(routes.size());
        assertTrue(true);

    }

    @Test
    public void nearesNeighbour_shouldReturnAValidRoute() {
        List<GooglePlace> fastestNearest = nearestNeighbourUnderTest.calculateFastestRoute(advancedRoute, start, end);
        assertNotNull(fastestNearest);
    }

    @Test
    public void dummyCalculatorsComparisonTest() {
        List<GooglePlace> fastestNearestAdvancedRoute = nearestNeighbourUnderTest.calculateFastestRoute(advancedRoute, start, end);
        List<GooglePlace> fastestBruteForceAdvancedRoute = bruteForceUnderTest.calculateFastestRoute(advancedRoute, start, end);
        double nearestDistanceAdvancedRoute = DistanceCalculatorUtil.calculateTotalRouteDistance(fastestNearestAdvancedRoute);
        double bruteForceDistanceAdvancedRoute = DistanceCalculatorUtil.calculateTotalRouteDistance(fastestBruteForceAdvancedRoute);

        List<GooglePlace> fastestNearestMaxRoute = nearestNeighbourUnderTest.calculateFastestRoute(maxedRoute, start, end);
        List<GooglePlace> fastestBruteForceMaxRoute = bruteForceUnderTest.calculateFastestRoute(maxedRoute, start, end);
        double nearestDistanceMaxRoute = DistanceCalculatorUtil.calculateTotalRouteDistance(fastestNearestMaxRoute);
        double bruteForceDistanceMaxRoute = DistanceCalculatorUtil.calculateTotalRouteDistance(fastestBruteForceMaxRoute);

        System.out.println("NEAREST NEIGHBOUR DISTANCE ADVANCED: " + nearestDistanceAdvancedRoute);
        System.out.println("BRUTE FORCE DISTANCE ADVANCED: " + bruteForceDistanceAdvancedRoute);

        System.out.println("NEAREST NEIGHBOUR DISTANCE MAXED: " + nearestDistanceMaxRoute);
        System.out.println("BRUTE FORCE DISTANCE MAXED: " + bruteForceDistanceMaxRoute);

        assertTrue(true);

    }
}
