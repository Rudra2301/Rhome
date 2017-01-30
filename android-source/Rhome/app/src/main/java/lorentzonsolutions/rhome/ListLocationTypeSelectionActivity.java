package lorentzonsolutions.rhome;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.shared.LocationTypes;
import lorentzonsolutions.rhome.utils.Resources;

public class ListLocationTypeSelectionActivity extends AppCompatActivity {

    private final String TAG = "LIST_LOCATION_TYPE";
    private final Context thisContext = this;

    private ListView locationTypeList;
    private ArrayAdapter<LocationTypes> listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_location_type_selection);

        // Setting the header
        getSupportActionBar().setTitle("Choose location type");

        List<LocationTypes> locationTypes = new ArrayList<>();
        for(LocationTypes type: LocationTypes.values()) locationTypes.add(type);

        // Creating adapter
        listAdapter = new ArrayAdapter<>(
                Resources.getInstance().getContext(),
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                locationTypes);


        // Setting adapter to list view
        locationTypeList = (ListView) findViewById(R.id.list_of_location_types);
        locationTypeList.setAdapter(listAdapter);

        // Setting onClick listener
        locationTypeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the location type selected
                LocationTypes type = (LocationTypes) parent.getItemAtPosition(position);
                Log.d(TAG, "Type selected: " + type.getAsReadable());

                Intent intent = new Intent(Resources.getInstance().getContext(), ListNearbyPlacesOfTypeActivity.class);
                intent.putExtra("selected_type", type.getAsGoogleType());
                startActivity(intent);
            }
        });


    }




}
