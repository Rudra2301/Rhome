package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import java.util.List;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.StorageUtil;
import lorentzonsolutions.rhome.utils.URLIconDownloader;

public class PickPlacesToVisitActivity extends AppCompatActivity {

    private static String TAG = PickPlacesToVisitActivity.class.toString();

    StorageUtil storage = StorageUtil.INSTANCE;

    Button addPlaceButton;
    Button doneButton;
    FloatingActionButton backButton;
    ListView selectedPlacesList;
    SelectedPlaceListAdapter selectedListAdapter;

    TextView startLocationInfo;
    TextView endLocationInfo;
    TextView placeListHeader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_places_to_visit);

        setViews();
        initListeners();
        setStartAndEncLocationInfo();

        updateList();
        selectedListAdapter.notifyDataSetChanged();

    }

    private void setViews() {
        addPlaceButton = (Button) findViewById(R.id.step_two_add_place_button);
        doneButton = (Button) findViewById(R.id.pick_places_to_visit_done_button);
        backButton = (FloatingActionButton) findViewById(R.id.step_two_floating_back_button);
        startLocationInfo = (TextView) findViewById(R.id.step_two_start_location_info_text);
        endLocationInfo = (TextView) findViewById(R.id.step_two_end_location_info_text);
        selectedPlacesList = (ListView) findViewById(R.id.selected_places_list);
        selectedListAdapter = new SelectedPlaceListAdapter(this, storage.getSelectedPlacesList());
        selectedPlacesList.setAdapter(selectedListAdapter);
        placeListHeader = (TextView) findViewById(R.id.step_two_place_list_header);
    }

    private void setStartAndEncLocationInfo() {

        Address start = storage.getSelectedStartAddress();
        Address end = storage.getSelectedEndAddress();

        StringBuilder startBuilder = new StringBuilder();
        StringBuilder endBuilder = new StringBuilder();

        for(int i = 0; i < start.getMaxAddressLineIndex(); i++) {
            if(i == start.getMaxAddressLineIndex() -1) {
                startBuilder.append(start.getAddressLine(i) );
            }
            else startBuilder.append(start.getAddressLine(i) + ", ");
        }

        for(int i = 0; i < end.getMaxAddressLineIndex(); i++) {
            if(i == end.getMaxAddressLineIndex() -1) {
                endBuilder.append(end.getAddressLine(i) );
            }
            else endBuilder.append(end.getAddressLine(i) + ", ");
        }

        startLocationInfo.setText(startBuilder.toString());
        endLocationInfo.setText(endBuilder.toString());

    }

    @Override
    protected void onResume() {
        setViews();
        selectedListAdapter.notifyDataSetChanged();
        updateList();
        super.onResume();
    }

    private void updateList() {
        if(storage.getSelectedPlacesList() != null && storage.getSelectedPlacesList().size() != 0) {
            doneButton.setVisibility(View.VISIBLE);
            doneButton.setEnabled(true);
            placeListHeader.setVisibility(View.VISIBLE);

            doneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Resources.getInstance().getContext(), RouteOrderActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private void initListeners() {

        addPlaceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(storage.getSelectedStartLocation() != null && storage.getSelectedEndLocation() != null) {
                    Intent intent = new Intent(Resources.getInstance().getContext(), SelectWhatToDoActivity.class);
                    startActivity(intent);
                }
                else if(storage.getSelectedStartLocation() == null) {
                    Toast.makeText(Resources.getInstance().getContext(), "You must select a start location.", Toast.LENGTH_SHORT).show();
                }
                else if(storage.getSelectedEndLocation() == null){
                    Toast.makeText(Resources.getInstance().getContext(), "You must select an end location.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Adapter for list
    class SelectedPlaceListAdapter extends ArrayAdapter<GooglePlace> {

        public SelectedPlaceListAdapter(Context context, List<GooglePlace> placesList) {
            super(context, 0, placesList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_place_selected, parent, false);

            GooglePlace place = getItem(position);

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
        private GooglePlace place;
        private Drawable icon;


        public SetPlaceIcon(GooglePlace place, ImageView iconView) {
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
