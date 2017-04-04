package lorentzonsolutions.rhome.utils;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.shared.GooglePlaceInformation;

/**
 * Handles storage functionality. Singleton class using enum approach.
 */


public enum StorageUtil {
    INSTANCE;

    private Location selectedStartLocation;
    private Address selectedStartAddress;

    private Location selectedEndLocation;
    private Address selectedEndAddress;

    private List<GooglePlaceInformation> selectedPlaces = new ArrayList<>();
    private List<GooglePlaceInformation> placesNotToShow = new ArrayList<>();

    private List<GooglePlaceInformation> fastestRoute = new ArrayList<>();

    public List<GooglePlaceInformation> getFastestRoute() throws RouteException{
        if(fastestRoute.size() == 0)throw new RouteException("No route calculated!");
        return fastestRoute;
    }

    public void setFastestRoute(List<GooglePlaceInformation> fastestRoute) {
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

    public boolean permissionsGiven(AppCompatActivity activity) {
        return ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }
}
