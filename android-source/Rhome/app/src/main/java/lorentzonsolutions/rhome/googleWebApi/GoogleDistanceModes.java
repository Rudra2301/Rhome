package lorentzonsolutions.rhome.googleWebApi;

enum GoogleDistanceModes {

    DRIVING("driving"), WALKING("walking"), BICYCLING("walking");

    String googleName;

    GoogleDistanceModes(String name) {
        this.googleName = name;
    }

    @Override
    public String toString() {
        return this.googleName;
    }

}
