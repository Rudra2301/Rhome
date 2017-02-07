package lorentzonsolutions.rhome.utils;

import android.location.Location;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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


    private List<List<PlaceInformation>> triedRoutes = new ArrayList<>();

    public void CalculateFastestTime() {

        // TODO. Check - Ant Colony Optimization
        setOrderIDs();
        List<PlaceInformation> newRoute;

        class PlaceDistanceComparator implements Comparator<PlaceInformation> {
            public int compare(PlaceInformation p1, PlaceInformation p2) {
                return p1.distanceToStartLocation - p2.distanceToStartLocation;
            }
        }
        Collections.sort(route, new PlaceDistanceComparator());

        for(PlaceInformation place: route) {
            System.out.println(place.name + " - Distance to start; " + place.distanceToStartLocation);
        }


    }

    // Setting orderID to each placeobject
    private void setOrderIDs() {
        for(int i = 0; i < route.size(); i++) {
            route.get(i).setOrderId(i);
        }
    }

    private static double distance(double fromLat, double fromLng, double toLat, double toLng) {

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