package lorentzonsolutions.rhome.utils;

import android.location.Address;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.shared.GooglePlaceInformation;

/**
 * Handles storage functionality. Singleton class using enum approach.
 */


public enum StorageUtil {
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
    private List<GooglePlaceInformation> selectedPlaces = new ArrayList<>();
    public void addSelectedPlace(GooglePlaceInformation place) {
        selectedPlaces.add(place);
    }
    public void removeSelectedPlace(GooglePlaceInformation place) {
        selectedPlaces.remove(place);
    }
    // TODO. Return copy. Not reference to the actual object.
    public List<GooglePlaceInformation> getSelectedPlacesList() {
        return this.selectedPlaces;
    }

    // PLACES NOT TO SHOW
    private List<GooglePlaceInformation> placesNotToShow = new ArrayList<>();
    public void removeFromNeverShowList(GooglePlaceInformation place) {
        if(placesNotToShow.contains(place)) placesNotToShow.remove(place);
    }
    public void addToNeverShowList(GooglePlaceInformation place) {
        placesNotToShow.add(place);
    }
    // TODO. Return copy.
    public List<GooglePlaceInformation> getPlacesNotToShow() {
        return this.placesNotToShow;
    }

    // FASTEST ROUTE
    private List<GooglePlaceInformation> fastestRoute = new ArrayList<>();
    public List<GooglePlaceInformation> getFastestRoute() throws RouteException{
        if(fastestRoute.size() == 0)throw new RouteException("No route calculated!");
        return fastestRoute;
    }
    public void setFastestRoute(List<GooglePlaceInformation> fastestRoute) {
        this.fastestRoute = fastestRoute;
    }

    // PLACE TO PLACE LIST
    public List<List<GooglePlaceInformation>> splitToMinorRoutes(List<GooglePlaceInformation> route) {
        List<List<GooglePlaceInformation>> splitted = new ArrayList<>();

        GooglePlaceInformation placeBefore = null;
        for(int i = 0; i < route.size(); i++) {
            List<GooglePlaceInformation> minorRoute = new ArrayList<>();
            GooglePlaceInformation placeNow = route.get(i);
            if(placeBefore != null) {
                minorRoute.add(placeBefore); minorRoute.add(placeNow);
                splitted.add(minorRoute);
            }
            placeBefore = placeNow;
        }

        System.out.println("Minor routes list calculated");
        int calc = 1;
        for(List<GooglePlaceInformation> minor : splitted) {
            System.out.println(calc + " st route: \n" + minor);
            calc++;
        }
        return splitted;
    }


}
