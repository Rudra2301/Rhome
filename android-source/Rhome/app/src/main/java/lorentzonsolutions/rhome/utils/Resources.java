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
    private final String API_KEY_WEB_API = "AIzaSyDA5A7MQiJOWkWPbk2QIK78V0svBK7buW8";


    public static Resources getInstance() {return instance;}

    public void setContext(Context context) {this.context = context;}
    public Context getContext() {return context;}
    public String getAPI_KEY_WEB_API() {return API_KEY_WEB_API;}
}
