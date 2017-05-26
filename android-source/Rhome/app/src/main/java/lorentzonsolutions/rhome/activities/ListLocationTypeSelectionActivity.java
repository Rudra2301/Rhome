package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.googleWebApi.GoogleLocationTypes;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.interfaces.RhomeActivity;
import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.database.InternalStorage;

/**
 * Activity to show summary of places to visit.
 *
 * @author Johan Lorentzon
 *
 */
public class ListLocationTypeSelectionActivity extends AppCompatActivity implements RhomeActivity {

    Context context = this;

    private final String TAG = ListLocationTypeSelectionActivity.class.toString();

    private ArrayAdapter<GoogleLocationTypes> listAdapter;
    private boolean favouritesShowing = false;
    private List<GoogleLocationTypes> googleLocationTypes = new ArrayList<>();

    // Views.
    private FloatingActionButton backButton;
    private Button toggleFavourites;
    private ListView locationTypeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_location_type_selection);

        assignViews();
        initEvents();
        updateList();
    }

    @Override
    protected void onResume() {
        assignViews();
        initEvents();
        favouritesShowing = false;
        updateList();
        super.onResume();
    }

    @Override
    public void assignViews() {
        locationTypeList = (ListView) findViewById(R.id.list_of_location_types);
        backButton = (FloatingActionButton) findViewById(R.id.activity_location_type_floating_back_button);
        toggleFavourites = (Button) findViewById(R.id.toggle_favourites_button);
    }

    @Override
    public void initEvents() {
        // On click listener for list items.
        locationTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the location type selected
                GoogleLocationTypes type = (GoogleLocationTypes) parent.getItemAtPosition(position);
                Log.d(TAG, "Type selected: " + type.getAsReadable());

                // Instantiate a new intent and pass the selected type value to this. Start activity.
                Intent intent = new Intent(Resources.getInstance().getContext(), ListNearbyPlacesActivity.class);
                intent.putExtra("selected_type", type.getAsGoogleType());
                startActivity(intent);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toggleFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateList();
            }
        });

        listAdapter = new LocationTypeListAdapter(this, googleLocationTypes);
        locationTypeList.setAdapter(listAdapter);

        registerForContextMenu(locationTypeList);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        // Check if the view firing the event is the list of places
        if(view.getId() == R.id.list_of_location_types) {
            menu.setHeaderTitle(R.string.select_places_context_menu_header);
            String[] menuItems = getResources().getStringArray(R.array.list_location_type_context_menu);

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

        GoogleLocationTypes type = (GoogleLocationTypes) locationTypeList.getAdapter().getItem(info.position);

        // Option 1 - Remove. 2 - Back.
        if(select == 0) {
            // Navigate from current
            new ResetMostVisitedCount().execute(type);
        }
        else if(select == 1) {}

        return true;
    }

    /**
     * Updates the list to show all types.
     */
    private void listShowsAll() {
        googleLocationTypes.clear();
        Collections.addAll(googleLocationTypes, GoogleLocationTypes.values());
        listAdapter.notifyDataSetChanged();
        favouritesShowing = false;
        toggleFavourites.setText(R.string.toggle_favourites_button_show_favourites);
        makeSnackBar("Showing all types!");
    }

    /**
     * Updates the list to show favourites from database.
     */
    private void listShowsFavourites() {
        googleLocationTypes.clear();
        favouritesShowing = true;
        Log.d(TAG, "FAVOURITES SHOWING!");
        toggleFavourites.setText(R.string.toggle_favourites_button_show_all);
        new FetchMostVisited().execute();
        makeSnackBar("Showing your most picked types!");
    }

    /**
     * Updating the list. Either showing all types or favourites depending on what the user has choosen.
     */
    private void updateList() {

        if(favouritesShowing) {
            listShowsAll();
        }
        else {
            listShowsFavourites();
        }
    }

    /**
     * Inner list adapter class for location type list.
     */
    private class LocationTypeListAdapter extends ArrayAdapter<GoogleLocationTypes> {

        LocationTypeListAdapter(Context context, List<GoogleLocationTypes> placesList) {
            super(context, 0, placesList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.list_item_location_type, parent, false);

            // Set a customized layout on the list items.
            GoogleLocationTypes type = getItem(position);
            TextView typeName = (TextView) convertView.findViewById(R.id.place_type);
            typeName.setText(type.getAsReadable());
            return convertView;
        }
    }

    /**
     * Async inner class for collecting most visited places from database and populate list.
     */
    private class FetchMostVisited extends AsyncTask<Void, GoogleLocationTypes, Void> {
        InternalStorage storage = new InternalStorage(context);

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Started Async task for getting favourites from DB.");
        }

        @Override
        protected Void doInBackground(Void... params) {
            List<GoogleLocationTypes> favourites = storage.getOrderedListOfTypesFromDB();
            for(GoogleLocationTypes type: favourites) {
                googleLocationTypes.add(type);
                publishProgress(type);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(GoogleLocationTypes... params) {
            GoogleLocationTypes typeDone = params[0];
            Log.d(TAG, "Added most visited to list: " + typeDone.getAsGoogleType());
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(TAG, "List updated with most visited.");
            listAdapter.notifyDataSetChanged();

        }
    }

    /**
     * Async inner class for resetting count for a type in database.
     */
    private class ResetMostVisitedCount extends AsyncTask<GoogleLocationTypes, Void, GoogleLocationTypes> {
        InternalStorage storage = new InternalStorage(context);

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "Started Async task for resetting count in DB.");
        }

        @Override
        protected GoogleLocationTypes doInBackground(GoogleLocationTypes... params) {
            GoogleLocationTypes type = params[0];
            storage.resetType(type);
            return type;
        }

        @Override
        protected void onPostExecute(GoogleLocationTypes type) {
            GoogleLocationTypes resetType = type;
            Log.d(TAG, "Count reset for " + resetType);
            favouritesShowing = !favouritesShowing;
            updateList();
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
