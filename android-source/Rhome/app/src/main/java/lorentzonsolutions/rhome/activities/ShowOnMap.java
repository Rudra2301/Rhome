package lorentzonsolutions.rhome.activities;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.shared.GooglePlaceInformation;

public class ShowOnMap extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private final String TAG = "PLACE_MAP";

    private GooglePlaceInformation place;

    // Google API
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    // TODO. Show users location

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_on_map);

        place = getIntent().getExtras().getParcelable("place_to_show");

        buildGoogleApiClient();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragmentMyLocation = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_place);

        mapFragmentMyLocation.getMapAsync(this);

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        moveCamera(new LatLng(place.latitude, place.longitude));

        Log.i(TAG, "Adding marker to map.");
        // Placing marker on map
        mMap.clear();
        MarkerOptions marker = new MarkerOptions();
        marker.position(new LatLng(place.latitude, place.longitude)).title(place.name).snippet(place.address);
        // Setting icons
        // marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.example))
        mMap.addMarker(marker);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection to the Google API suspended.");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "Connection to Google API failed :\n " + connectionResult.getErrorMessage());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "Map loaded and ready.");
        mMap = googleMap;

    }

    // Using the GoogleApiClient builder to set the reference of mGoogleApiClient.
    private void buildGoogleApiClient() {

        // TODO. Enable autoManage like: .enableAutoManage(this *FragmentActivity*, this *OnConnectionFailedListener) in builder.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .enableAutoManage(this,this)
                    .build();
        }
    }

    private void moveCamera(LatLng location) {
        if (location == null) {
            Log.i(TAG, "Location is missing.");
            return;
        }
            // Moving camera
            Log.i(TAG, "Moving camera to selected place");
            CameraPosition position = CameraPosition.builder().bearing(0).tilt(0).zoom(12).target(location).build();
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));


    }
}
