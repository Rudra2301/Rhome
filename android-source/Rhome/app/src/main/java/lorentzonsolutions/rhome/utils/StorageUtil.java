package lorentzonsolutions.rhome.utils;

import android.location.Address;
import android.location.Location;

import com.google.android.gms.location.places.Place;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.shared.PlaceInformation;

/**
 * Handles storage functionality. Singleton class using enum approach.
 */


public enum StorageUtil {
    INSTANCE;

    private Location selectedStartLocation;
    private Address selectedStartAddress;

    private Location selectedEndLocation;
    private Address selectedEndAddress;

    private List<PlaceInformation> selectedPlaces = new ArrayList<>();
    private List<PlaceInformation> placesNotToShow = new ArrayList<>();

    private List<PlaceInformation> fastestRoute = new ArrayList<>();

    public List<PlaceInformation> getFastestRoute() throws RouteException{
        if(fastestRoute.size() == 0)throw new RouteException("No route calculated!");
        return fastestRoute;
    }

    public void setFastestRoute(List<PlaceInformation> fastestRoute) {
        this.fastestRoute = fastestRoute;
    }

    public Location getSelectedStartLocation() {return selectedStartLocation;}
    public Address getSelectedStartAddress() {return selectedStartAddress;}

    public void setSelectedStartLocation(Location location) {
        this.selectedStartLocation = location;
    }

    public void setSelectedStartAddress(Address address) {
        this.selectedStartAddress = address;
    }

    public Location getSelectedEndLocation() {return this.selectedEndLocation;}
    public Address getSelectedEndAddress() {return this.selectedEndAddress;}

    public void setSelectedEndLocation(Location endAddress) {
        this.selectedEndLocation = endAddress;
    }

    public void setSelectedEndAddress(Address endAddress) {
        this.selectedEndAddress = endAddress;
    }

    public void addSelectedPlace(PlaceInformation place) {
        selectedPlaces.add(place);
    }
    public void removeSelectedPlace(PlaceInformation place) {
        selectedPlaces.remove(place);
    }

    // TODO. Return copy. Not reference to the actual object.
    public List<PlaceInformation> getSelectedPlacesList() {
        return this.selectedPlaces;
    }

    public void removeFromNeverShowList(PlaceInformation place) {
        if(placesNotToShow.contains(place)) placesNotToShow.remove(place);
    }
    public void addToNeverShowList(PlaceInformation place) {
        placesNotToShow.add(place);
    }

    // TODO. Return copy.
    public List<PlaceInformation> getPlacesNotToShow() {
        return this.placesNotToShow;
    }
}
