package lorentzonsolutions.rhome.modelclasses;

/**
 * This class represents a place that this application uses to display to the user.
 *
 * @author Johan Lorentzon
 *
 */

public class RhomePlace {

    public String name;
    public double latitude;
    public double longitude;
    public boolean isOpen;
    public String iconAddress;
    public String[] types;
    public String address;

    //Start location
    public int distanceToStartLocation;
    public int minutesByCarToStartLocation;
    public int minutesByBicycleToStartLocation;
    public int minutesByWalkToStartLocation;
    // End location
    public int distanceToEndLocation;
    public int minutesByCarToEndLocation;
    public int minutesByBicycleToEndLocation;
    public int minutesByWalkToEndLocation;

}
