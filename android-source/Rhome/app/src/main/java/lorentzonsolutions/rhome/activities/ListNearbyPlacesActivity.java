package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.googleWebApi.GoogleWebApiUtil;
import lorentzonsolutions.rhome.interfaces.RhomeActivity;
import lorentzonsolutions.rhome.interfaces.WebApiUtil;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.utils.SessionStorage;

/**
 * Activity class for listing nearby places.
 *
 * @author Johan Lorentzon
 *
 */
public class ListNearbyPlacesActivity extends AppCompatActivity implements RhomeActivity{

    private final String TAG = ListNearbyPlacesActivity.class.toString();

    private WebApiUtil webApiUtil = new GoogleWebApiUtil();

    private boolean isListUpdating = false;
    private int searchRadius = 5000;

    private String selectedType;

    private ListView nearbyPlacesList;
    private ArrayAdapter<GooglePlace> listAdapter;
    private List<GooglePlace> places = new ArrayList<>();

    private SessionStorage sessionStorage = SessionStorage.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_nearby_places);

        // Getting the value from the intent invoking this. Value collected is the type to search for.
        Intent intent = getIntent();
        selectedType = intent.getStringExtra("selected_type");
        Log.d(TAG, "Searching for location of type: " + selectedType);

        assignViews();
        initEvents();

        // Initializing async task to populate list.
        new NearbyLocationCollector().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        // Check if the view firing the event is the list of places
        if(v.getId() == R.id.nearby_locations) {
            ListView theList = (ListView) v;
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
            // Get the selected place
            // TODO. Check if the nearByPlacesList can be used
            GooglePlace clickedPlace = (GooglePlace) theList.getAdapter().getItem(info.position);
            menu.setHeaderTitle(clickedPlace.name);
            String[] menuItems = getResources().getStringArray(R.array.places_context_menu);
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
        // 0 -> Show on map. 1 -> Add to never show list
        GooglePlace placeClicked = (GooglePlace) nearbyPlacesList.getAdapter().getItem(info.position);

        if(select == 0) {
            // Show map
            Intent intent = new Intent(getApplicationContext(), ShowOnMap.class);
            intent.putExtra("place_to_show", placeClicked);
            startActivity(intent);
        }
        else if(select == 1) {
            sessionStorage.addToNeverShowList(placeClicked);
            // TODO. Dont show in list, update.
        }

        return true;
    }

    @Override
    public void initEvents() {
        listAdapter = new PlaceListAdapter(getApplicationContext(), places);

        nearbyPlacesList.setAdapter(listAdapter);


        nearbyPlacesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(!isListUpdating) {
                    GooglePlace place = (GooglePlace) parent.getItemAtPosition(position);
                    // Check if the place already exists in the selected place list. If so, remove it.
                    if(sessionStorage.getSelectedPlacesList().contains(place)) {
                        sessionStorage.removeSelectedPlace(place);
                        Log.d(TAG, "Place removed from selected.");
                        makeSnackBar("Place removed.");
                    }
                    // If not, add it.
                    else {
                        sessionStorage.addSelectedPlace(place);
                        Log.d(TAG, "Place added to selected.");
                        makeSnackBar("Place added.");
                    }

                    // Notify adapter that data has changed.
                    listAdapter.notifyDataSetChanged();
                }
                else {
                    makeSnackBar("List updating. Please wait.");
                }
            }
        });

        // Context menu for items in list
        registerForContextMenu(nearbyPlacesList);

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.list_nearby_locations_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public void assignViews() {
        nearbyPlacesList = (ListView) findViewById(R.id.nearby_locations);


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


    // INNER ASYNC TASK FOR ACCESSING NETWORK AND RETRIEVE DATA

    /*
    Async task <first, second , third> list explanation.
    First   -> for use in doInBackGround(first... firsts). This method returns the value to the onPostExecute. As: return third;
                Use publishProgress(second) to call the onProgressUpdate(). Send the value specified as second.
    Second  -> for use in onProgressUpdate(second... seconds)
    Third   -> for use in onPostExecute(third third). The return value from the doInBackground
     */
    private class NearbyLocationCollector extends AsyncTask<Void, GooglePlace, Void> {

        TextView progressText = (TextView) findViewById(R.id.getting_locations_progress_text);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.getting_locations_progress);

        @Override
        protected void onPreExecute() {
            isListUpdating = true;
            progressText.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            makeSnackBar("Loading locations.");
        }

        @Override
        protected Void doInBackground(Void... params) {

            double searchPointLatitude = sessionStorage.getSelectedStartLocation().getLatitude();
            double searchPointLongitude = sessionStorage.getSelectedStartLocation().getLongitude();

            // Fetching data.
            List<GooglePlace> googlePlaceList = webApiUtil.getNearbyLocationsList(
                    searchPointLatitude, searchPointLongitude, searchRadius, selectedType);

            Collections.sort(googlePlaceList);

            for(GooglePlace place : googlePlaceList) publishProgress(place);
            return null;
        }

        @Override
        protected void onProgressUpdate(GooglePlace... progress) {
            Log.i(TAG, "Nearby location added.");
            listAdapter.add(progress[0]);
            listAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.i("AsyncTask", "Data collected.");

            String message = "Showing " + listAdapter.getCount() + " places nearby.";
            makeSnackBar(message);

            isListUpdating = false;
            progressText.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
        }
    }



    // Adapter for list
    class PlaceListAdapter extends ArrayAdapter<GooglePlace> {

        public PlaceListAdapter(Context context, List<GooglePlace> placesList) {
            super(context, 0, placesList);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            // Get the data item for this position
            GooglePlace place = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_place, parent, false);

            // TODO. Fix duration info.
            TextView placeName = (TextView) convertView.findViewById(R.id.place_name);
            TextView placeAddress = (TextView) convertView.findViewById(R.id.place_address);
            TextView placeDistance = (TextView) convertView.findViewById(R.id.place_distance);
            TextView placeDuration = (TextView) convertView.findViewById(R.id.place_duration);
            TextView placeIsOpen = (TextView) convertView.findViewById(R.id.place_isopen);
            LinearLayout placeItem = (LinearLayout) convertView.findViewById(R.id.place_item);

            // Populate the data into the template view using the data object
            assert place != null;
            placeName.setText(place.name);
            placeAddress.setText(place.address);

            if(place.isOpen) {
                placeIsOpen.setText(R.string.open);
                placeIsOpen.setTextColor(Color.GREEN);
            }
            else {
                placeIsOpen.setText(R.string.closed);
                placeIsOpen.setTextColor(Color.RED);
            }

            int distanceInKm = place.distanceToStartLocation/1000;
            placeDistance.setText("Distance " + distanceInKm + " km");

            int timeInMinutes = place.minutesByCarToStartLocation /60;
            placeDuration.setText("Duration by car " + timeInMinutes + " min");

            if(sessionStorage.getSelectedPlacesList().contains(place)) {

                placeItem.setBackgroundColor(getResources().getColor(R.color.accent_material_light_1));

                Log.i(TAG, "Found place in selected list: " + place.name);
                TextView selected = (TextView) convertView.findViewById(R.id.place_selected_text);
                selected.setVisibility(View.VISIBLE);
            }
            return convertView;
        }

    }


}
