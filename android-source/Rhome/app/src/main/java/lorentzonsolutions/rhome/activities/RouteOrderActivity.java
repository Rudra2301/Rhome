package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.googleWebApi.GoogleLocationTypes;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.interfaces.RhomeActivity;
import lorentzonsolutions.rhome.interfaces.RouteCalculator;
import lorentzonsolutions.rhome.routeCalculators.DistanceCalculatorUtil;
import lorentzonsolutions.rhome.routeCalculators.NearestNeighbourRouteCalculator;
import lorentzonsolutions.rhome.utils.SessionStorage;
import lorentzonsolutions.rhome.utils.database.InternalStorage;

public class RouteOrderActivity extends AppCompatActivity implements RhomeActivity {

    private static String TAG = RouteOrderActivity.class.toString();

    private Context context = this;

    private ArrayAdapter<String> routeListAdapter;
    private ListView routeView;
    private Button routeMapButton;
    private Button minorRouteListButton;
    private FloatingActionButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_order);

        activateActivity();
    }

    @Override
    protected void onResume() {
        activateActivity();
        super.onResume();
    }

    /**
     * Method for handling the start of activity. This should be called both from onCreate and onResume.
     */
    private void activateActivity() {
        initRouteCalculation();
        incrementSelectedPlaces();

        assignViews();
        initEvents();
    }

    @Override
    public void initEvents() {
        ArrayList<String> places = new ArrayList<>();
        for(GooglePlace googlePlace : SessionStorage.INSTANCE.getFastestRoute()) {
            places.add(googlePlace.name);
        }
        routeListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);
        routeView.setAdapter(routeListAdapter);

        routeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MinorRoutesMapActivity.class);
                startActivity(intent);
            }
        });

        minorRouteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListMinorRoutesActivity.class);
                startActivity(intent);
            }
        });

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void assignViews() {
        routeView = (ListView) findViewById(R.id.fastest_route);
        routeMapButton = (Button) findViewById(R.id.button_show_route_on_map);
        minorRouteListButton = (Button) findViewById(R.id.button_show_minor_route_list);
        buttonBack = (FloatingActionButton) findViewById(R.id.activity_route_floating_back_button);
    }

    /**
     * Collects the places from SessionStorage and starts background activity to increment each count in DB.
     */
    private void incrementSelectedPlaces() {
        List<GooglePlace> places = SessionStorage.INSTANCE.getSelectedPlacesList();
        List<GoogleLocationTypes> allSelectedTypes = new ArrayList<>();

        for(GooglePlace place : places) {
            for(int i = 0; i < place.types.length; i++) {
                GoogleLocationTypes googleLocationType = GoogleLocationTypes.getGoogleTypeFromString(place.types[i]);
                if(googleLocationType != null) {
                    allSelectedTypes.add(GoogleLocationTypes.getGoogleTypeFromString(place.types[i]));
                }
            }
        }

        new IncrementAllSelectedPlaces().execute(allSelectedTypes);


    }

    /**
     * Method starts the route calculator to calculate the best possible route.
     */
    private void initRouteCalculation() {
        // TODO. Complement with ChanceByBruteForce calculator.
        RouteCalculator calculator = new NearestNeighbourRouteCalculator();
        Location startLocation = SessionStorage.INSTANCE.getSelectedStartLocation();
        Location endLocation = SessionStorage.INSTANCE.getSelectedEndLocation();

        List<GooglePlace> fastestRoute = calculator.calculateFastestRoute(SessionStorage.INSTANCE.getSelectedPlacesList(), startLocation, endLocation);

        SessionStorage.INSTANCE.setFastestRoute(fastestRoute);

        Log.d(TAG, "Calculated fastest route: ");

        for(GooglePlace place : fastestRoute) {
            Log.d(TAG, place.name + " | Distance to start: " + place.distanceToStartLocation);
        }

        double totalDistance = DistanceCalculatorUtil.calculateTotalRouteDistance(fastestRoute);
        Log.d(TAG, "Total distance: " + totalDistance + " km.");
    }

    /**
     * Async task for connection to database to increment count for all selected places types.
     */
    private class IncrementAllSelectedPlaces extends AsyncTask<List<GoogleLocationTypes>, GoogleLocationTypes, Void> {
        InternalStorage storage = new InternalStorage(context);

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Started Async task for incrementing count for selected places in DB.");

        }

        @Override
        protected Void doInBackground(List<GoogleLocationTypes>... params) {
            List<GoogleLocationTypes> selectedTypes = params[0];
            for(GoogleLocationTypes type: selectedTypes) {
                storage.incrementType(type);
                publishProgress(type);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(GoogleLocationTypes... params) {
            GoogleLocationTypes typeDone = params[0];
            Log.d(TAG, "Incremented type: " + typeDone.getAsGoogleType());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "Types of selected places incremented in DB. Count in DB as follows:");
            List<GoogleLocationTypes> dbCountForTypes = storage.getOrderedListOfTypesFromDB();
            for(GoogleLocationTypes type: dbCountForTypes) {
                int count = storage.getCountForType(type);
                Log.d(TAG, type.getAsGoogleType() + " : " + count);
            }

        }
    }
}
