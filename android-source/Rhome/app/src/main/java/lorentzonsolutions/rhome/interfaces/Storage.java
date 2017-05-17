package lorentzonsolutions.rhome.interfaces;

import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GoogleLocationTypes;

/**
 * Interface for creating a storage.
 */

public interface Storage {
    /**
     * Takes an array of GoogleLocationTypes and increments the value for those in the database.
     * @param types
     */
    void increaseForTypes(GoogleLocationTypes[] types);

    /**
     * Takes an array of GoogleLocationTypes and increments the value for those in the database.
     * @param types
     */
    void decreaseForTypes(GoogleLocationTypes[] types);

    /**
     * Returns the count for given type. -1 if type doesn't exist in DB.
     * @param type
     * @return
     */
    int getCountForType(GoogleLocationTypes type);

    /**
     * Returns a list of the recorded types in DB. List starts with highest and then descends.
     * @return
     */
    List<GoogleLocationTypes> getOrderedListOfTypesFromDB();


}
