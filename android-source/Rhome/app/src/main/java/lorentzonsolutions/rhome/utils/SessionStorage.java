package lorentzonsolutions.rhome.utils;

import android.location.Address;
import android.location.Location;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;

/**
 * Handles storage functionality temporarily while application is active. Singleton class using enum approach.
 *
 */


public enum SessionStorage {
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

    private List<GooglePlace> placesNotToShow = new ArrayList<>();
    private List<GooglePlace> fastestRoute = new ArrayList<>();
    private List<GooglePlace> selectedPlaces = new ArrayList<>();

    public void addSelectedPlace(GooglePlace place) {
        selectedPlaces.add(place);
    }
    public void removeSelectedPlace(GooglePlace place) {
        selectedPlaces.remove(place);
    }
    public List<GooglePlace> getSelectedPlacesList() {
        return new ArrayList<>(selectedPlaces);
    }

    public void removeFromNeverShowList(GooglePlace place) {
        if(placesNotToShow.contains(place)) placesNotToShow.remove(place);
    }
    public void addToNeverShowList(GooglePlace place) {
        placesNotToShow.add(place);
    }

    public List<GooglePlace> getPlacesNotToShow() {
        return new ArrayList<>(placesNotToShow);
    }

    public List<GooglePlace> getFastestRoute() {
        return new ArrayList<>(fastestRoute);
    }
    public void setFastestRoute(List<GooglePlace> fastestRoute) {
        this.fastestRoute = fastestRoute;
    }

    /**
     * Takes a list of places and splits this into a list of list where each list in the list contains of 2 places which to travel between.
     *
     * @param route
     * @return a list of lists for place-to-place route.
     */
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
