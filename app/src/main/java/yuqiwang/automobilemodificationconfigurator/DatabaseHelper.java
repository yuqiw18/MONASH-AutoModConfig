package yuqiwang.automobilemodificationconfigurator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.HashMap;
import java.util.LinkedHashMap;

import yuqiwang.automobilemodificationconfigurator.DataStruct.Brand;
import yuqiwang.automobilemodificationconfigurator.DataStruct.DataStruct;

/**
 * Created by ClayW on 2/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Set Database Properties
    public static final String DATABASE_NAME = "AMC_DB";
    public static final int DATABASE_VERSION = 1;


    private static final String BRAND_CREATE_STATEMENT = "CREATE TABLE BRAND " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "BRAND_NAME TEXT NOT NULL," +
            "BRAND_ORIGIN TEXT NOT NULL" +
            ");";

//    private static final String MODEL_CREATE_STATEMENT = "CREATE TABLE MODEL " +
//            "(" +
//            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
//            "BRAND_NAME TEXT NOT NULL" +
//            ");";;

    //private static final String BADGE_CREATE_STATEMENT;

    //private static final String PARTS_CREATE_STATEMENT;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(BRAND_CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Monster.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addData(Brand data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //if (data instanceof DataStruct.Brand){
            values.put("BRAND_NAME", data.getName());
            values.put("BRAND_ORIGIN", data.getOrigin());
        //}
        db.insert("BRAND", null, values);
        db.close();
    }

    public void createDefault() {
        addData(new Brand(0, "Mercedes-Benz", "Germany"));
    }

    public HashMap<Long, Brand> getData() {

        HashMap<Long, Brand> data = new LinkedHashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM BRAND", null);
        // Add each person to hashmap (Each row has 1 person)
        if(cursor.moveToFirst()) {
            do {
                Brand newData = new Brand(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2)
                        );
                data.put(newData.getId(), newData);
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }
}