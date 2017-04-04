package lorentzonsolutions.rhome.shared;

/**
 * Helper object for holding information about a distance and duration between two places.
 */
public class DistanceDurationInfoObject {

    public int distance;
    public int duration;

    public DistanceDurationInfoObject(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Total minutes: " + duration + ". Hours: " + duration/60 + ". Minutes: " + duration%60;
    }
}
