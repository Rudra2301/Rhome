package lorentzonsolutions.rhome.shared;

/**
 * Enum containing the Google Maps API location types.
 */

public enum LocationTypes {

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
    LOCKSMITH("locksmith", "Locksmith");


    private String googleType;
    private String readable;

    LocationTypes(String googleType, String readable) {
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
}

/*
funeral_home
hindu_temple
---------------------------
lodging
meal_delivery
meal_takeaway
mosque
movie_rental
movie_theater
moving_company
museum
night_club
painter
park
parking
pet_store
pharmacy
physiotherapist
place_of_worship (deprecated)
plumber
police
post_office
real_estate_agency
restaurant
roofing_contractor
rv_park
school
shoe_store
shopping_mall
spa
stadium
storage
store
subway_station
synagogue
taxi_stand
train_station
transit_station
travel_agency
university
veterinary_care
zoo
 */
