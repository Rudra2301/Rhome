package lorentzonsolutions.rhome.shared;

import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.NearbyLocationSearcher;

/**
 * This class contains a NearByLocationSearcher and a list to populate the result in.
 * This helps the async task populate a list outside the main thread.
 */

public class LocationList {
    private NearbyLocationSearcher nearbyLocationSearcher;
    private List<PlaceInformation> placeInformationList;

    public LocationList(NearbyLocationSearcher nearbyLocationSearcher, List<PlaceInformation> placeInformationList) {
        this.nearbyLocationSearcher = nearbyLocationSearcher;
        this.placeInformationList = placeInformationList;
    }

    public NearbyLocationSearcher getNearbyLocationSearcher() {return this.nearbyLocationSearcher;}
    public List<PlaceInformation> getPlaceInformationList() {return placeInformationList;}
}
