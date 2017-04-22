package yuqi.amc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

import yuqi.amc.DataStruct.Badge;
import yuqi.amc.DataStruct.Brand;
import yuqi.amc.DataStruct.DataStruct;
import yuqi.amc.DataStruct.Model;
import yuqi.amc.DataStruct.Part;


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

        if (data instanceof Badge){
            values.put("BADGE_NAME", data.getName());
            values.put("BADGE_YEAR", ((Badge) data).getYear());
            values.put("MODEL_NAME", ((Badge) data).getModelName());
            db.insert("BADGE", null, values);
        }

        if (data instanceof Part){
            values.put("PART_NAME", data.getName());
            values.put("PART_TYPE", ((Part) data).getType());
            values.put("PART_PRICE", ((Part) data).getPrice());
            values.put("PART_STOCK", ((Part) data).getStock());
            values.put("MODEL_NAME", ((Part) data).getModelName());
            values.put("BADGE_NAME", ((Part) data).getBadgeName());
            db.insert("PART", null, values);
        }
        db.close();
    }

    public HashMap<Long, DataStruct> getData(String tableName, String[] columnName, String[] args ) {

        HashMap<Long, DataStruct> data = new LinkedHashMap<>();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor;

        if (columnName != null){

            if(columnName.length == 1){
                cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + columnName[0] + " =?", args);
            }else if (columnName.length == 2){
                cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + columnName[0] + " =?" + "AND " + columnName[1] + " =?", args);
            }else {
                cursor = db.rawQuery("SELECT * FROM " + tableName + " WHERE " + columnName[0] + " =?" + "AND " + columnName[1] + " =?" + "AND " + columnName[2] + " =?", args);
            }
        }else{
            cursor = db.rawQuery("SELECT * FROM " + tableName, null);
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
                        DataStruct newData = new Part(
                                cursor.getLong(0),
                                cursor.getString(1),
                                cursor.getString(2),
                                cursor.getDouble(3),
                                cursor.getInt(4),
                                cursor.getString(5),
                                cursor.getString(6)
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

    public int countParts(String[] args){
        SQLiteDatabase db = this.getReadableDatabase();
        int num = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM PART WHERE MODEL_NAME = ? AND BADGE_NAME = ?", args);
        if (cursor != null && cursor.moveToFirst()) {
            num = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        return num;
    }

}