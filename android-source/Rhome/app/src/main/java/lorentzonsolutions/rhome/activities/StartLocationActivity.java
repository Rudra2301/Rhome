package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Locale;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.utils.LocationConverter;
import lorentzonsolutions.rhome.utils.SessionStorage;

public class StartLocationActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = StartLocationActivity.class.toString();
    private final Context context = this;

    // Google API
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private boolean mapIsReady = false;
    private boolean isUpdatingSelectedAddress = false;

    // Location variable
    private Location currentLocation;
    private Location selectedLocation;
    private Address selectedAddress;

    // View Objects
    Button myLocationButton;
    Button setStartLocationButton;
    TextView info;
    TextView infoHeader;


    // Storage object singleton
    SessionStorage sessionStorage = SessionStorage.INSTANCE;

    // Location converter singleton
    LocationConverter locationConverter = LocationConverter.INSTANCE;

    Geocoder geocoder = new Geocoder(this, Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mapIsReady = false;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_location);

        myLocationButton = (Button) findViewById(R.id.select_start_use_current_position_button);
        setStartLocationButton = (Button) findViewById(R.id.select_start_done_button);
        info = (TextView) findViewById(R.id.select_start_info_text);
        infoHeader = (TextView) findViewById(R.id.select_start_info_header);

        // Setting up the Google Api Client
        buildGoogleApiClient();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragmentMyLocation = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragmentMyLocation.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Map loaded and ready.");
        mapIsReady = true;
        mMap = googleMap;

        // Initializing listeners
        initEvents();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to the Google API.");

        getCurrentLocation();
        if(currentLocation != null){
            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection to the Google API suspended.");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // Connection to the Google API failed for some reason. Log.
        Log.i(TAG, "Connection to Google API failed :\n " + connectionResult.getErrorMessage());
    }

    private void moveCamera(LatLng location) {
        if (location == null) {
            Log.i(TAG, "Location is missing.");
            return;
        }

        if (mapIsReady) {
            // Setting the location as selected location
            selectedLocation = locationConverter.latLngToLocation(location);
            new UpdateSelectedAddress(info, mMap).execute();

            // Moving camera
            Log.i(TAG, "Moving camera to selected place");
            CameraPosition position = CameraPosition.builder().bearing(0).tilt(0).zoom(12).target(location).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
        } else {
            Log.i(TAG, "Map not ready!");
            makeSnackBar("Map not ready.");
        }

        infoHeader.setVisibility(View.VISIBLE);
        info.setVisibility(View.VISIBLE);
        setStartLocationButton.setEnabled(true);
    }

    // Using the GoogleApiClient builder to set the reference of mGoogleApiClient.
    private void buildGoogleApiClient() {

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this, this)
                    .build();
        }
    }

    private void makeSnackBar(String message) {
        Snackbar mySnackbar = Snackbar.make(getWindow().getDecorView(),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

    // Events for buttons etc.
    private void initEvents() {

        // On map marker clicks
        // TODO. Marker info window doesn't hide when clicked again
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.isInfoWindowShown()) marker.hideInfoWindow();
                else marker.showInfoWindow();
                return true;
            }
        });

        myLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCurrentLocation();
                if(currentLocation != null) {
                    LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                    selectedLocation = currentLocation;
                    moveCamera(currentLatLng);
                }
            }
        });

        // Google place select field
        // Getting the autocomplete fragment
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Setting listener to the onPlaceFragment
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName());
                moveCamera(place.getLatLng());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });

        setStartLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStartLocation();
                finish();
            }
        });
    }

    // Stores the selected location as startlocation
    private void setStartLocation() {
        if (isUpdatingSelectedAddress) {
            makeSnackBar("Updating address in progress. Please wait.");
        } else {
            if(selectedLocation != null) {
                sessionStorage.setSelectedStartLocation(selectedLocation);
                makeSnackBar("Start location has been set.");
                Log.d(TAG, "Start location has been set.");
            } else {
                makeSnackBar("Fetching location. Please wait.");
            }
        }
    }

    // Function for collecting the last known location, aka current location.
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }
    }

    // INNER CLASS FOR FETCHING ADDRESS ASYNC ------------------------------------------------------
    private class UpdateSelectedAddress extends AsyncTask<Void, Void, Void> {
        // Using weak references to update view objects on main thread.
        private final WeakReference<TextView> infoTextViewWeakReference;
        private final WeakReference<GoogleMap> googleMapWeakReference;

        private UpdateSelectedAddress(TextView infoTextViewWeakReference, GoogleMap map) {
            this.infoTextViewWeakReference = new WeakReference<>(infoTextViewWeakReference);
            this.googleMapWeakReference = new WeakReference<GoogleMap>(map);
        }

        @Override
        protected void onPreExecute() {
            isUpdatingSelectedAddress = true;
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                List<Address> addresses = geocoder.getFromLocation(
                        selectedLocation.getLatitude(),
                        selectedLocation.getLongitude(),
                        // Get just a single address.
                        1);
                if(addresses == null || addresses.size() == 0) {
                    Log.i(TAG, "No address found!");
                }
                else {
                    selectedAddress = addresses.get(0);
                    sessionStorage.setSelectedStartAddress(selectedAddress);
                    //updateSelectedLocationInfo();
                }
            }
            catch (IOException ioe) {
                Log.e(TAG, "Service not available: \n" + ioe.getMessage());
                ioe.printStackTrace();
                return null;
            }
            catch (IllegalArgumentException iae) {
                Log.e(TAG, "Invalid latitude or longitude: \n" + iae.getMessage());
                iae.printStackTrace();
                return null;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            isUpdatingSelectedAddress = false;
            if(infoTextViewWeakReference.get() != null) {
                Log.i(TAG, "Updating location information text.");
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < selectedAddress.getMaxAddressLineIndex(); i++) {
                    sb.append(selectedAddress.getAddressLine(i) + "\n");
                }
                infoTextViewWeakReference.get().setText(sb.toString());
            }
            if(googleMapWeakReference.get() != null) {
                Log.i(TAG, "Adding marker to map.");
                // Placing marker on map
                googleMapWeakReference.get().clear();
                MarkerOptions marker = new MarkerOptions();
                marker.position(locationConverter.locationToLatLng(selectedLocation)).title(selectedAddress.getAddressLine(0));
                googleMapWeakReference.get().addMarker(marker);
            }
        }
    }

}
