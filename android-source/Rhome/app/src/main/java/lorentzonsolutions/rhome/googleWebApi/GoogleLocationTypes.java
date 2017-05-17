package lorentzonsolutions.rhome.googleWebApi;

import java.util.HashMap;
import java.util.Map;

/**
 * Enum containing the Google Maps API location types.
 */

public enum GoogleLocationTypes {

    ACCOUNTING("accounting", "Accounting"), AIRPORT("airport", "Airport"), AMUSEMENT_PARK("amusement_park", "Amusement Park"), AQUARIUM("aquarium", "Aquarium"),
    ART_GALLERY("art_gallery", "Art Gallery"), ATM("atm", "ATM"), BAKERY("bakery", "Bakery"), BANK("bank", "Bank"), BAR("bar", "Bar"), BEAUTY_SALON("beauty_salon", "Beauty Salon"),
    BICYCLE_STORE("bicycle_store", "Bicycle Store"), BOOK_STORE("book_store", "Book Store"), BOWLING_ALLEY("bowling_alley", "Bowling Alley"), BUS_STATION("bus_station", "Bus Station"),
    CAFE("cafe", "Cafe"), CAMPGROUND("campground", "Campground"), CAR_DEALER("car_dealer", "Car Dealer"), CAR_RENTAL("car_rental", "Car Rental"), CAR_REPAIR("car_repair", "Car Repair"),
    CAR_WASH("car_wash", "Car Wash"), CASINO("casion", "Casino"), CEMETERY("cemetery", "Cemetery"), CHURCH("church", "Church"), CITY_HALL("city_hall", "City Hall"),
    CLOTHING_STORE("clothing_store", "Clothing Strore"), CONVENIENCE_STORE("convenience_store", "Convenience Store"), COURTHOUSE("courthouse", "Courthouse"), DENTIST("dentist", "Dentist"),
    DEPARTMENT_STORE("department_store", "Department Store"), DOCTOR("doctor", "Doctor"), ELECTRICIAN("electrician", "Electrician"), ELECTRONICS_STORE("electronics_store", "Electronics Store"),
    EMBASSY("embassy", "Embassy"), FIRE_STATION("fire_station", "Fire Station"), FLORIST("florist", "Florist"), FURNITURE_STORE("furniture_store", "Furniture Store"),
    GAS_STATION("gas_station", "Gas Station"), GYM("gym", "Gym"), HAIR_CARE("hair_care", "Hair Care"), HARDWARE_STORE("hardware_store", "Hardware Store"), HOME_GOODS_STORE("home_goods_store", "Home Goods Store"),
    HOSPITAL("hospital", "Hospital"), INSURANCE_AGENCY("insurance_agency", "Insurance Agency"), JEWELRY_STORE("jewelry_store", "Jewelry Store"), LAUNDRY("laundry", "Laundry"),
    LAWYER("lawyer", "Lawyer"), LIBRARY("library", "Library"), LIQUOR_STORE("liquor_store", "Liquor Store"), LOCAL_GOVERNMENT_OFFICE("local_government_office", "Local Goverment Office"),
    LOCKSMITH("locksmith", "Locksmith"), RESTAURANT("restaurant", "Restaurant"),MEAL_TAKEAWAY("meal_takeaway", "Meal Takeaway"), MOSQUE("mosque", "Mosque"),
    MOVIE_RENTAL("movie_rental", "Movie Rental"), MOVIE_THEATER("movie_theater", "Movie Theater"), MUSEUM("museum", "Museum"), NIGHT_CLUB("night_club", "Night Club"),
    PAINTER("painter", "Painter"), PARKING("parking", "Parking"), PET_STORE("pet_store", "Pet Store"), PHARMACY("pharmacy", "Pharmacy"),
    PHYSIOTHERAPIST("physiotherapist", "Physiotherapist"), PLUMBER("plumber", "Plumber"), POLICE("police", "Police"), POST_OFFICE("post_office", "Post Office"),
    REAL_ESTATE_AGENCY("real_estate_agency", "Real Estate Agency"), RV_PARK("rv_park", "RV Park"), SCHOOL("school", "School"), SHOE_STORE("shoe_store", "Shoe Store"),
    SHOPPING_MALL("shopping_mall", "Shopping Mall"), SPA("spa", "Spa"), STADIUM("stadium", "Stadium"), STORE("store", "Store"), SYNAGOGUE("synagogue", "Synagogue"),
    TRAIN_STATION("train_station", "Train Station"), TRAVEL_AGENCY("travel_agency", "Travel Agency"), UNIVERSITY("university", "University"),
    VETERINARY_CARE("veterinary_care", "Veterinary Care"), ZOO("zoo", "Zoo");

    private String googleType;
    private String readable;

    GoogleLocationTypes(String googleType, String readable) {
        this.googleType = googleType;
        this.readable = readable;
    }

    public String getAsGoogleType() {
        return this.googleType;
    }

    public String getAsReadable() {
        return this.readable;
    }

    @Override
    public String toString() {
        return getAsReadable();
    }

    // Building a map of the thing to do and type associated with that activity
    public static Map<GoogleLocationTypes, String> mapOfWhatToDo;

    static {
        // TODO. Build this elsewhere and use resources (R.string...)
        mapOfWhatToDo = new HashMap<>();
        mapOfWhatToDo.put(AIRPORT, "Go to the airport");
        mapOfWhatToDo.put(AMUSEMENT_PARK, "Go to the amusement park");
        mapOfWhatToDo.put(CLOTHING_STORE, "Buy clothes");
        mapOfWhatToDo.put(ATM, "Withdraw money");

        mapOfWhatToDo.put(MOVIE_THEATER, "Go to the movies");

        mapOfWhatToDo.put(LIQUOR_STORE, "Buy liquor");

        mapOfWhatToDo.put(BAKERY, "Go to the bakery");

        mapOfWhatToDo.put(CAR_REPAIR, "Repair the car");

        mapOfWhatToDo.put(ZOO, "Visit the zoo");

    }
}

/*


Not included

transit_station
taxi_stand
storage
funeral_home
hindu_temple
lodging
meal_delivery
moving_company
park
roofing_contractor
subway_station

 */
