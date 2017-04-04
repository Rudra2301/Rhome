package lorentzonsolutions.rhome.utils;

import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

/**
 * Class to convert between location types. NOTE: use the FetchAddressIntentService to convert to an address.
 */
public enum LocationConverter {
    INSTANCE;

    public Location placeToLocation(Place place) {
        Location location = new Location("");
        location.setLatitude(place.getLatLng().latitude);
        location.setLongitude(place.getLatLng().longitude);

        return location;
    }

    public Location latLngToLocation(LatLng latLng) {
        Location location = new Location("");
        location.setLatitude(latLng.latitude);
        location.setLongitude(latLng.longitude);

        return location;
    }

    public LatLng locationToLatLng(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }
}
