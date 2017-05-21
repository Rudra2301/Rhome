package lorentzonsolutions.rhome.activities;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.googleWebApi.GoogleWebApiUtil;
import lorentzonsolutions.rhome.interfaces.WebApiUtil;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.utils.TemporalStorageUtil;

public class MinorRoutesMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = MinorRoutesMapActivity.class.toString();

    // Google API
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minor_routes_map);

        buildGoogleApiClient();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_route);

        mapFragment.getMapAsync(this);

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.activity_route_map_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "Connected to the Google API.");
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

        // Setting camera in position.
        LatLng cameraPosition = new LatLng(TemporalStorageUtil.INSTANCE.getSelectedStartLocation().getLatitude(),
                TemporalStorageUtil.INSTANCE.getSelectedStartLocation().getLongitude());
        CameraPosition position = CameraPosition.builder()
                .bearing(0)
                .tilt(0)
                .zoom(10)
                .target(cameraPosition)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));

        buildRoute();
    }

    private void buildRoute() {

        // Drawing route
        List<LatLng> latLngRoute;

        try {
            latLngRoute = makeLatLngList(TemporalStorageUtil.INSTANCE.getFastestRoute());
            placeMarkersForRoute(TemporalStorageUtil.INSTANCE.getFastestRoute());

            if(latLngRoute != null && latLngRoute.size() >= 3) {
                Iterator iterator = latLngRoute.iterator();
                LatLng start = (LatLng) iterator.next();
                LatLng next;
                while (iterator.hasNext()) {
                    next = (LatLng) iterator.next();

                    LatLng startLatLng = new LatLng(start.latitude, start.longitude);
                    LatLng endLatLng = new LatLng(next.latitude, next.longitude);

                    BuildDirectionsData buildDirectionsData = new BuildDirectionsData();
                    buildDirectionsData.execute(startLatLng, endLatLng);

                    start = next;
                }
            } else {
                Log.d(TAG, "Route is not set or does not contain any selected places.");
            }

        } catch (RouteException e) {
            Log.d(TAG, "Exception getting route. \n" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void placeMarkersForRoute(List<GooglePlace> route) {
        for(GooglePlace place : route) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(place.latitude, place.longitude)).title(place.name);
            mMap.addMarker(marker);
        }
    }

    private List<LatLng> makeLatLngList(List<GooglePlace> googlePlaceList) {
        List<LatLng> latLngList = new ArrayList<>();
        for(GooglePlace place : googlePlaceList) latLngList.add(new LatLng(place.latitude, place.longitude));
        return latLngList;
    }

    // ------------------------ ASYNC TASK CLASSES -------------------------------- //

    private class BuildDirectionsData extends AsyncTask<LatLng, Void, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(LatLng... latLngs) {
            WebApiUtil webApiUtil = new GoogleWebApiUtil();
            LatLng start = latLngs[0];
            LatLng end = latLngs[1];
            List<List<HashMap<String, String>>> result = webApiUtil.getPolylineData(start, end);
            return result;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            super.onPostExecute(result);
            drawPolyline(result);
        }
    }

    private void drawPolyline(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;

        // Traversing through all the routes
        for(int i=0;i<result.size();i++){
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();

            // Fetching i-th route
            List<HashMap<String, String>> path = result.get(i);

            // Fetching all the points in i-th route
            for(int j=0;j<path.size();j++){
                HashMap<String,String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }

            // Adding all the points in the route to LineOptions
            lineOptions.addAll(points);
            lineOptions.width(6);
            lineOptions.color(Color.RED);
        }

        // Drawing polyline in the Google Map for the i-th route
        mMap.addPolyline(lineOptions);
    }

}
