package yuqiwang.automobilemodificationconfigurator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

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
        //sqLiteDatabase.execSQL(Monster.CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Monster.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addMonster() {

    }



    public void updateBrand(){

    }


    public void createDefault() {

    }
}