package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
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
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.interfaces.RouteCalculator;
import lorentzonsolutions.rhome.routeCalculators.NearestNeighbourRouteCalculator;
import lorentzonsolutions.rhome.utils.StorageUtil;

public class RouteOrderActivity extends AppCompatActivity {

    private static String TAG = RouteOrderActivity.class.toString();

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_order);

        initRouteCalculation();

        try {
            ArrayList<String> places = new ArrayList<>();
            for(GooglePlace googlePlace : StorageUtil.INSTANCE.getFastestRoute()) {
                places.add(googlePlace.name);
            }

            ArrayAdapter<String> routeListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, places);

            ListView routeView = (ListView) findViewById(R.id.fastest_route);

            routeView.setAdapter(routeListAdapter);
        } catch (RouteException e) {
            e.printStackTrace();
        }

        // Button for route map activity
        Button routeMapButton = (Button) findViewById(R.id.button_show_route_on_map);
        routeMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MinorRoutesMapActivity.class);
                startActivity(intent);
            }
        });

        // Button for minor route activity
        Button minorRouteListButton = (Button) findViewById(R.id.button_show_minor_route_list);
        minorRouteListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ListMinorRoutesActivity.class);
                startActivity(intent);
            }
        });

        // Button back

        FloatingActionButton buttonBack = (FloatingActionButton) findViewById(R.id.activity_route_floating_back_button);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRouteCalculation() {
        RouteCalculator calculator = new NearestNeighbourRouteCalculator();
        List<GooglePlace> fastestRoute = calculator.calculateFastestRoute(StorageUtil.INSTANCE.getSelectedPlacesList());
        StorageUtil.INSTANCE.setFastestRoute(fastestRoute);

        Log.d(TAG, "Calculated fastest route: ");

        for(GooglePlace place : fastestRoute) {
            Log.d(TAG, place.name + " | Distance to start: " + place.distanceToStartLocation);
        }

        double totalDistance = calculator.calculateTotalRouteDistance(fastestRoute);
        Log.d(TAG, "Total distance: " + totalDistance + " km.");
    }
}
