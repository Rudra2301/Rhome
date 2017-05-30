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
import lorentzonsolutions.rhome.utils.DistanceCalculatorUtil;
import lorentzonsolutions.rhome.routeCalculators.NearestNeighbourRouteCalculator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;


/**
 * @author Johan Lorentzon
 * @since 22/5 - 2017
 *
 * Test for RouteCalculatorsTest.class
 */

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class})
public class RouteCalculatorsTest {

    List<GooglePlace> simpleRoute;
    List<GooglePlace> advancedRoute;
    List<GooglePlace> maxedRoute;

    ChanceByBruteForce bruteForceUnderTest;
    NearestNeighbourRouteCalculator nearestNeighbourUnderTest;

    Location start;
    Location end;

    Location simpleFirst;
    Location simpleLast;

    // Variables for simple list.
    GooglePlace simpleTwo;
    GooglePlace simpleThree;
    GooglePlace simpleFour;

    @Before
    public void before() {
        PowerMockito.mockStatic(Log.class);

        bruteForceUnderTest = new ChanceByBruteForce();
        nearestNeighbourUnderTest = new NearestNeighbourRouteCalculator();

        simpleRoute = new ArrayList<>();
        advancedRoute = new ArrayList<>();
        maxedRoute = new ArrayList<>();

        // Mocking locations
        start = Mockito.mock(Location.class);
        end = Mockito.mock(Location.class);

        doReturn(57.722853).when(start).getLatitude();
        doReturn(11.939049).when(start).getLongitude();

        doReturn(57.672583).when(end).getLatitude();
        doReturn(11.997757).when(end).getLongitude();

        GooglePlace place2 = new GooglePlace.BuildPlace("PLACE2", 57.704147, 12.012863).build();
        GooglePlace place3 = new GooglePlace.BuildPlace("PLACE3", 57.738067, 12.057152).build();
        GooglePlace place4 = new GooglePlace.BuildPlace("PLACE4", 57.681761, 11.953125).build();
        GooglePlace place5 = new GooglePlace.BuildPlace("PLACE5", 57.668543, 12.057152).build();
        GooglePlace place6 = new GooglePlace.BuildPlace("PLACE6", 57.712951, 11.975441).build();
        GooglePlace place7 = new GooglePlace.BuildPlace("PLACE7", 57.731102, 12.01767).build();

        GooglePlace extra1 = new GooglePlace.BuildPlace("EXTRA1", 57.678136, 11.906433).build();
        GooglePlace extra2 = new GooglePlace.BuildPlace("EXTRA2", 57.710567, 11.903343).build();
        GooglePlace extra3 = new GooglePlace.BuildPlace("EXTRA3", 57.689286, 11.986771).build();


        advancedRoute.add(place2); advancedRoute.add(place4); advancedRoute.add(place3);
        advancedRoute.add(place7); advancedRoute.add(place6); advancedRoute.add(place5);

        maxedRoute.addAll(advancedRoute); maxedRoute.add(extra1); maxedRoute.add(extra2); maxedRoute.add(extra3);

        // ------------ SIMPLE ROUTE ------------------- //
        simpleFirst = Mockito.mock(Location.class);
        simpleLast = Mockito.mock(Location.class);

        doReturn(57.685249).when(simpleFirst).getLatitude();
        doReturn(11.926346).when(simpleFirst).getLongitude();

        doReturn(57.730919).when(simpleLast).getLatitude();
        doReturn(12.113113).when(simpleLast).getLongitude();

        simpleTwo = new GooglePlace.BuildPlace("simpleTwo", 57.69314, 11.968231).build();
        simpleThree = new GooglePlace.BuildPlace("simpleThree", 57.701763, 12.010803).build();
        simpleFour = new GooglePlace.BuildPlace("simpleFour", 57.714235, 12.055092).build();

        simpleRoute.add(simpleThree); simpleRoute.add(simpleFour); simpleRoute.add(simpleTwo);

        simpleFour.distanceToEndLocation = 10; simpleFour.distanceToStartLocation = 20;
        simpleThree.distanceToEndLocation = 15; simpleThree.distanceToStartLocation = 15;
        simpleTwo.distanceToEndLocation = 20; simpleThree.distanceToStartLocation = 10;

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
    public void bruteForce_shouldBeAbleToFindMoreThanHalfOfMaximumUniquesWithMaxTriesForEachNewUniqueOf500WithEightPlaces() {
        GooglePlace startPlace = new GooglePlace.BuildPlace("Start location", start.getLatitude(), start.getLongitude()).build();
        GooglePlace endPlace = new GooglePlace.BuildPlace("End location", end.getLatitude(), end.getLongitude()).build();

        int maximumUniques = 5040; // 8!
        int maxFindNewUniqueTries = 500;
        List<List<GooglePlace>> routes = bruteForceUnderTest.findUniques(maxedRoute, startPlace, endPlace, maximumUniques, maxFindNewUniqueTries);
        System.out.println(routes.size());
        assertTrue(true);

    }

    @Test
    public void bruteForce_shouldBeAbleToFindMoreThanHalfOfMaximumUniquesWithMaxTriesForEachNewUniqueOf500WithSixPlaces() {
        GooglePlace startPlace = new GooglePlace.BuildPlace("Start location", start.getLatitude(), start.getLongitude()).build();
        GooglePlace endPlace = new GooglePlace.BuildPlace("End location", end.getLatitude(), end.getLongitude()).build();

        int maximumUniques = 5040; // 7!
        int maxFindNewUniqueTries = 500;
        List<List<GooglePlace>> routes = bruteForceUnderTest.findUniques(advancedRoute, startPlace, endPlace, maximumUniques, maxFindNewUniqueTries);
        System.out.println(routes.size());
        assertTrue(routes.size() > 720/2);
    }

    @Test
    public void nearesNeighbour_shouldReturnAValidRoute() {
        List<GooglePlace> fastestNearest = nearestNeighbourUnderTest.calculateFastestRoute(advancedRoute, start, end);
        assertNotNull(fastestNearest);
    }

    @Test
    public void nearestNeighbourShouldFindCorrectRoute() {
        List<GooglePlace> fastest = nearestNeighbourUnderTest.calculateFastestRoute(simpleRoute, simpleFirst, simpleLast);
        // Assert start place is first in list
        assertTrue(simpleFirst.getLatitude() == fastest.get(0).latitude);
        assertTrue(simpleFirst.getLongitude() == fastest.get(0).longitude);

        // Assert end place is last in list
        assertTrue(simpleLast.getLatitude() == fastest.get(fastest.size() - 1).latitude);
        assertTrue(simpleLast.getLongitude() == fastest.get(fastest.size() - 1).longitude);


        // Assert the correct place order
        assertTrue(simpleTwo.equals(fastest.get(1)));
        assertTrue(simpleThree.equals(fastest.get(2)));
        assertTrue(simpleFour.equals(fastest.get(3)));

        for(GooglePlace place : fastest) System.out.println(place.name);
    }

    // Dummy test for comparison between algorithms. Most of the time the
    @Test
    public void nearestNeighbourComparedToBruteForceWhenPlacesExceedsSeven() {
        GooglePlace startPlace = new GooglePlace.BuildPlace("Start location", start.getLatitude(), start.getLongitude()).build();
        GooglePlace endPlace = new GooglePlace.BuildPlace("End location", end.getLatitude(), end.getLongitude()).build();

        int maximumUniques = 5040; // 7!
        int maxFindNewUniqueTries = 500;
        List<List<GooglePlace>> routes = bruteForceUnderTest.findUniques(advancedRoute, startPlace, endPlace, maximumUniques, maxFindNewUniqueTries);
        System.out.println(routes.size());
        assertTrue(routes.size() > 720/2);
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
