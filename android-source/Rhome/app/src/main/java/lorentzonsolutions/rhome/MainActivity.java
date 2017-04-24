package lorentzonsolutions.rhome;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat.OnRequestPermissionsResultCallback;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.activities.RouteActivity;
import lorentzonsolutions.rhome.shared.GooglePlaceInformation;
import lorentzonsolutions.rhome.utils.RouteCalculator;
import lorentzonsolutions.rhome.utils.StorageUtil;

public class MainActivity extends AppCompatActivity implements OnRequestPermissionsResultCallback {

    private static final int LOCATION_PERMISSIONS = 101;

    private Context thisContext = this;

    // Helper objects
    private StorageUtil storageUtil = StorageUtil.INSTANCE;

    private ArrayAdapter<GooglePlaceInformation> selectedListAdapter;
    ListView selectedPlacesList;
    private HashMap<GooglePlaceInformation, WeakReference<ImageView>> placeIconMap = new HashMap<>();
    private final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Hiding action bar on top
        // getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        //Resources.getInstance().setContext(this);


;
    }


    @Override
    protected void onResume() {

        if(storageUtil.getSelectedPlacesList() != null && storageUtil.getSelectedPlacesList().size() != 0) {
            Button calculateFastestTime = (Button) findViewById(R.id.fastest_time);
            calculateFastestTime.setVisibility(View.VISIBLE);
            findViewById(R.id.selected_places_header).setVisibility(View.VISIBLE);

            calculateFastestTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RouteCalculator calculator = new RouteCalculator();
                    List<GooglePlaceInformation> fastestRoute = calculator.calculateFastestTime(storageUtil.getSelectedPlacesList(), true);
                    storageUtil.setFastestRoute(fastestRoute);
                    Log.d(TAG, "Calculated fastest route: ");
                    for(GooglePlaceInformation place : fastestRoute) {
                        Log.d(TAG, place.name + " | Distance to start: " + place.distanceToStartLocation);
                    }

                    double totalDistance = calculator.calculateTotalRouteDistance(fastestRoute);
                    Log.d(TAG, "Total distance: " + totalDistance + " km.");

                    // Starting route activity
                    Intent intent = new Intent(thisContext, RouteActivity.class);
                    startActivity(intent);
                }
            });

        }
        super.onResume();
    }



    // The interface method for OnRequestPermissionCallback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Nothing needs to be done.
    }

}
