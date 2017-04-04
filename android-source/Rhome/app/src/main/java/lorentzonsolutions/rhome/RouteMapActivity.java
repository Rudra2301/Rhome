package lorentzonsolutions.rhome;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.googleWebApi.DirectionsAPI;
import lorentzonsolutions.rhome.googleWebApi.JSONDataParser;
import lorentzonsolutions.rhome.shared.GooglePlaceInformation;
import lorentzonsolutions.rhome.utils.StorageUtil;

public class RouteMapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final String TAG = RouteMapActivity.class.toString();

    // Google API
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    // DirectionsAPI
    private DirectionsAPI directionsAPI = new DirectionsAPI();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_map);

        buildGoogleApiClient();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_route);

        mapFragment.getMapAsync(this);
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
        LatLng cameraPosition = new LatLng(StorageUtil.INSTANCE.getSelectedStartLocation().getLatitude(),
                StorageUtil.INSTANCE.getSelectedStartLocation().getLongitude());
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
            latLngRoute = makeLatLngList(StorageUtil.INSTANCE.getFastestRoute());
            placeMarkersForRoute(StorageUtil.INSTANCE.getFastestRoute());

            if(latLngRoute != null && latLngRoute.size() >= 3) {
                Iterator iterator = latLngRoute.iterator();
                LatLng start = (LatLng) iterator.next();
                LatLng next;
                while (iterator.hasNext()) {
                    next = (LatLng) iterator.next();

                    LatLng startLatLng = new LatLng(start.latitude, start.longitude);
                    LatLng endLatLng = new LatLng(next.latitude, next.longitude);

                    String directionsUrl = directionsAPI.directionsUrlForRoute(startLatLng, endLatLng);
                    Log.d(TAG, "URL for Google DirectionsAPI API created: \n" + directionsUrl);

                    DownloadDirectionsData downloadDirectionsData = new DownloadDirectionsData();
                    downloadDirectionsData.execute(directionsUrl);

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

    private void placeMarkersForRoute(List<GooglePlaceInformation> route) {
        for(GooglePlaceInformation place : route) {
            MarkerOptions marker = new MarkerOptions();
            marker.position(new LatLng(place.latitude, place.longitude)).title(place.name);
            mMap.addMarker(marker);
        }
    }

    private List<LatLng> makeLatLngList(List<GooglePlaceInformation> googlePlaceInformationList) {
        List<LatLng> latLngList = new ArrayList<>();
        for(GooglePlaceInformation place : googlePlaceInformationList) latLngList.add(new LatLng(place.latitude, place.longitude));
        return latLngList;
    }

    // ------------------------ ASYNTASK CLASSES -------------------------------- //

    private class DownloadDirectionsData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String directionsApiUrl = url[0];
            String data = directionsAPI.downloadDirectionsData(directionsApiUrl);
            Log.d(TAG, "Data downloaded. \n" + data);
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            List<List<HashMap<String, String>>> routes = null;

            try{
                JSONDataParser jsonDataParser = new JSONDataParser();

                // Starts parsing data
                routes = jsonDataParser.parseDirectionsData(jsonData[0]);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
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

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    */
}
