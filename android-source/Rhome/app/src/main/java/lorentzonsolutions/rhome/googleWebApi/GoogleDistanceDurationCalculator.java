package lorentzonsolutions.rhome.googleWebApi;

import android.util.Log;

import lorentzonsolutions.rhome.utils.Resources;
import lorentzonsolutions.rhome.utils.UrlDataReceiver;

/**
 * Class for calculation distance between locations.
 */

public class GoogleDistanceDurationCalculator {
    private final String TAG = GoogleDistanceDurationCalculator.class.toString();

    private UrlDataReceiver urlDataReceiver = new UrlDataReceiver();

    public String calculateDistance(double fromLatitude, double fromLongitude, double toLatidtude, double toLongitude, GoogleDistanceModes mode) {

        return urlDataReceiver.readURL(GoogleWebApiUtil
                .buildDistanceMatrixUrl(fromLatitude, fromLongitude, toLatidtude, toLongitude, mode));
    }
}
