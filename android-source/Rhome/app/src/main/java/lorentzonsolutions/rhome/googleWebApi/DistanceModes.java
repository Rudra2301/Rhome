package lorentzonsolutions.rhome.googleWebApi;

/**
 * Created by johanlorentzon on 2017-01-27.
 */

public enum DistanceModes {

    DRIVING("driving"), WALKING("walking"), BICYCLING("walking");

    String googleName;

    DistanceModes(String name) {
        this.googleName = name;
    }

    @Override
    public String toString() {
        return this.googleName;
    }

}
