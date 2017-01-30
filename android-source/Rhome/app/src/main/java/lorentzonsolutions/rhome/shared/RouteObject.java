package lorentzonsolutions.rhome.shared;

import lorentzonsolutions.rhome.googleWebApi.DistanceDuration;
import lorentzonsolutions.rhome.googleWebApi.DistanceDurationCalculator;
import lorentzonsolutions.rhome.googleWebApi.JSONDataParser;

/**
 * Created by johanlorentzon on 2017-01-30.
 */

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
}
