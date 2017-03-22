package lorentzonsolutions.rhome;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.shared.PlaceInformation;
import lorentzonsolutions.rhome.utils.StorageUtil;

public class RouteActivity extends AppCompatActivity {

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        try {
            ArrayList<String> places = new ArrayList<>();
            for(PlaceInformation placeInformation : StorageUtil.INSTANCE.getFastestRoute()) {
                places.add(placeInformation.name);
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
                Intent intent = new Intent(context, RouteMapActivity.class);
                startActivity(intent);
            }
        });
    }
}
