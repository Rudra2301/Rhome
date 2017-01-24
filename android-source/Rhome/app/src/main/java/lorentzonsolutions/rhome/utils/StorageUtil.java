package lorentzonsolutions.rhome.utils;

import android.location.Address;
import android.location.Location;

/**
 * Handles storage functionality. Singleton class using enum approach.
 */


public enum StorageUtil {
    INSTANCE;

    private Location selectedStartLocation;
    private Address selectedStartAddress;

    public Location getSelectedStartLocation() {return selectedStartLocation;}
    public Address getSelectedStartAddress() {return selectedStartAddress;}

    public void setSelectedStartLocation(Location location) {
        this.selectedStartLocation = location;
    }

    public void setSelectedStartAddress(Address address) {
        this.selectedStartAddress = address;
    }
}
