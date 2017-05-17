package lorentzonsolutions.rhome.googleWebApi;

/**
 * A helper class to contain the distance and durations data between places.
 */

public class GoogleDistanceAndDurationObject {

    public int distance;
    public int duration;

    public GoogleDistanceAndDurationObject(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Total minutes: " + duration + ". Hours: " + duration/60 + ". Minutes: " + duration%60;
    }

}
