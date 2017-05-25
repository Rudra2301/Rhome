package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.database.InternalStorage;

public class ListLocationTypeSelectionActivity extends AppCompatActivity {

    Context context = this;

    private final String TAG = ListLocationTypeSelectionActivity.class.toString();

    private ListView locationTypeList;
    private ArrayAdapter<GoogleLocationTypes> listAdapter;
    private boolean favouritesShowing = false;
    private List<GoogleLocationTypes> googleLocationTypes;

    Button toggleFavourites;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_location_type_selection);

        googleLocationTypes = new ArrayList<>();
        for(GoogleLocationTypes type: GoogleLocationTypes.values()) googleLocationTypes.add(type);

        // Creating adapter
        listAdapter = new LocationTypeListAdapter(this, googleLocationTypes);

        // Setting adapter to list view
        locationTypeList = (ListView) findViewById(R.id.list_of_location_types);
        locationTypeList.setAdapter(listAdapter);

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

        FloatingActionButton backButton = (FloatingActionButton) findViewById(R.id.activity_location_type_floating_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Favourites logic
        toggleFavourites = (Button) findViewById(R.id.toggle_favourites_button);
        toggleFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(favouritesShowing) {
                    favouritesShowing = false;
                    toggleFavourites.setText(R.string.toggle_favourites_button_showing_all);
                    listShowsAll();
                }
                else {
                    listShowsFavourites();
                    toggleFavourites.setText(R.string.toggle_favourites_button_showing_favourites);
                    favouritesShowing = true;
                }
            }
        });

    }

    private void listShowsAll() {
        googleLocationTypes.clear();
        Collections.addAll(googleLocationTypes, GoogleLocationTypes.values());
        listAdapter.notifyDataSetChanged();
    }

    private void listShowsFavourites() {
        googleLocationTypes.clear();
        new FetchMostVisited().execute();
    }

    // Adapter for list
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




}
