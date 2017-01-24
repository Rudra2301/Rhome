package lorentzonsolutions.rhome.utils;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;

/**
 * Created by johanlorentzon on 2017-01-23.
 */

public class Resources {

    private static Resources instance = new Resources();
    private Resources() {}
    private Context context;
    private GoogleApiClient mGoogleApiClient;

    public static Resources getInstance() {return instance;}

    public void setContext(Context context) {this.context = context;}
    public Context getContext() {return context;}
}
