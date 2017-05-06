package yuqi.amc.SqliteData;

import android.os.Parcel;

/**
 * Created by ClayW on 14/04/2017.
 */

public class Badge extends DataStruct {


    public static final String CREATE_STATEMENT = "CREATE TABLE BADGE " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "BADGE_NAME TEXT NOT NULL," +
            "BADGE_YEAR TEXT NOT NULL," +
            "MODEL_NAME TEXT NOT NULL," +
            "FOREIGN KEY(MODEL_NAME) REFERENCES MODEL(MODEL_NAME)" +
            ");";

    private String year;
    private String modelName;

    public Badge(long id, String name, String year, String modelName){
        super(id, name);
        this.year = year;
        this.modelName = modelName;
    }

    public String getYear(){
        return year;
    }

    public String getModelName(){
        return modelName;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.year);
        dest.writeString(this.modelName);
    }

    public static final Creator<Badge> CREATOR = new Creator<Badge>() {
        @Override
        public Badge createFromParcel(Parcel source) {
            return new Badge(source);
        }

        @Override
        public Badge[] newArray(int size) {
            return new Badge[size];
        }
    };

    protected Badge(Parcel source){
        id = source.readLong();
        name = source.readString();
        year = source.readString();
        modelName = source.readString();
    }
}
