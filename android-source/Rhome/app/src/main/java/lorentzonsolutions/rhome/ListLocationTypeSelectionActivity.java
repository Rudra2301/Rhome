package lorentzonsolutions.rhome;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.shared.GoogleLocationTypes;
import lorentzonsolutions.rhome.utils.Resources;

public class ListLocationTypeSelectionActivity extends AppCompatActivity {

    private final String TAG = "LIST_LOCATION_TYPE";
    private final Context thisContext = this;

    private ListView locationTypeList;
    private ArrayAdapter<GoogleLocationTypes> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_location_type_selection);

        List<GoogleLocationTypes> googleLocationTypes = new ArrayList<>();
        for(GoogleLocationTypes type: GoogleLocationTypes.values()) googleLocationTypes.add(type);

        // Creating adapter
        listAdapter = new LocationTypeListAdapter(this, googleLocationTypes);


        // Setting adapter to list view
        locationTypeList = (ListView) findViewById(R.id.list_of_location_types);
        locationTypeList.setAdapter(listAdapter);

        // Setting onClick listener
        locationTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the location type selected
                GoogleLocationTypes type = (GoogleLocationTypes) parent.getItemAtPosition(position);
                Log.d(TAG, "Type selected: " + type.getAsReadable());

                Intent intent = new Intent(Resources.getInstance().getContext(), ListNearbyPlacesOfTypeActivity.class);
                intent.putExtra("selected_type", type.getAsGoogleType());
                startActivity(intent);
            }
        });


    }

    // Adapter for list
    class LocationTypeListAdapter extends ArrayAdapter<GoogleLocationTypes> {

        public LocationTypeListAdapter(Context context, List<GoogleLocationTypes> placesList) {
            super(context, 0, placesList);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) convertView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.location_type_list_item, parent, false);

            GoogleLocationTypes type = getItem(position);
            TextView typeName = (TextView) convertView.findViewById(R.id.place_type);
            typeName.setText(type.getAsReadable());
            return convertView;
        }
    }




}
