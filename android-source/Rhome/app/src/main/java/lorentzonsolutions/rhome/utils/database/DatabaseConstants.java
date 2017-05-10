package lorentzonsolutions.rhome.utils.database;

import lorentzonsolutions.rhome.shared.GoogleLocationTypes;

/**
 * Contains the constants and queries for app database.
 */

public class DatabaseConstants {

    public static final String APP_DATABASE_NAME = "RhomeAppDB";
    public static final int APP_DATABASE_VERSION = 1;

    public static final String TABLE_LOCATION_TYPES = "location_types_table";
    public static final String TYPE = "name";
    public static final String COUNT = "count";

    public static final String CREATE_TABLE_LOCATION_TYPES = "CREATE table " +
            TABLE_LOCATION_TYPES + "(" +
            TYPE + " text primary key," +
            COUNT + " text " +
            ")";

    public static final String LOCATION_TYPE_DELETE = "DROP TABLE IF EXISTS " + TABLE_LOCATION_TYPES;

    public static String query_getCountForType(GoogleLocationTypes type) {
        return "SELECT " + COUNT + " FROM " + TABLE_LOCATION_TYPES + " WHERE " + TYPE + " = " + type.getAsGoogleType() + ";";
    }
    public static String query_updateCountForType(GoogleLocationTypes type, int newCount) {
        return "UPDATE " + TABLE_LOCATION_TYPES + " SET " + COUNT + " = " + newCount + " WHERE " + TYPE + " = " + type.getAsGoogleType();
    }

    public static String query_insertTypeWithValueOne(GoogleLocationTypes type) {
        return "INSERT INTO " + TABLE_LOCATION_TYPES + "[(" + TYPE + ", " + COUNT + ")] VALUES (" + type.getAsGoogleType() + ", " + 1 + ")";
    }


}
