package yuqi.amc.SQLiteData;

import android.os.Parcel;

public class Model extends DataStruct {

    public static final String CREATE_STATEMENT = "CREATE TABLE MODEL " +
            "(" +
            "ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
            "MODEL_NAME TEXT NOT NULL," +
            "BODY_TYPE TEXT NOT NULL," +
            "DRIVE_TYPE TEXT NOT NULL," +
            "BRAND_NAME TEXT NOT NULL," +
            "FOREIGN KEY(BRAND_NAME) REFERENCES BRAND(BRAND_NAME)" +
            ");";

    private String bodyType;
    private String brandName;
    private String driveType;

    public Model(long id, String name, String bodyType, String driveType, String brandName){
        super(id, name);
        this.bodyType = bodyType;
        this.driveType = driveType;
        this.brandName = brandName;
    }

    public String getBodyType(){
        return bodyType;
    }

    public String getBrandName(){
        return brandName;
    }

    public String getDriveType(){ return driveType; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.bodyType);
        dest.writeString(this.driveType);
        dest.writeString(this.brandName);
    }

    public static final Creator<Model> CREATOR = new Creator<Model>() {
        @Override
        public Model createFromParcel(Parcel source) {
            return new Model(source);
        }

        @Override
        public Model[] newArray(int size) {
            return new Model[size];
        }
    };

    protected Model(Parcel source){
        id = source.readLong();
        name = source.readString();
        bodyType = source.readString();
        driveType = source.readString();
        brandName = source.readString();
    }

}

