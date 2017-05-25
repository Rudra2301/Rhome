package lorentzonsolutions.rhome.utils.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import lorentzonsolutions.rhome.interfaces.Storage;
import lorentzonsolutions.rhome.googleWebApi.GoogleLocationTypes;
import static lorentzonsolutions.rhome.utils.database.DatabaseConstants.*;

/**
 * Class for handling database connection.
 */

public class InternalStorage extends SQLiteOpenHelper implements Storage {


    private static String TAG = InternalStorage.class.toString();

    public InternalStorage(Context context) {
        super(context, APP_DATABASE_NAME, null, APP_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Database created.");
        db.execSQL(CREATE_TABLE_LOCATION_TYPES);
        // Add all location types to db
        for(GoogleLocationTypes type: GoogleLocationTypes.values())
            db.execSQL(query_insertNewType(type));

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "Database upgraded.");
        // db.execSQL(LOCATION_TYPE_DELETE);
        // onCreate(db);
    }


    // Methods inherited from Storage interface.
    @Override
    public void incrementType(GoogleLocationTypes type) {
        Log.d(TAG, "Incrementing type: " + type);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query_incrementCountForType(type));
    }

    @Override
    public void resetType(GoogleLocationTypes type) {
        Log.d(TAG, "Resetting count for type: " + type);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query_resetCountForType(type));
    }

    @Override
    public List<GoogleLocationTypes> getOrderedListOfTypesFromDB() {
        SQLiteDatabase db = getReadableDatabase();
        String[] selectionArgs = {};
        Cursor c = db.rawQuery(query_getAllByDescOrder(), selectionArgs);
        List<GoogleLocationTypes> orderedTypesList = new ArrayList<>();

        while(c.moveToNext()) {
            String typeString = c.getString(c.getColumnIndex(TYPE_COLUMN_NAME));
            GoogleLocationTypes type = getGoogleTypeFromString(typeString);
            if(type != null) orderedTypesList.add(type);
        }

        return orderedTypesList;
    }

    private GoogleLocationTypes getGoogleTypeFromString(String typeString) {
        for(GoogleLocationTypes type: GoogleLocationTypes.values()) {
            if(type.getAsGoogleType().equals(typeString)) return type;
        }
        return null;
    }

}
