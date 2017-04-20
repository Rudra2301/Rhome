package lorentzonsolutions.rhome.activities;

import android.content.Intent;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.StringBuilderPrinter;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import lorentzonsolutions.rhome.EndLocationActivity;
import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.StartLocationActivity;
import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.StorageUtil;

public class StepOneActivity extends AppCompatActivity {

    StorageUtil storage = StorageUtil.INSTANCE;
    Button setStart;
    TextView startLocationHeaderText;
    TextView startLocationInfoText;

    Button setEnd;
    TextView endLocationHeaderText;
    TextView endLocationInfoText;

    Button doneButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_one);

        initComponents();
        startButtonFunctionality();
        startLocationInformationText();
        endButtonFunctionality();
        endLocationInformationText();
        doneButtonFunctionality();
    }

    @Override
    protected void onResume() {
        startButtonFunctionality();
        startLocationInformationText();
        endButtonFunctionality();
        endLocationInformationText();
        doneButtonFunctionality();

        super.onResume();
    }

    private void initComponents() {
        doneButton = (Button) findViewById(R.id.step_one_done_button);
        startLocationHeaderText = (TextView) findViewById(R.id.step_one_start_location_header);
        startLocationInfoText = (TextView) findViewById(R.id.step_one_start_location_information);
        setEnd = (Button) findViewById(R.id.step_one_select_end);
        setStart = (Button) findViewById(R.id.step_one_select_start);
        endLocationHeaderText = (TextView) findViewById(R.id.step_one_end_location_header);
        endLocationInfoText = (TextView) findViewById(R.id.step_one_end_location_information);
    }

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
        setEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resources.getInstance().getContext(), EndLocationActivity.class);
                startActivity(intent);
            }
        });
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
        setStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resources.getInstance().getContext(), StartLocationActivity.class);
                startActivity(intent);
            }
        });
    }
}
