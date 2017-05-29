package lorentzonsolutions.rhome;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.utils.SessionStorage;

import static junit.framework.Assert.assertTrue;

/**
 * Test for SessionStorageClass.
 *
 * @author Johan Lorentzon
 *
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SessionStorageTest {

    @Test
    public void shouldSplitAListIntoMinorRoutesOfTwoAndTwo_oddNumberOfPlaces() {
        GooglePlace place1 = new GooglePlace.BuildPlace("PLACE1", 57.704147, 12.012863).build();
        GooglePlace place2 = new GooglePlace.BuildPlace("PLACE2", 57.738067, 12.057152).build();
        GooglePlace place3 = new GooglePlace.BuildPlace("PLACE3", 57.681761, 11.953125).build();
        GooglePlace place4 = new GooglePlace.BuildPlace("PLACE4", 57.668543, 12.057152).build();
        GooglePlace place5 = new GooglePlace.BuildPlace("PLACE5", 57.712951, 11.975441).build();
        GooglePlace place6 = new GooglePlace.BuildPlace("PLACE6", 57.731102, 12.01767).build();

        List<GooglePlace> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
        places.add(place3);
        places.add(place4);
        places.add(place5);

        List<List<GooglePlace>> listOfMinorRoutes = SessionStorage.INSTANCE.splitToMinorRoutes(places);
        for(List<GooglePlace> minorRoute : listOfMinorRoutes) {
            assertTrue(minorRoute.size() == 2);
        }
    }

    @Test
    public void shouldSplitAListIntoMinorRoutesOfTwoAndTwo_evenNumberOfPlaces() {
        GooglePlace place1 = new GooglePlace.BuildPlace("PLACE1", 57.704147, 12.012863).build();
        GooglePlace place2 = new GooglePlace.BuildPlace("PLACE2", 57.738067, 12.057152).build();
        GooglePlace place3 = new GooglePlace.BuildPlace("PLACE3", 57.681761, 11.953125).build();
        GooglePlace place4 = new GooglePlace.BuildPlace("PLACE4", 57.668543, 12.057152).build();
        GooglePlace place5 = new GooglePlace.BuildPlace("PLACE5", 57.712951, 11.975441).build();
        GooglePlace place6 = new GooglePlace.BuildPlace("PLACE6", 57.731102, 12.01767).build();

        List<GooglePlace> places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
        places.add(place3);
        places.add(place4);
        places.add(place5);
        places.add(place6);

        List<List<GooglePlace>> listOfMinorRoutes = SessionStorage.INSTANCE.splitToMinorRoutes(places);
        for(List<GooglePlace> minorRoute : listOfMinorRoutes) {
            assertTrue(minorRoute.size() == 2);
        }
    }
}
