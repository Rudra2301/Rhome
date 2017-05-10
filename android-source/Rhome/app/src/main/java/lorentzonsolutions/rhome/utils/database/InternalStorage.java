package lorentzonsolutions.rhome.utils.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.List;

import lorentzonsolutions.rhome.interfaces.Storage;
import lorentzonsolutions.rhome.shared.GoogleLocationTypes;
import static lorentzonsolutions.rhome.utils.database.DatabaseConstants.*;

/**
 * Class for handling database connection.
 */

public class InternalStorage extends SQLiteOpenHelper implements Storage{


    private static String TAG = InternalStorage.class.toString();

    public InternalStorage(Context context) {
        super(context, APP_DATABASE_NAME, null, APP_DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCATION_TYPES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // db.execSQL(LOCATION_TYPE_DELETE);
        // onCreate(db);
    }

    @Override
    public void increaseForTypes(GoogleLocationTypes[] types) {
        for (GoogleLocationTypes type : types) {
            Log.d(TAG, "Incrementing type: " + type);
        }
    }

    @Override
    public void decreaseForTypes(GoogleLocationTypes[] types) {
        for (GoogleLocationTypes type : types) {
            Log.d(TAG, "Decreasing type: " + type);
        }
    }

    @Override
    public int getCountForType(GoogleLocationTypes type) {
        return 0;
    }

    @Override
    public List<GoogleLocationTypes> getOrderedListOfTypesFromDB() {
        return null;
    }

}
