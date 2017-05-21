package lorentzonsolutions.rhome.utils;

import android.location.Address;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;

/**
 * Handles storage functionality. Singleton class using enum approach.
 */


public enum TemporalStorageUtil {
    INSTANCE;

    // START AND END LOCATIONS
    private Location selectedStartLocation;
    private Address selectedStartAddress;
    private Location selectedEndLocation;
    private Address selectedEndAddress;
    public Location getSelectedStartLocation() {return selectedStartLocation;}
    public Address getSelectedStartAddress() {return selectedStartAddress;}
    public void setSelectedStartLocation(Location location) {this.selectedStartLocation = location;}
    public void setSelectedStartAddress(Address address) {this.selectedStartAddress = address;}
    public Location getSelectedEndLocation() {return this.selectedEndLocation;}
    public Address getSelectedEndAddress() {return this.selectedEndAddress;}
    public void setSelectedEndLocation(Location endAddress) {this.selectedEndLocation = endAddress;}
    public void setSelectedEndAddress(Address endAddress) {
        this.selectedEndAddress = endAddress;
    }

    //SELECTED PLACES
    private List<GooglePlace> selectedPlaces = new ArrayList<>();
    public void addSelectedPlace(GooglePlace place) {
        selectedPlaces.add(place);
        // TODO. Increment value of place type in DB.
    }
    public void removeSelectedPlace(GooglePlace place) {
        selectedPlaces.remove(place);
        // TODO. Decrease value of place type in DB.
    }
    // TODO. Return copy. Not reference to the actual object.
    public List<GooglePlace> getSelectedPlacesList() {
        return this.selectedPlaces;
    }

    // PLACES NOT TO SHOW
    private List<GooglePlace> placesNotToShow = new ArrayList<>();
    public void removeFromNeverShowList(GooglePlace place) {
        if(placesNotToShow.contains(place)) placesNotToShow.remove(place);
    }
    public void addToNeverShowList(GooglePlace place) {
        placesNotToShow.add(place);
    }
    // TODO. Return copy.
    public List<GooglePlace> getPlacesNotToShow() {
        return this.placesNotToShow;
    }

    // FASTEST ROUTE
    private List<GooglePlace> fastestRoute = new ArrayList<>();
    public List<GooglePlace> getFastestRoute() throws RouteException{
        if(fastestRoute.size() == 0)throw new RouteException("No route calculated!");
        return fastestRoute;
    }
    public void setFastestRoute(List<GooglePlace> fastestRoute) {
        this.fastestRoute = fastestRoute;
    }

    // PLACE TO PLACE LIST
    public List<List<GooglePlace>> splitToMinorRoutes(List<GooglePlace> route) {
        List<List<GooglePlace>> splitted = new ArrayList<>();

        GooglePlace placeBefore = null;
        for(int i = 0; i < route.size(); i++) {
            List<GooglePlace> minorRoute = new ArrayList<>();
            GooglePlace placeNow = route.get(i);
            if(placeBefore != null) {
                minorRoute.add(placeBefore); minorRoute.add(placeNow);
                splitted.add(minorRoute);
            }
            placeBefore = placeNow;
        }

        System.out.println("Minor routes list calculated");
        int calc = 1;
        for(List<GooglePlace> minor : splitted) {
            System.out.println(calc + " st route: \n" + minor);
            calc++;
        }
        return splitted;
    }


}
