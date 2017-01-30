package lorentzonsolutions.rhome.googleWebApi;

import lorentzonsolutions.rhome.utils.Resources;

/**
 * Collects responses from the Google Web API.
 */

public class NearbyLocationSearcher {
    // Google api documentation
    // https://developers.google.com/places/web-service/search

    // Supported types for searches is found here:
    // https://developers.google.com/places/web-service/supported_types

    private NearbyLocationSearcher(Builder builder) {
        this.locationLatitude = builder.latitude;
        this.locationLongitude = builder.longitude;
        this.radius = builder.radius;
        this.type = builder.type;
    }

    private final String TAG = "NEARBY_LOCATION_SERACHER";
    private final String API_KEY = Resources.getInstance().getAPI_KEY_WEB_API();

    private URLDataReceiver urlDataReceiver = new URLDataReceiver();

    private double locationLongitude;
    private double locationLatitude;

    // In meters (max 50 000)
    private int radius;

    private String type;

    public String retrieveNearbyLocations() {
        return urlDataReceiver.readURL(buildURL());
    }

    private String buildURL() {
        // latitude - longitude
        String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" +
                this.locationLatitude + "," +
                this.locationLongitude +
                "&radius=" + this.radius +
                "&types=" + this.type +
                "&key=" + API_KEY;

        return url;
    }



    // BUILDER -------------------------------------------------------------------------------------
    public static class Builder {
        private final double longitude;
        private final double latitude;
        private int radius;
        private String type;

        public Builder(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public Builder radius(int radius) {
            this.radius = radius;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public NearbyLocationSearcher build() {
            return new NearbyLocationSearcher(this);
        }

    }

}
