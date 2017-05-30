package lorentzonsolutions.rhome.utils.database;

import lorentzonsolutions.rhome.googleWebApi.GoogleLocationTypes;

/**
 * Contains the constants and queries for app database.
 *
 * @author Johan Lorentzon
 *
 */

public class DatabaseConstants {

    public static final String APP_DATABASE_NAME = "RhomeAppDB";
    public static final int APP_DATABASE_VERSION = 1;

    public static final String TABLE_NAME_LOCATION_TYPES = "LOCATION_TYPES";
    public static final String TYPE_COLUMN_NAME = "TYPE_NAME";
    public static final String COUNT_COLUMN_NAME = "COUNT";

    public static final String CREATE_TABLE_LOCATION_TYPES = "CREATE TABLE " +
            TABLE_NAME_LOCATION_TYPES + "(" +
            TYPE_COLUMN_NAME + " TEXT PRIMARY KEY NOT NULL," +
            COUNT_COLUMN_NAME + " INT NOT NULL " +
            ")";

    public static final String LOCATION_TYPE_DELETE = "DROP TABLE IF EXISTS " + TABLE_NAME_LOCATION_TYPES;



    public static String query_getCountForType(GoogleLocationTypes type) {
        return "SELECT COUNT FROM " + TABLE_NAME_LOCATION_TYPES + " WHERE " + TYPE_COLUMN_NAME + "=\"" + type.getAsGoogleType() + "\"";
    }

    public static String query_incrementCountForType(GoogleLocationTypes type) {
        return "UPDATE " + TABLE_NAME_LOCATION_TYPES +
                " SET " + COUNT_COLUMN_NAME + "=" + COUNT_COLUMN_NAME + "+1" +
                " WHERE " + TYPE_COLUMN_NAME + "=\"" + type.getAsGoogleType() + "\"";
    }

    public static String query_insertNewType(GoogleLocationTypes type) {
        return "INSERT INTO " + TABLE_NAME_LOCATION_TYPES + " VALUES (\"" + type.getAsGoogleType() + "\", " + 0 + ")";
    }

    public static String query_resetCountForType(GoogleLocationTypes type) {
        return "UPDATE " + TABLE_NAME_LOCATION_TYPES +
                " SET " + COUNT_COLUMN_NAME + "=0" +
                " WHERE " + TYPE_COLUMN_NAME + "=\"" + type.getAsGoogleType() + "\"";
    }

    public static String query_getAllByDescOrder() {
        return "SELECT * FROM " + TABLE_NAME_LOCATION_TYPES +
                " WHERE " + COUNT_COLUMN_NAME + " > 0" +
                " ORDER BY " + COUNT_COLUMN_NAME + " DESC";
    }


}
