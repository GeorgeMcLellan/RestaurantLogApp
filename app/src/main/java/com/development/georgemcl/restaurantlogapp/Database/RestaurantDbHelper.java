package com.development.georgemcl.restaurantlogapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by George on 17-Nov-17.
 */

public class RestaurantDbHelper extends SQLiteOpenHelper{

    public static final String DATABASE_NAME = "Restaurant.db";
    public static final String TABLE_NAME = "restaurant_table";

    public static final String FIELD_ID = "_id";

    public static final String FIELD_PLACE_ID = "placeid";

    public static final String FIELD_NAME = "name";
    public static final String FIELD_LAT = "lat";
    public static final String FIELD_LNG = "lng";

    public static final String FIELD_CUISINE = "cuisine";
    public static final String FIELD_PRICE_LEVEL = "pricelevel";
    public static final String FIELD_RATING = "rating";



    public RestaurantDbHelper(Context context) {
        super(context, DATABASE_NAME, null, 2);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FIELD_PLACE_ID + " TEXT, "+
                FIELD_NAME + " TEXT, "+
                FIELD_LAT + " DOUBLE, "+
                FIELD_LNG + " DOUBLE, "+
                FIELD_CUISINE + " TEXT, " +
                FIELD_PRICE_LEVEL + " TEXT, " +
                FIELD_RATING + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean insertData(String place_id, String name, double latitude, double longitude, String cuisine, String price_level, String rating){

        ContentValues contentValues = new ContentValues();
        contentValues.put(FIELD_PLACE_ID, place_id);
        contentValues.put(FIELD_NAME, name);
        contentValues.put(FIELD_LAT, latitude);
        contentValues.put(FIELD_LNG, longitude);
        contentValues.put(FIELD_CUISINE, cuisine);
        contentValues.put(FIELD_PRICE_LEVEL, price_level);
        contentValues.put(FIELD_RATING, rating);

        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TABLE_NAME,null, contentValues) != -1;
    }

    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }
}
