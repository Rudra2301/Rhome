package lorentzonsolutions.rhome.interfaces;

import java.util.List;

import lorentzonsolutions.rhome.googleWebApi.GoogleLocationTypes;

/**
 * Interface for creating a storage.
 */

public interface Storage {
    /**
     * Increases the count by 1 for given type.
     * @param type
     */
    void incrementType(GoogleLocationTypes type);

    /**
     * Resets the count to 0 for the type.
     * @param type
     */
    void resetType(GoogleLocationTypes type);

    /**
     * Returns a list of the recorded types in DB. List starts with highest and then descends.
     * @return
     */
    List<GoogleLocationTypes> getOrderedListOfTypesFromDB();

    /**
     * Returns the count for the type in db.
     *
     * @param type
     * @return the count for type recorded in db.
     */
    int getCountForType(GoogleLocationTypes type);


}
