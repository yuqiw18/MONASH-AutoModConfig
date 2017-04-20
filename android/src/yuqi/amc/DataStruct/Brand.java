package yuqi.amc.DataStruct;

import android.os.Parcel;

public class Brand extends DataStruct {

    public static final String CREATE_STATEMENT = "CREATE TABLE BRAND " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "BRAND_NAME TEXT NOT NULL," +
            "BRAND_ORIGIN TEXT NOT NULL" +
            ");";

    private String origin;

    public Brand(long id, String name, String origin){
        super(id, name);
        this.origin = origin;
    }

    public String getOrigin(){
        return origin;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.origin);
    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override
        public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        @Override
        public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };

    protected Brand(Parcel source){
        id = source.readLong();
        name = source.readString();
        origin = source.readString();
    }

}



