package lorentzonsolutions.rhome.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.interfaces.RhomeActivity;
import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.SessionStorage;

/**
 * Activity class for activity pick start and end locations.
 *
 * @author Johan Lorentzon
 *
 */
public class PickStartAndEndActivity extends AppCompatActivity implements RhomeActivity {

    private final String TAG = PickStartAndEndActivity.class.toString();
    private final Context context = this;

    private SessionStorage storage = SessionStorage.INSTANCE;
    private Button setStart;
    private TextView startLocationHeaderText;
    private TextView startLocationInfoText;

    private Button setEnd;
    private TextView endLocationHeaderText;
    private TextView endLocationInfoText;

    private Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_start_and_end);

        assignViews();
        initEvents();
        startButtonFunctionality();
        startLocationInformationText();
        endButtonFunctionality();
        endLocationInformationText();
        doneButtonFunctionality();
    }

    @Override
    protected void onResume() {
        assignViews();
        initEvents();
        startButtonFunctionality();
        startLocationInformationText();
        endButtonFunctionality();
        endLocationInformationText();
        doneButtonFunctionality();

        super.onResume();
    }

    @Override
    public void initEvents() {
        setStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StartLocationActivity.class);
                startActivity(intent);
            }
        });

        setEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EndLocationActivity.class);
                startActivity(intent);
            }
        });

        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PickPlacesToVisitActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void assignViews() {
        doneButton = (Button) findViewById(R.id.step_one_done_button);
        startLocationHeaderText = (TextView) findViewById(R.id.step_one_start_location_header);
        startLocationInfoText = (TextView) findViewById(R.id.step_one_start_location_information);
        setEnd = (Button) findViewById(R.id.step_one_select_end);
        setStart = (Button) findViewById(R.id.step_one_select_start);
        endLocationHeaderText = (TextView) findViewById(R.id.step_one_end_location_header);
        endLocationInfoText = (TextView) findViewById(R.id.step_one_end_location_information);
    }

    /**
     * Handles the activation of the done button.
     */
    private void doneButtonFunctionality() {
        // Check if locations have been picked
        if(storage.getSelectedStartLocation() != null && storage.getSelectedEndLocation() != null) {
            doneButton.setEnabled(true);
        }
    }

    private void startLocationInformationText() {

        if(storage.getSelectedStartLocation() != null) {
            startLocationHeaderText.setVisibility(View.VISIBLE);
            Address start = storage.getSelectedStartAddress();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < start.getMaxAddressLineIndex(); i++) {
                sb.append(start.getAddressLine(i) + "\n");
            }
            startLocationInfoText.setText(sb.toString());
            startLocationInfoText.setVisibility(View.VISIBLE);
        }
        else {
            startLocationInfoText.setText(R.string.step_one_no_location_choosen);
        }

        startLocationHeaderText.setVisibility(View.VISIBLE);
        startLocationInfoText.setVisibility(View.VISIBLE);
    }

    private void endButtonFunctionality() {


        if(storage.getSelectedStartLocation() == null) {
            setEnd.setEnabled(false);
        } else setEnd.setEnabled(true);

        if(storage.getSelectedEndLocation() == null) setEnd.setText(R.string.step_one_set_end_location);
        else setEnd.setText(R.string.step_one_reset_end_location);
    }

    private void endLocationInformationText() {

        if(storage.getSelectedEndLocation() != null) {
            Address end = storage.getSelectedEndAddress();
            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < end.getMaxAddressLineIndex(); i++) {
                sb.append(end.getAddressLine(i) + "\n");
            }
            endLocationInfoText.setText(sb.toString());
        }
        else {
            endLocationInfoText.setText(R.string.step_one_no_location_choosen);
        }
        if(storage.getSelectedStartLocation() == null) {
            endLocationHeaderText.setVisibility(View.INVISIBLE);
            endLocationInfoText.setVisibility(View.INVISIBLE);
        } else {
            endLocationHeaderText.setVisibility(View.VISIBLE);
            endLocationInfoText.setVisibility(View.VISIBLE);
        }
    }

    private void startButtonFunctionality() {
        setStart.setEnabled(true);
        if(storage.getSelectedStartLocation() == null) setStart.setText(R.string.step_one_set_start_location);
        else setStart.setText(R.string.step_one_reset_start_location);
    }
}
