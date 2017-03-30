package lorentzonsolutions.rhome.shared;

import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.DistanceDuration;
import lorentzonsolutions.rhome.googleWebApi.DistanceDurationCalculator;
import lorentzonsolutions.rhome.googleWebApi.JSONDataParser;

public class RouteObject {

    private PlaceInformation from;
    private PlaceInformation to;
    private int durationByCar;
    private int distance;

    private DistanceDurationCalculator distanceDurationCalculator = new DistanceDurationCalculator();
    private JSONDataParser jsonDataParser = new JSONDataParser();

    public RouteObject(PlaceInformation from, PlaceInformation to) {
        this.from = from;
        this.to = to;

        String routeData = distanceDurationCalculator.calculateDistance(from.latitude, from.longitude, to.latitude, to.longitude);
        DistanceDuration distanceDuration = jsonDataParser.parseDistanceCalculationData(routeData);

        durationByCar = distanceDuration.duration;
        distance = distanceDuration.distance;
    }

    public int getDurationByCar() {
        return durationByCar;
    }

    public int getDistance() {
        return distance;
    }

    public static String printRoute(List<PlaceInformation> route) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < route.size(); i++) {
            PlaceInformation place = route.get(i);
            sb.append((i+1) + ". Name: " + place.name
                    + "\nDistance to end: " + place.distanceToEndLocation
                    + "\nDistance to start:" + place.distanceToStartLocation
                    + "\nAddress: " + place.address
                    + "\n----------------------------------------------------\n");
        }

        return sb.toString();
    }
}
