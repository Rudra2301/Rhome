package lorentzonsolutions.rhome;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.customViews.HeaderTextView;
import lorentzonsolutions.rhome.shared.PlaceInformation;
import lorentzonsolutions.rhome.utils.RouteCalculator;
import lorentzonsolutions.rhome.utils.StorageUtil;
import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.URLIconDownloader;

public class MainActivity extends AppCompatActivity {

    private Context thisContext = this;

    // Helper objects
    private StorageUtil storageUtil = StorageUtil.INSTANCE;

    private ArrayAdapter<PlaceInformation> selectedListAdapter;
    ListView selectedPlacesList;
    private HashMap<PlaceInformation, WeakReference<ImageView>> placeIconMap = new HashMap<>();
    private final String TAG = "MAIN_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Hiding action bar on top
        getSupportActionBar().hide();

        setContentView(R.layout.activity_main);
        Resources.getInstance().setContext(this);

        Button selectPlacesButton = (Button) findViewById(R.id.select_places);

        selectPlacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storageUtil.getSelectedStartLocation() != null && storageUtil.getSelectedEndLocation() != null) {
                    Intent intent = new Intent(Resources.getInstance().getContext(), ListLocationTypeSelectionActivity.class);
                    startActivity(intent);
                }
                else if(storageUtil.getSelectedStartLocation() == null) {
                    Toast.makeText(Resources.getInstance().getContext(), "You must select a start location.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Resources.getInstance().getContext(), "You must select an end location.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Selected places
        selectedPlacesList = (ListView) findViewById(R.id.selected_places_list);
        selectedListAdapter = new SelectedPlaceListAdapter(this, storageUtil.getSelectedPlacesList());
        selectedPlacesList.setAdapter(selectedListAdapter);

        Button selectStartButton = (Button) findViewById(R.id.start_location_button);
        selectStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisContext, StartLocationActivity.class);
                startActivity(intent);
            }
        });

        Button selectEndButton = (Button) findViewById(R.id.end_location_button);
        selectEndButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(thisContext, EndLocationActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    protected void onResume() {
        Address startAddress = storageUtil.getSelectedStartAddress();
        if(startAddress != null) {

            //Change the text on the select start location button.
            Button selectStart = (Button) findViewById(R.id.start_location_button);
            selectStart.setText(R.string.start_location_is_set);

            // Showing the info for selected start location
            TextView infoHeader = (TextView) findViewById(R.id.start_location_info_header);
            infoHeader.setVisibility(View.VISIBLE);

            TextView infoText = (TextView) findViewById(R.id.start_location_info_text);
            infoText.setVisibility(View.VISIBLE);

            infoText.setText(startAddress.getAddressLine(0));

            selectedListAdapter.notifyDataSetChanged();
        }
        Address endAddress = storageUtil.getSelectedEndAddress();

        if(endAddress != null) {

            //Change the text on the select start location button.
            Button selectStart = (Button) findViewById(R.id.end_location_button);
            selectStart.setText(R.string.end_location_is_set);

            // Showing the info for selected start location
            TextView infoHeader = (TextView) findViewById(R.id.end_location_info_header);
            infoHeader.setVisibility(View.VISIBLE);

            TextView infoText = (TextView) findViewById(R.id.end_location_info_text);
            infoText.setVisibility(View.VISIBLE);

            infoText.setText(endAddress.getAddressLine(0));

            selectedListAdapter.notifyDataSetChanged();
        }

        if(storageUtil.getSelectedPlacesList() != null && storageUtil.getSelectedPlacesList().size() != 0) {
            Button calculateFastestTime = (Button) findViewById(R.id.fastest_time);
            calculateFastestTime.setVisibility(View.VISIBLE);
            findViewById(R.id.selected_places_header).setVisibility(View.VISIBLE);

            calculateFastestTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RouteCalculator calculator = new RouteCalculator();
                    List<PlaceInformation> fastestRoute = calculator.calculateFastestTime(storageUtil.getSelectedPlacesList(), true);
                    Log.d(TAG, "Calculated fastest route: ");
                    for(PlaceInformation place : fastestRoute) {
                        Log.d(TAG, place.name + " | Distance to start: " + place.distanceToStartLocation);
                    }

                    double totalDistance = calculator.calculateTotalRouteDistance(fastestRoute);
                    Log.d(TAG, "Total distance: " + totalDistance + " km.");
                }
            });

        }
        super.onResume();
    }

    // Adapter for list
    class SelectedPlaceListAdapter extends ArrayAdapter<PlaceInformation> {

        public SelectedPlaceListAdapter(Context context, List<PlaceInformation> placesList) {
            super(context, 0, placesList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.place_selected_list_item, parent, false);

            PlaceInformation place = getItem(position);

            TextView selectedPlaceName = (TextView) convertView.findViewById(R.id.selected_place_name);
            TextView selectedPlaceAddress = (TextView) convertView.findViewById(R.id.selected_place_address);
            ImageView iconView = (ImageView) convertView.findViewById(R.id.place_icon);

            selectedPlaceName.setText(place.name);
            selectedPlaceAddress.setText(place.address);

            // Running thread to collect icon
            new SetPlaceIcon(place, iconView).execute();

            return convertView;
        }
    }

    // Async task for fetching the icon image
    class SetPlaceIcon extends AsyncTask<Void, Void, Void> {

        private final WeakReference<ImageView> iconView;
        private PlaceInformation place;
        private Drawable icon;


        public SetPlaceIcon(PlaceInformation place, ImageView iconView) {
            this.iconView = new WeakReference<ImageView>(iconView);
            this.place = place;
        }

        @Override
        protected Void doInBackground(Void... params) {
            icon = URLIconDownloader.loadImageFromUrl(place.iconAddress);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            if(iconView != null && icon != null) iconView.get().setImageDrawable(icon);
        }
    }

}
