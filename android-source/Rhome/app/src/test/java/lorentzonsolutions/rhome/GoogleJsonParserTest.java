package lorentzonsolutions.rhome;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.HashMap;
import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GoogleDistanceDuration;
import lorentzonsolutions.rhome.googleWebApi.GooglePlace;
import lorentzonsolutions.rhome.googleWebApi.GoogleJsonParser;

import static junit.framework.Assert.assertNotNull;

/**
 * Test class for JsonDataParser.
 *
 * @author Johan Lorentzon
 *
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class GoogleJsonParserTest {

    GoogleJsonParser unitUnderTest;

    @Before
    public void before() {
        unitUnderTest = new GoogleJsonParser();
    }

    @Test
    public void shouldParseNearyLocationsDataresponseToGooglePlaces() {
        /*
        API url
        https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=57.687801,11.92067&radius=100&types=book_store&key=[KEY];
         */

        String apiResponseData =
                "{ \"html_attributions\" : [], \"results\" : [ { \"geometry\" : { \"location\" : { \"lat\" : 57.6875473, \"lng\" : 11.9214534 }, \"viewport\" : { \"northeast\" : { \"lat\" : 57.6888200302915, \"lng\" : 11.9225967802915 }, \"southwest\" : { \"lat\" : 57.6861220697085, \"lng\" : 11.91989881970849 } } }, \"icon\" : \"https://maps.gstatic.com/mapfiles/place_api/icons/shopping-71.png\", \"id\" : \"b13eecbbfe6be27ef44502327f5579875c646906\", \"name\" : \"Mariaplans bokhandel\", \"opening_hours\" : { \"open_now\" : false, \"weekday_text\" : [] }, \"photos\" : [ { \"height\" : 2896, \"html_attributions\" : [ \"\\u003ca href=\\\"https://maps.google.com/maps/contrib/116499325128804474249/photos\\\"\\u003eTomas Zeljko\\u003c/a\\u003e\" ], \"photo_reference\" : \"CmRYAAAAu-uy4mTS7FxZhy1k8m8IcjMOZ0TFAXLyUkbrYR3JOyTbV6mgyxX-BwDn-jiBCih5fOqPkWskl0H1WWuIEbGBbsomqNVJVXO_HULvM13DZNXHdzRGfHXVVsQW3WilJZ9vEhBzaUYjGqYFld3O6JU4FPP0GhTuuhk6PXHXOM68I_TN-Bq1i4Pvlw\", \"width\" : 5152 } ], \"place_id\" : \"ChIJ37ZffzHzT0YRirz5KLQW1Kk\", \"rating\" : 4.3, \"reference\" : \"CmRSAAAAxQYDSLcXSwIH_4kPYkWIjcFnR2-lAJ6sj_K2YMJU42xzfHHJwaAmcILvX-AdBeOQQp9vkTLxaMRZcASd6whHrBajBjyG2fUdTxy2t17l7OdWNePsgjOaq368QmtopStbEhCpAx1DKC6cAyJIC9OuDDStGhTIej8yjfu06a5xCjyH83HcTYsJ1g\", \"scope\" : \"GOOGLE\", \"types\" : [ \"book_store\", \"store\", \"point_of_interest\", \"establishment\" ], \"vicinity\" : \"Slottsskogsgatan 46, Göteborg\" } ], \"status\" : \"OK\" }";

        List<GooglePlace> places = unitUnderTest.parseNearbySearchData(apiResponseData);
        System.out.println(places);
        assertNotNull(places);
    }

    @Test
    public void shouldParseReverseGeocodeData() {
        /*
        API url
        https://maps.googleapis.com/maps/api/geocode/json?latlng=57.6875473,11.9214534&key=[KEY]
         */

        String apiResponseData =
                "{ \"results\" : [ { \"address_components\" : [ { \"long_name\" : \"46\", \"short_name\" : \"46\", \"types\" : [ \"street_number\" ] }, { \"long_name\" : \"Slottsskogsgatan\", \"short_name\" : \"Slottsskogsgatan\", \"types\" : [ \"route\" ] }, { \"long_name\" : \"Majorna\", \"short_name\" : \"Majorna\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"locality\", \"political\" ] }, { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"postal_town\" ] }, { \"long_name\" : \"Västra Götalands län\", \"short_name\" : \"Västra Götalands län\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Sverige\", \"short_name\" : \"SE\", \"types\" : [ \"country\", \"political\" ] }, { \"long_name\" : \"414 70\", \"short_name\" : \"414 70\", \"types\" : [ \"postal_code\" ] } ], \"formatted_address\" : \"Slottsskogsgatan 46, 414 70 Göteborg, Sverige\", \"geometry\" : { \"location\" : { \"lat\" : 57.6875473, \"lng\" : 11.9214534 }, \"location_type\" : \"ROOFTOP\", \"viewport\" : { \"northeast\" : { \"lat\" : 57.68889628029151, \"lng\" : 11.9228023802915 }, \"southwest\" : { \"lat\" : 57.6861983197085, \"lng\" : 11.9201044197085 } } }, \"place_id\" : \"ChIJ37ZffzHzT0YRFyeN2Ad23rQ\", \"types\" : [ \"street_address\" ] }, { \"address_components\" : [ { \"long_name\" : \"Majorna\", \"short_name\" : \"Majorna\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"locality\", \"political\" ] }, { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"Västra Götalands län\", \"short_name\" : \"Västra Götalands län\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Sverige\", \"short_name\" : \"SE\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"Majorna, Göteborg, Sverige\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : 57.70081500000001, \"lng\" : 11.9345488 }, \"southwest\" : { \"lat\" : 57.68708710000001, \"lng\" : 11.9104785 } }, \"location\" : { \"lat\" : 57.6951745, \"lng\" : 11.927827 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : 57.70081500000001, \"lng\" : 11.9345488 }, \"southwest\" : { \"lat\" : 57.68708710000001, \"lng\" : 11.9104785 } } }, \"place_id\" : \"ChIJDUfU2DXzT0YR278afd9LAPM\", \"types\" : [ \"political\", \"sublocality\", \"sublocality_level_1\" ] }, { \"address_components\" : [ { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"locality\", \"political\" ] }, { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"Västra Götalands län\", \"short_name\" : \"Västra Götalands län\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Sverige\", \"short_name\" : \"SE\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"Göteborg, Sverige\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : 57.863055, \"lng\" : 12.193316 }, \"southwest\" : { \"lat\" : 57.5389291, \"lng\" : 11.594357 } }, \"location\" : { \"lat\" : 57.70887, \"lng\" : 11.97456 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : 57.863055, \"lng\" : 12.192838 }, \"southwest\" : { \"lat\" : 57.5408589, \"lng\" : 11.5945269 } } }, \"place_id\" : \"ChIJPwdslmeOT0YRQHwOKXiQAQQ\", \"types\" : [ \"locality\", \"political\" ] }, { \"address_components\" : [ { \"long_name\" : \"414 70\", \"short_name\" : \"414 70\", \"types\" : [ \"postal_code\" ] }, { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"locality\", \"political\" ] }, { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"postal_town\" ] }, { \"long_name\" : \"Västra Götalands län\", \"short_name\" : \"Västra Götalands län\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Sverige\", \"short_name\" : \"SE\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"414 70 Göteborg, Sverige\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : 57.68822789999999, \"lng\" : 11.930746 }, \"southwest\" : { \"lat\" : 57.684353, \"lng\" : 11.91945 } }, \"location\" : { \"lat\" : 57.6867069, \"lng\" : 11.9248456 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : 57.68822789999999, \"lng\" : 11.930746 }, \"southwest\" : { \"lat\" : 57.684353, \"lng\" : 11.91945 } } }, \"place_id\" : \"ChIJd4V9rDHzT0YRwxrAB3iQAQs\", \"types\" : [ \"postal_code\" ] }, { \"address_components\" : [ { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"postal_town\" ] }, { \"long_name\" : \"Västra Götalands län\", \"short_name\" : \"Västra Götalands län\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Sverige\", \"short_name\" : \"SE\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"Göteborg, Sverige\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : 57.79029490000001, \"lng\" : 12.1055639 }, \"southwest\" : { \"lat\" : 57.66720799999999, \"lng\" : 11.7939059 } }, \"location\" : { \"lat\" : 57.7058708, \"lng\" : 11.9388136 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : 57.79029490000001, \"lng\" : 12.1055639 }, \"southwest\" : { \"lat\" : 57.66720799999999, \"lng\" : 11.7939059 } } }, \"place_id\" : \"ChIJTe4awGJiRUYRPPX8JcKpisU\", \"types\" : [ \"postal_town\" ] }, { \"address_components\" : [ { \"long_name\" : \"Göteborg\", \"short_name\" : \"Göteborg\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"long_name\" : \"Västra Götalands län\", \"short_name\" : \"Västra Götalands län\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Sverige\", \"short_name\" : \"SE\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"Göteborg, Sverige\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : 57.867944, \"lng\" : 12.216002 }, \"southwest\" : { \"lat\" : 57.5068119, \"lng\" : 11.6931389 } }, \"location\" : { \"lat\" : 57.7372915, \"lng\" : 11.9713569 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : 57.867944, \"lng\" : 12.216002 }, \"southwest\" : { \"lat\" : 57.5092279, \"lng\" : 11.6998109 } } }, \"place_id\" : \"ChIJf2rnIRTzT0YRjiyUaXB5Awc\", \"types\" : [ \"administrative_area_level_2\", \"political\" ] }, { \"address_components\" : [ { \"long_name\" : \"Västra Götalands län\", \"short_name\" : \"Västra Götalands län\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"long_name\" : \"Sverige\", \"short_name\" : \"SE\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"Västra Götalands län, Sverige\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : 59.26203409999999, \"lng\" : 14.717377 }, \"southwest\" : { \"lat\" : 57.14008, \"lng\" : 10.961968 } }, \"location\" : { \"lat\" : 58.2527926, \"lng\" : 13.0596425 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : 59.26203409999999, \"lng\" : 14.7148095 }, \"southwest\" : { \"lat\" : 57.14008, \"lng\" : 10.9636245 } } }, \"place_id\" : \"ChIJlRL-UXszRUYRcIsOKXiQAQM\", \"types\" : [ \"administrative_area_level_1\", \"political\" ] }, { \"address_components\" : [ { \"long_name\" : \"Sverige\", \"short_name\" : \"SE\", \"types\" : [ \"country\", \"political\" ] } ], \"formatted_address\" : \"Sverige\", \"geometry\" : { \"bounds\" : { \"northeast\" : { \"lat\" : 69.0599709, \"lng\" : 24.1773102 }, \"southwest\" : { \"lat\" : 55.0059799, \"lng\" : 10.5798 } }, \"location\" : { \"lat\" : 60.12816100000001, \"lng\" : 18.643501 }, \"location_type\" : \"APPROXIMATE\", \"viewport\" : { \"northeast\" : { \"lat\" : 69.05916479999999, \"lng\" : 24.1616356 }, \"southwest\" : { \"lat\" : 55.33860300000001, \"lng\" : 10.9639409 } } }, \"place_id\" : \"ChIJ8fA1bTmyXEYRYm-tjaLruCI\", \"types\" : [ \"country\", \"political\" ] } ], \"status\" : \"OK\" }";

        GooglePlace place = unitUnderTest.parseReverseGeocodeDataToPlaceInformation(apiResponseData);
        System.out.println(place);
        assertNotNull(place);
    }

    @Test
    public void shouldParseDistanceDurationDataToDistanceDurationObject() {
        /*
        https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins=57.6875473,11.9214534&destinations=57.683941,11.916261&mode=driving&key=[KEY]
         */

        String apiResponseData =
                "{ \"destination_addresses\" : [ \"Mariagatan 26C, 414 71 Göteborg, Sverige\" ], \"origin_addresses\" : [ \"Ärlegatan 2, 414 57 Göteborg, Sverige\" ], \"rows\" : [ { \"elements\" : [ { \"distance\" : { \"text\" : \"0,6 km\", \"value\" : 571 }, \"duration\" : { \"text\" : \"3 min\", \"value\" : 166 }, \"status\" : \"OK\" } ] } ], \"status\" : \"OK\" }";

        GoogleDistanceDuration distanceDuration = unitUnderTest.parseDistanceCalculationData(apiResponseData);

        System.out.println(distanceDuration);
        assertNotNull(distanceDuration);

    }

    @Test
    public void shouldParseDirectionsData() throws JSONException {

        /*
        https://maps.googleapis.com/maps/api/directions/json?origin=57.6875473,11.9214534&destination=57.683941,11.916261&sensor=false
        */

        String apiResponseData = "{ \"geocoded_waypoints\" : [ { \"geocoder_status\" : \"OK\", \"place_id\" : \"EifDhHJsZWdhdGFuIDIsIDQxNCA1NyBHw7Z0ZWJvcmcsIFN2ZXJpZ2U\", \"types\" : [ \"street_address\" ] }, { \"geocoder_status\" : \"OK\", \"place_id\" : \"ChIJjzWWkS3zT0YRDk0sOuPia4Q\", \"types\" : [ \"street_address\" ] } ], \"routes\" : [ { \"bounds\" : { \"northeast\" : { \"lat\" : 57.6876035, \"lng\" : 11.9212744 }, \"southwest\" : { \"lat\" : 57.6838309, \"lng\" : 11.9159391 } }, \"copyrights\" : \"Kartdata ©2017 Google\", \"legs\" : [ { \"distance\" : { \"text\" : \"0,6 km\", \"value\" : 571 }, \"duration\" : { \"text\" : \"3 min\", \"value\" : 166 }, \"end_address\" : \"Mariagatan 26C, 414 71 Göteborg, Sverige\", \"end_location\" : { \"lat\" : 57.68393839999999, \"lng\" : 11.9162782 }, \"start_address\" : \"Ärlegatan 2, 414 57 Göteborg, Sverige\", \"start_location\" : { \"lat\" : 57.6876035, \"lng\" : 11.9212744 }, \"steps\" : [ { \"distance\" : { \"text\" : \"26 m\", \"value\" : 26 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 6 }, \"end_location\" : { \"lat\" : 57.68744339999999, \"lng\" : 11.920972 }, \"html_instructions\" : \"Kör \\u003cb\\u003eåt sydväst\\u003c/b\\u003e på \\u003cb\\u003eÄrlegatan\\u003c/b\\u003e mot \\u003cb\\u003eSlottsskogsgatan\\u003c/b\\u003e\", \"polyline\" : { \"points\" : \"obb_J}jwgAHJNZDR\" }, \"start_location\" : { \"lat\" : 57.6876035, \"lng\" : 11.9212744 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"38 m\", \"value\" : 38 }, \"duration\" : { \"text\" : \"1 min\", \"value\" : 12 }, \"end_location\" : { \"lat\" : 57.6871394, \"lng\" : 11.9212227 }, \"html_instructions\" : \"Sväng \\u003cb\\u003evänster\\u003c/b\\u003e till \\u003cb\\u003eSlottskogsgatan\\u003c/b\\u003e\", \"maneuver\" : \"turn-left\", \"polyline\" : { \"points\" : \"oab_JaiwgAHMJ@BCTY@?HG\" }, \"start_location\" : { \"lat\" : 57.68744339999999, \"lng\" : 11.920972 }, \"travel_mode\" : \"DRIVING\" }, { \"distance\" : { \"text\" : \"0,5 km\", \"value\" : 507 }, \"duration\" : { \"text\" : \"2 min\", \"value\" : 148 }, \"end_location\" : { \"lat\" : 57.68393839999999, \"lng\" : 11.9162782 }, \"html_instructions\" : \"I rondellen tar du \\u003cb\\u003e1:a\\u003c/b\\u003e avfarten in på \\u003cb\\u003eMariagatan\\u003c/b\\u003e\", \"maneuver\" : \"roundabout-right\", \"polyline\" : { \"points\" : \"s_b_JsjwgAHFDNJVfAxB^t@FNLVp@lAT^FNDHHLNZxArClA|BjAbCpAdCXh@R^DI@?@E@ADEDGUc@\" }, \"start_location\" : { \"lat\" : 57.6871394, \"lng\" : 11.9212227 }, \"travel_mode\" : \"DRIVING\" } ], \"traffic_speed_entry\" : [], \"via_waypoint\" : [] } ], \"overview_polyline\" : { \"points\" : \"obb_J}jwgAXf@DRHMNAVYHGHFPf@|BvEvGbM|ChGl@hAJQJMUc@\" }, \"summary\" : \"Mariagatan\", \"warnings\" : [], \"waypoint_order\" : [] } ], \"status\" : \"OK\" }";

        List<List<HashMap<String,String>>> parsedResponse = unitUnderTest.parseDirectionsData(apiResponseData);
        int nrPoints = 0;
        for(List<HashMap<String, String>> pointData : parsedResponse) {
            nrPoints += pointData.size();
            for(HashMap<String, String> pointInfo : pointData) {
                System.out.println("Point: " + pointInfo);
            }
        }
        System.out.println("Nr points: " + nrPoints);
        System.out.println("Nr routes: " + parsedResponse.size());

        assertNotNull(parsedResponse);

    }
}
