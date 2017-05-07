package yuqi.amc.SQLiteData;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ClayW on 14/04/2017.
 */

public class DataStruct implements Parcelable {

    protected long id;
    protected String name;

    public DataStruct(){}

    //
    public DataStruct(long id, String name){
        this.id = id;
        this.name = name;
    }

    public long getId(){
        return id;
    }

    public String getName(){
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    public static final Creator<DataStruct> CREATOR = new Creator<DataStruct>() {
        @Override
        public DataStruct createFromParcel(Parcel source) {
            return new DataStruct(source);
        }

        @Override
        public DataStruct[] newArray(int size) {
            return new DataStruct[size];
        }
    };

    protected DataStruct(Parcel source){
        id = source.readInt();
        name = source.readString();
    }
}
