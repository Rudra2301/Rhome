package lorentzonsolutions.rhome.utils;

import android.location.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import lorentzonsolutions.rhome.shared.PlaceInformation;
import lorentzonsolutions.rhome.shared.RouteObject;

/**
 * Created by johanlorentzon on 2017-01-30.
 */

public class RouteCalculator {
    Location startLocation = StorageUtil.INSTANCE.getSelectedStartLocation();
    PlaceInformation startPlace = new PlaceInformation.BuildPlace("Start location", startLocation.getLatitude(), startLocation.getLongitude()).build();
    // TODO. Set end place.

    private List<PlaceInformation> route;

    public RouteCalculator(List<PlaceInformation> route) {
        this.route = route;
    }

    private List<PlaceInformation> fastestTimeRoute = new ArrayList<>();
    private int fastestTime;
    private int fastestTimeDistance;

    private List<PlaceInformation> shortestDistanceRoute = new ArrayList<>();
    private int shortestDistance;
    private int shortestDistanceTime;

    private List<List<PlaceInformation>> triedRoutes = new ArrayList<>();

    public void CalculateFastestTime() {

        // TODO. Check out generic algorithms to make the best calculation.

        setOrderIDs();
        List<PlaceInformation> newRoute;
        
        while((newRoute = findNewRoute()) != null) {

            triedRoutes.add(newRoute);

        }

    }

    // Setting orderID to each placeobject
    private void setOrderIDs() {
        for(int i = 0; i < route.size(); i++) {
            route.get(i).setOrderId(i);
        }
    }

    private List<PlaceInformation> findNewRoute() {
        // If it's the first run. Return the list unmodified.
        if(triedRoutes.size() == 0) return route;

        else {
            List<PlaceInformation> newRoute = new ArrayList<>();
            // TODO. Copy list?
            int nrOfTries = 0;
            while(nrOfTries < 100) {
                Collections.copy(newRoute, route);
                Collections.shuffle(newRoute);
                if(!routeIsTried(newRoute)) return newRoute;
                nrOfTries++;
            }
            return null;
        }

    }

    private boolean routeIsTried(List<PlaceInformation> route) {
        int[] routeOne = new int[route.size()];
        for(int i = 0; i < routeOne.length; i++) routeOne[i] = route.get(i).getOrderId();

        for(List<PlaceInformation> triedRoute : triedRoutes) {
            int[] routeTwo = new int[triedRoute.size()];
            for(int i = 0; i < routeTwo.length; i++) routeTwo[i] = triedRoute.get(i).getOrderId();
            if(routesIsEqual(routeOne,routeTwo)) return true;
        }
        return false;
    }

    private boolean routesIsEqual(int[] routeOne, int[] routeTwo) {
        for(int i = 0; i < routeOne.length; i++) {
            if(routeOne[i] == routeTwo[i]) return true;
        }
        return false;
    }
}
