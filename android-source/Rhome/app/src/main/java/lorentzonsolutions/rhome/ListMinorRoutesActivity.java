package lorentzonsolutions.rhome;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import lorentzonsolutions.rhome.exceptions.RouteException;
import lorentzonsolutions.rhome.shared.GoogleLocationTypes;
import lorentzonsolutions.rhome.shared.GooglePlaceInformation;
import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.StorageUtil;

public class ListMinorRoutesActivity extends AppCompatActivity {

    private final String TAG = ListMinorRoutesActivity.class.toString();

    ArrayAdapter<List<GooglePlaceInformation>> adapter;
    ListView minorRouteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_minor_routes);

        // Setting the titlebar
        if(getSupportActionBar() != null) getSupportActionBar().setTitle("Your minor routes");

        // Creating adapter
        try {
            adapter = new MinorRoutesListAdapter(this, StorageUtil.INSTANCE.splitToMinorRoutes(StorageUtil.INSTANCE.getFastestRoute()));

            minorRouteList = (ListView) findViewById(R.id.list_of_minor_routes);
            minorRouteList.setAdapter(adapter);

            // Setting onClick listener
            minorRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // Get the location type selected
                    List<GooglePlaceInformation> minorRoute = (List<GooglePlaceInformation>) parent.getItemAtPosition(position);
                    Log.d(TAG, "Minor route selected: " + minorRoute);

                    startNavigation(minorRoute, false);
                }
            });

            registerForContextMenu(minorRouteList);

        } catch (RouteException re) {
            Log.d(TAG, "No fastest route detected, could not create list.");
        }

    }


    // Adapter for list
    class MinorRoutesListAdapter extends ArrayAdapter<List<GooglePlaceInformation>> {

        public MinorRoutesListAdapter(Context context, List<List<GooglePlaceInformation>> placesList) {
            super(context, 0, placesList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_item_minor_routes, parent, false);

            List<GooglePlaceInformation> minorRoute = getItem(position);
            GooglePlaceInformation from = minorRoute.get(0);
            GooglePlaceInformation to = minorRoute.get(1);

            TextView fromName = (TextView) convertView.findViewById(R.id.minor_route_place_from);
            TextView toName = (TextView) convertView.findViewById(R.id.minor_route_place_to);
            fromName.setText(from.toString());
            toName.setText(to.toString());

            return convertView;
        }
    }

    // ContextMenu for longclick
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Check if the view firing the event is the list of places
        if(v.getId() == R.id.list_of_minor_routes) {
            ListView theList = (ListView) v;
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            menu.setHeaderTitle(R.string.minor_route_context_header);
            String[] menuItems = getResources().getStringArray(R.array.minor_route_context);
            // Add the menu items
            for(int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    // Event for catching context menu item selected
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int select = item.getItemId();
        // 0 -> Navigate from current. 1 -> Turn by turn navigation
        List<GooglePlaceInformation> minorRoute = (List<GooglePlaceInformation>) minorRouteList.getAdapter().getItem(info.position);

        if(select == 0) {
            // Navigate from current
            startNavigation(minorRoute, false);
        }
        else if(select == 1) {
            startNavigation(minorRoute, true);
        }

        return true;
    }

    private void startNavigation(List<GooglePlaceInformation> minorRoute, boolean turnByTurn) {
        GooglePlaceInformation fromLocation = minorRoute.get(0);
        GooglePlaceInformation toLocation = minorRoute.get(1);

        String uri = "";

        if(turnByTurn) {
            uri = "http://maps.google.com/maps?" +
                    "saddr="+fromLocation.latitude + "," + fromLocation.longitude +
                    "&daddr="+ toLocation.latitude + "," + toLocation.longitude;

        } else {
            uri = "google.navigation:q=" +
                    toLocation.latitude + "," + toLocation.longitude;
        }

        Intent navIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        navIntent.setPackage("com.google.android.apps.maps");
        startActivity(navIntent);

        if(navIntent.resolveActivity(getPackageManager()) != null) {
            //Start intent
            startActivity(navIntent);
        } else {
            // No apps to handle navigation
            Log.d(TAG, "There are no available apps on device to handle navigation.");
            Toast.makeText(Resources.getInstance()
                            .getContext(), "Could not find any navigation application. Please install one to proceed.",
                    Toast.LENGTH_SHORT).show();

        }
    }

}
