package lorentzonsolutions.rhome.googleWebApi;

/**
 * Created by johanlorentzon on 2017-01-27.
 */

public class DistanceDuration {

    public int distance;
    public int duration;

    public DistanceDuration(int distance, int duration) {
        this.distance = distance;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "Total minutes: " + duration + ". Hours: " + duration/60 + ". Minutes: " + duration%60;
    }
}
