package lorentzonsolutions.rhome;

import android.content.Intent;
import android.location.Address;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lorentzonsolutions.rhome.utils.StorageUtil;
import lorentzonsolutions.rhome.utils.Resources;

public class MainActivity extends AppCompatActivity {

    // Helper objects
    private StorageUtil storageUtil = StorageUtil.INSTANCE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO. Initialize the Google API connection!

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resources.getInstance().setContext(this);
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

        }
        super.onResume();
    }

    // BUTTON FUNCTIONS ----------------------------------------------------------------------------
    public void startMap(View view) {
        Intent intent = new Intent(this, StartLocationActivity.class);
        startActivity(intent);
    }


}
