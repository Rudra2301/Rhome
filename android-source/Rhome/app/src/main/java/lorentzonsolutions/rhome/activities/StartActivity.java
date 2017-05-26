package lorentzonsolutions.rhome.activities;

import android.Manifest;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import lorentzonsolutions.rhome.R;
import lorentzonsolutions.rhome.interfaces.RhomeActivity;
import lorentzonsolutions.rhome.utils.Resources;

public class StartActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, RhomeActivity {

    private final String TAG = StartActivity.class.toString();
    private static final int LOCATION_PERMISSIONS = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Resources.getInstance().setContext(this);

        Button goButton = (Button) findViewById(R.id.go_button);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Resources.getInstance().getContext(), PickStartAndEndActivity.class);
                startActivity(intent);
            }
        });

        requestPermissions();
    }

    /**
     * Requests the permissions to access device location information.
     */
    private void requestPermissions() {

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                LOCATION_PERMISSIONS);

    }

    // The interface method for OnRequestPermissionCallback
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Nothing needs to be done.
    }

    @Override
    public void initEvents() {

    }

    @Override
    public void assignViews() {

    }
}
