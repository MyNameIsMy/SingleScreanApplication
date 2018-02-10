package projects.suchushin.org.ubrainianstest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "geonames.db";
    private final static String TABLE_NAME = "GeoNamesTable";
    private final static String COLUMN_NAME = "ListOfSearchResult";
    private final static String CREATE_QUERY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "(" + COLUMN_NAME + " TEXT)";
    private static int version = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, version);
        Log.d("Database operation", "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void putInformation(String entry){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME, entry);
        database.insert(TABLE_NAME, null, cv);
    }

    public Cursor getInformation(){
        SQLiteDatabase database = getReadableDatabase();
        String[] columns = {COLUMN_NAME};
        return database.query(TABLE_NAME, columns, null, null, null, null, null);
    }

    public void deleteInformation(String entry){
        SQLiteDatabase database = getWritableDatabase();
        String selection = COLUMN_NAME + " LIKE ?";
        String[] args = {entry};
        database.delete(TABLE_NAME, selection, args);
    }
}
