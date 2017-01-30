package lorentzonsolutions.rhome.shared;

/**
 * Enum containing the Google Maps API location types.
 */

public enum LocationTypes {

    ACCOUNTING("accounting", "Accounting"), AIRPORT("airport", "Airport"), AMUSEMENT_PARK("amusement_park", "Amusement Park"), AQUARIUM("aquarium", "Aquarium"),
    ART_GALLERY("art_gallery", "Art Gallery"), ATM("atm", "ATM"), BAKERY("bakery", "Bakery"), BANK("bank", "Bank"), BAR("bar", "Bar"), BEAUTY_SALON("beauty_salon", "Beauty Salon"),
    BICYCLE_STORE("bicycle_store", "Bicycle Store"), BOOK_STORE("book_store", "Book Store"), BOWLING_ALLEY("bowling_alley", "Bowling Alley"), BUS_STATION("bus_station", "Bus Station"),
    CAFE("cafe", "Cafe");


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
accounting
airport
amusement_park
aquarium
art_gallery
atm
bakery
bank
bar
beauty_salon
bicycle_store
book_store
bowling_alley
bus_station
cafe
--------------------
campground
car_dealer
car_rental
car_repair
car_wash
casino
cemetery
church
city_hall
clothing_store
convenience_store
courthouse
dentist
department_store
doctor
electrician
electronics_store
embassy
establishment (deprecated)
finance (deprecated)
fire_station
florist
food (deprecated)
funeral_home
furniture_store
gas_station
general_contractor (deprecated)
grocery_or_supermarket (deprecated)
gym
hair_care
hardware_store
health (deprecated)
hindu_temple
home_goods_store
hospital
insurance_agency
jewelry_store
laundry
lawyer
library
liquor_store
local_government_office
locksmith
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
