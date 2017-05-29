package lorentzonsolutions.rhome;

import android.location.Location;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import lorentzonsolutions.rhome.utils.LocationConverter;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;



@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class LocationConverterTest {

    @Test
    public void shouldConvertPlaceToLocationWithSameValues() {
        Place toConvert = Mockito.mock(Place.class);
        LatLng latLng = new LatLng(57.341263, 12.054321);
        doReturn(latLng).when(toConvert).getLatLng();

        Location converted = LocationConverter.INSTANCE.placeToLocation(toConvert);
        assertTrue(latLng.latitude == converted.getLatitude());
        assertTrue(latLng.longitude == converted.getLongitude());
    }

    @Test
    public void shouldConvertLatLngToLocationWithSameValues() {
        LatLng toConvert = new LatLng(57.341263, 12.054321);

        Location converted = LocationConverter.INSTANCE.latLngToLocation(toConvert);

        assertTrue(toConvert.latitude == converted.getLatitude());
        assertTrue(toConvert.longitude == converted.getLongitude());
    }

    @Test
    public void shouldConvertLocationToLatLngWithSameValues() {
        Location toConvert = new Location("");
        toConvert.setLatitude(57.341263); toConvert.setLongitude(12.054321);

        LatLng converted = LocationConverter.INSTANCE.locationToLatLng(toConvert);

        assertTrue(toConvert.getLatitude() == converted.latitude);
        assertTrue(toConvert.getLongitude() == converted.longitude);
    }
}
