package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import java.util.List;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.interfaces.RhomeActivity;
import lorentzonsolutions.rhome.utils.SessionStorage;

/**
 * Activity class for listing the Minor Routes calculated.
 *
 * @author Johan Lorentzon
 *
 */
public class ListMinorRoutesActivity extends AppCompatActivity implements RhomeActivity {

    private final String TAG = ListMinorRoutesActivity.class.toString();

    private ArrayAdapter<List<GooglePlace>> adapter;
    private ListView minorRouteList;
    private FloatingActionButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_minor_routes);

        assignViews();
        initEvents();
    }

    @Override
    protected void onResume() {

        // TODO. Check if position is near any destination position. If so, ask if minor route is completed.
        assignViews();
        initEvents();
        super.onResume();
    }

    @Override
    public void initEvents() {
        adapter = new MinorRoutesListAdapter(this, SessionStorage.INSTANCE.splitToMinorRoutes(SessionStorage.INSTANCE.getFastestRoute()));
        minorRouteList.setAdapter(adapter);

        minorRouteList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the location type selected
                List<GooglePlace> minorRoute = (List<GooglePlace>) parent.getItemAtPosition(position);
                Log.d(TAG, "Minor route selected: " + minorRoute);

                startNavigation(minorRoute, false);
            }
        });

        registerForContextMenu(minorRouteList);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void assignViews() {
        minorRouteList = (ListView) findViewById(R.id.list_of_minor_routes);
        backButton = (FloatingActionButton) findViewById(R.id.activity_minor_route_list_back_button);
    }

    // Adapter for list
    private class MinorRoutesListAdapter extends ArrayAdapter<List<GooglePlace>> {

        MinorRoutesListAdapter(Context context, List<List<GooglePlace>> placesList) {
            super(context, 0, placesList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_item_minor_routes, parent, false);

            List<GooglePlace> minorRoute = getItem(position);
            GooglePlace from = minorRoute.get(0);
            GooglePlace to = minorRoute.get(1);

            TextView title = (TextView) convertView.findViewById(R.id.minor_route_list_item_header);
            String header = getResources().getString(R.string.minor_item_header);
            title.setText(header + " " + (position + 1));
            TextView fromName = (TextView) convertView.findViewById(R.id.minor_route_place_from);
            TextView toName = (TextView) convertView.findViewById(R.id.minor_route_place_to);
            fromName.setText(from.name);
            toName.setText(to.name);

            TextView fromInfo = (TextView) convertView.findViewById(R.id.minor_route_from_info_text);
            TextView toInfo = (TextView) convertView.findViewById(R.id.minor_route_to_info_text);

            fromInfo.setText(from.address);
            toInfo.setText(to.address);

            return convertView;
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        // Check if the view firing the event is the list of places
        if(view.getId() == R.id.list_of_minor_routes) {
            menu.setHeaderTitle(R.string.minor_route_context_header);
            String[] menuItems = getResources().getStringArray(R.array.minor_route_context);
            // Add the menu items
            for(int i = 0; i < menuItems.length; i++) {
                menu.add(Menu.NONE, i, i, menuItems[i]);
            }
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int select = item.getItemId();
        // 0 -> Navigate from current. 1 -> Turn by turn navigation
        List<GooglePlace> minorRoute = (List<GooglePlace>) minorRouteList.getAdapter().getItem(info.position);

        if(select == 0) {
            // Navigate from current
            startNavigation(minorRoute, false);
        }
        else if(select == 1) {
            startNavigation(minorRoute, true);
        }

        return true;
    }

    /**
     * Class for initializing the activity for navigation. As of now the only valid navigation application to use is google maps.
     *
     * Valid package for navigation is set to com.google.android.apps.maps for the intent.
     *
     * @param minorRoute
     * @param turnByTurn
     */
    private void startNavigation(List<GooglePlace> minorRoute, boolean turnByTurn) {
        GooglePlace fromLocation = minorRoute.get(0);
        GooglePlace toLocation = minorRoute.get(1);

        String uri;

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
            makeSnackBar("Could not find any navigation application. Please install one to proceed.");
         }
    }

    /**
     * Shows a snack bar to the user with given message.
     *
     * @param message
     */
    private void makeSnackBar(String message) {
        Snackbar mySnackbar = Snackbar.make(getWindow().getDecorView(),
                message, Snackbar.LENGTH_LONG);
        mySnackbar.show();
    }

}
