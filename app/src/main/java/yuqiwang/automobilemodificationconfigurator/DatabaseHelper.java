package yuqiwang.automobilemodificationconfigurator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

import yuqiwang.automobilemodificationconfigurator.DataStruct.DataStruct;
import yuqiwang.automobilemodificationconfigurator.DataStruct.Badge;
import yuqiwang.automobilemodificationconfigurator.DataStruct.Brand;
import yuqiwang.automobilemodificationconfigurator.DataStruct.Model;
import yuqiwang.automobilemodificationconfigurator.DataStruct.Part;

/**
 * Created by ClayW on 2/04/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Set Database Properties
    public static final String DATABASE_NAME = "AMC_DB";
    public static final int DATABASE_VERSION = 1;



    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(Brand.CREATE_STATEMENT);
        sqLiteDatabase.execSQL(Model.CREATE_STATEMENT);
        sqLiteDatabase.execSQL(Badge.CREATE_STATEMENT);
        sqLiteDatabase.execSQL(Part.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Monster.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addData(DataStruct data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (data instanceof Brand){
            values.put("BRAND_NAME", data.getName());
            values.put("BRAND_ORIGIN", ((Brand)data).getOrigin());
            db.insert("BRAND", null, values);
        }

        if (data instanceof Model){
            values.put("MODEL_NAME", data.getName());
            values.put("MODEL_BODY_TYPE", ((Model) data).getBodyType());
            values.put("BRAND_NAME", ((Model) data).getBrandName());
            db.insert("MODEL", null, values);
        }

        db.close();
    }

    public void createDefault() {
        addData(new Brand(0, "Mercedes-Benz", "Germany"));
    }



    public HashMap<Long, DataStruct> getData(String tableName, String columnName, String[] args ) {

        HashMap<Long, DataStruct> data = new LinkedHashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor;

        if (columnName == null){

            cursor = db.rawQuery("SELECT * FROM " + tableName, null);

        }else{

            cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + columnName + " =?", args);
        }

        if(cursor.moveToFirst()) {

            switch (tableName){

                case "BRAND":
                    do {
                        DataStruct newData = new Brand(
                                cursor.getLong(0),
                                cursor.getString(1),
                                cursor.getString(2)
                        );
                        data.put(newData.getId(), newData);
                    }while(cursor.moveToNext());
                    break;
                case "MODEL":
                    do {
                        DataStruct newData = new Model(
                                cursor.getLong(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3)
                        );
                        data.put(newData.getId(), newData);
                    }while(cursor.moveToNext());
                    break;
                case "BADGE":
                    do {
                        DataStruct newData = new Badge(
                                cursor.getLong(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getString(3)
                        );
                        data.put(newData.getId(), newData);
                    }while(cursor.moveToNext());
                    break;
                case "PART":
                    do {
                        DataStruct newData = new Brand(
                                cursor.getLong(0),
                                cursor.getString(1),
                                cursor.getString(2)
                        );
                        data.put(newData.getId(), newData);
                    }while(cursor.moveToNext());
                    break;
            }
        }
        cursor.close();
        db.close();
        return data;
    }

    public boolean isEmpty(){

        SQLiteDatabase db = this.getReadableDatabase();
        boolean found = true;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM BRAND", null);
        if (cursor != null && cursor.moveToFirst()) {
            found = (cursor.getInt (0) == 0);
        }
        cursor.close();
        db.close();

        return found;
    }

}